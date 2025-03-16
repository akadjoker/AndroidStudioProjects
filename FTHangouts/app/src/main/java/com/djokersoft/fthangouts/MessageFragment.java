// MessageFragment.java
package com.djokersoft.fthangouts;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djokersoft.fthangouts.adapter.MessageAdapter;
import com.djokersoft.fthangouts.database.DatabaseHelper;
import com.djokersoft.fthangouts.model.Contact;
import com.djokersoft.fthangouts.model.Message;
import com.djokersoft.fthangouts.utils.MessageEvent;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private static final String TAG = "djokersoft";
    private static final int REQUEST_SEND_SMS = 1;
    
    private RecyclerView recyclerViewMessages;
    private TextView textViewContactName;
    private TextView textViewEmpty;
    private EditText editTextMessage;
    private MaterialButton buttonSendMessage;
    
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private long contactId;
    private Contact contact;
    private DatabaseHelper dbHelper;
    private BroadcastReceiver messageReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);


        messageList = new ArrayList<>();

        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        textViewContactName = view.findViewById(R.id.textViewContactName);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        buttonSendMessage = view.findViewById(R.id.buttonSendMessage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layoutManager);

        messageAdapter = new MessageAdapter(getContext(), messageList);
        recyclerViewMessages.setAdapter(messageAdapter);

        editTextMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !messageList.isEmpty()) {

                recyclerViewMessages.post(() ->
                        recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1)
                );
            }
        });

        //setupMessageReceiver();


        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        try
        {
            dbHelper = DatabaseHelper.getInstance(requireContext());

            Log.d(TAG, "onViewCreated");

            if (getArguments() != null && getArguments().containsKey("contact_id"))
            {
                contactId = getArguments().getLong("contact_id");
                loadContactAndMessages(contactId);
                Toast.makeText(getContext(), "Load contact: " + contactId, Toast.LENGTH_LONG).show();
            } else
            {

                NavHostFragment.findNavController(this).navigateUp();
                return;
            }

            Log.d(TAG, "MESSAFE :create layous");


            buttonSendMessage.setOnClickListener(v -> {
                try {
                     sendMessage();
                    Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                    Toast.makeText(getContext(), "Error sending message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Erro ao enviar mensagem", e);
                }
            });

            MessageEvent.getNewMessageEvent().observe(getViewLifecycleOwner(), contactIdFromEvent ->
            {
                if (contactIdFromEvent == contactId)
                {
                    loadMessages();
                    Log.d(TAG, "Message list updated due to new message event");
                }
            });

        } catch (Exception e)
        {

            Toast.makeText(getContext(), "RECEBE MESSAGE: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "onViewCreated", e);
        }


    }

    private void setupMessageReceiver() {
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                long receivedContactId = intent.getLongExtra("contact_id", -1);

                if (receivedContactId == contactId)
                {
                    loadMessages();

                    Log.d(TAG, "Message fragment updated with new message");
                }
            }
        };
    }

    private void loadContactAndMessages(long contactId)
    {
        try {

            Log.d(TAG, "loadContactAndMessages");

        contact = dbHelper.getContact(contactId);
        
        if (contact != null)
        {

            textViewContactName.setText(contact.getName());
            
            loadMessages();
        } else
        {
            Toast.makeText(getContext(), "Contact not found", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        }
        } catch (Exception e)
        {
            Toast.makeText(getContext(), "Error: loadContactAndMessages" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Erro :loadContactAndMessages", e);
        }
    }
    
    private void loadMessages() {
        try {

            Log.d(TAG, "Loaded  messages for contact: " + contact.getName()+" ID "+  contact.getId());
            List<Message> messages = dbHelper.getMessagesForContact(contactId);
            /*
            for (Message message : messages)
            {
                Log.d(TAG, "Message: " + message.getContent());
            }

*/
            messageAdapter.updateList(messages);



            if (!messageList.isEmpty())
            {
                recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
                textViewEmpty.setVisibility(View.GONE);
            } else
            {
                textViewEmpty.setVisibility(View.VISIBLE);
            }

            Log.d(TAG, "Loaded " + messageList.size() + " messages for contact: " + contact.getName());

    } catch (Exception e) {

        Toast.makeText(getContext(), "Load Messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Load Messages", e);
    }
    }
    
    private void sendMessage() {

            String messageText = editTextMessage.getText().toString().trim();

            if (messageText.isEmpty())
            {
                return;
            }


            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            {

                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
                return;
            }


        

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(contact.getPhoneNumber(), null, messageText, null, null);
            
            Message message = new Message(contactId, messageText, DatabaseHelper.getCurrentTimestamp(), true);
            dbHelper.addMessage(message);

          //   message = new Message(contactId, "OK", DatabaseHelper.getCurrentTimestamp(), false);
           // dbHelper.addMessage(message);
            editTextMessage.setText("");
            
            loadMessages();
            
            Log.d(TAG, "Message sent to: " + contact.getName() + ", phone: " + contact.getPhoneNumber());
        } catch (Exception e) {
            Toast.makeText(getContext(), "Failed to send message: " + e.getMessage(),Toast.LENGTH_LONG).show();
            Log.e(TAG, "Error sending message", e);
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        // Registrar o receiver
      //  LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
        //        messageReceiver, new IntentFilter("com.djokersoft.fthangouts.NEW_MESSAGE"));

        loadMessages();
    }

    @Override
    public void onPause() {
        super.onPause();


    //    if (messageReceiver != null) {
     //       LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(messageReceiver);
     //   }
    }

    // No MessageFragment
    private void observeSoftKeyboard() {
        final View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect r = new Rect();
            private int lastVisibleHeight;

            @Override
            public void onGlobalLayout() {

                rootView.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height();


                if (lastVisibleHeight > 0 && lastVisibleHeight > visibleHeight + 200) {

                    if (!messageList.isEmpty()) {
                        recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
                    }
                }

                lastVisibleHeight = visibleHeight;
            }
        });
    }
}
