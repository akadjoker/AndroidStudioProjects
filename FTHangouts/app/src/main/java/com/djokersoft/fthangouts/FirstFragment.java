
package com.djokersoft.fthangouts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.djokersoft.fthangouts.adapter.ContactAdapter;
import com.djokersoft.fthangouts.database.DatabaseHelper;
import com.djokersoft.fthangouts.model.Contact;
import com.djokersoft.fthangouts.utils.GlobalValues;
import com.djokersoft.fthangouts.utils.MessageEvent;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements ContactAdapter.OnContactClickListener {
    private static final String TAG = "djokersoft";
    private RecyclerView recyclerViewContacts;
    private TextView textViewEmpty;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList;

    private BroadcastReceiver messageReceiver;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        recyclerViewContacts = view.findViewById(R.id.recyclerViewContacts);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        contactList = new ArrayList<>();
     //   setupMessageReceiver();
        
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getContext() == null)
            return;


        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        contactAdapter = new ContactAdapter(getContext(), contactList, this);
        recyclerViewContacts.setAdapter(contactAdapter);
        

        loadContacts();


        MessageEvent.getNewMessageEvent().observe(getViewLifecycleOwner(), contactIdFromEvent ->
        {
            loadContacts();

            Log.d(TAG, "Contacts fragment updated with new message"+ contactIdFromEvent);
        });




        Log.d(TAG, "onViewCreated called");
    }



    private void loadContacts() {

        if (getContext() == null) {
            Log.e(TAG, "Context is null in loadContacts");
            return;
        }
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
        List<Contact> contacts = dbHelper.getAllContacts();
        
        Log.d(TAG, "loadContacts: Recuperados " + contacts.size() + " contatos");
        
        // Atualizar a lista de contatos mantendo a mesma referência
        contactList.clear();
        contactList.addAll(contacts);
        
        // Notificar o adaptador sobre as mudanças nos dados
        contactAdapter.notifyDataSetChanged();
        

        updateVisibility();
    }

    private void updateVisibility() {
        if (contactList.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerViewContacts.setVisibility(View.GONE);
            Log.d(TAG, "updateVisibility: Mostrando mensagem de lista vazia");
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerViewContacts.setVisibility(View.VISIBLE);
            Log.d(TAG, "updateVisibility: Mostrando lista de contatos");
        }
    }

    @Override
    public void onContactClick(Contact contact) {
        Log.d(TAG, "onContactClick: Contato selecionado: " + contact.getName());
        Bundle bundle = new Bundle();
        bundle.putLong("contact_id", contact.getId());
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
    }

    @Override
    public void onMessageClick(Contact contact) {
        Log.d(TAG, "Evia mensage: Botão de mensagem clicado para: " + contact.getName());


        try {

            Bundle bundle = new Bundle();
            bundle.putLong("contact_id", contact.getId());
            NavHostFragment.findNavController(this).navigate(R.id.action_FirstFragment_to_MessageFragment, bundle);


        } catch (Exception e) {

            Toast.makeText(getContext(), "Error no Evia msg: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Erro no código principal", e);
        }




    }


    private void setupMessageReceiver() {
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                long receivedContactId = intent.getLongExtra("contact_id", -1);

                loadContacts();

                Log.d(TAG, "Contacts fragment updated with new message"+ receivedContactId);

            }
        };
    }



    @Override
    public void onPause() {
        super.onPause();


      //  if (messageReceiver != null)
      //  {
     //       LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(messageReceiver);
      //  }
    }

    @Override
    public void onResume() {
        super.onResume();

        loadContacts();

     //   LocalBroadcastManager.getInstance(requireContext()).registerReceiver(messageReceiver, new IntentFilter("com.djokersoft.fthangouts.NEW_MESSAGE"));
    }

}