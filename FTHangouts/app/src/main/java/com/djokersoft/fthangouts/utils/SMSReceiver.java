// SMSReceiver.java
package com.djokersoft.fthangouts.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.djokersoft.fthangouts.MainActivity;
import com.djokersoft.fthangouts.database.DatabaseHelper;
import com.djokersoft.fthangouts.model.Contact;
import com.djokersoft.fthangouts.model.Message;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "djokersoft";
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    private void showMessageNotification(Context ctx, String sender, String message)
    {
        String toastMessage = sender + ": " + message ;
        Toast.makeText(ctx, toastMessage, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Message received: " + toastMessage);
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                
                if (pdus != null) {
                    DatabaseHelper dbHelper = DatabaseHelper.getInstance(context);
                    
                    for (Object pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                        String sender =GlobalValues.formatPhoneNumber( smsMessage.getDisplayOriginatingAddress());
                        String messageBody = smsMessage.getMessageBody();
                        
                        Log.d(TAG, "SMS received from: " + sender + " with message: " + messageBody);

                      //  showMessageNotification(context, sender, messageBody);
                        
                        // Procurar contato com este número
                        Contact contact = dbHelper.getContactByPhoneNumber(sender);
                        
                        if (contact != null)
                        {

                            Message message = new Message(contact.getId(), messageBody, DatabaseHelper.getCurrentTimestamp(), false);
                            dbHelper.addMessage(message);

                            MessageEvent.notifyNewMessage(contact.getId());
/*
                            Intent messageIntent = new Intent("com.djokersoft.fthangouts.NEW_MESSAGE");
                            messageIntent.putExtra("contact_id", contact.getId());
                            LocalBroadcastManager.getInstance(context).sendBroadcast(messageIntent);
                            */
                            Log.d(TAG, "Message saved for contact: " + contact.getName() + " with id: " + contact.getId());
                        } else
                        {
                            // Criar um novo contato com este número ( bônus)
                            Contact newContact = new Contact(
                                    sender, // Usar o número como nome
                                    sender, 
                                    "", // Email vazio
                                    "", // Endereço vazio
                                    ""  // Aniversário vazio
                            );
                            
                            long newContactId = dbHelper.addContact(newContact);
                            
                            if (newContactId != -1) {

                                Message message = new Message(newContactId, messageBody, DatabaseHelper.getCurrentTimestamp(), false);
                                dbHelper.addMessage(message);
                                MessageEvent.notifyNewMessage(newContactId);
/*
                                Intent messageIntent = new Intent("com.djokersoft.fthangouts.NEW_MESSAGE");
                                messageIntent.putExtra("contact_id", newContactId);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(messageIntent);
*/
                                Log.d(TAG, "New contact created with number: " + sender);
                            }
                        }

                    }
                }
            }
        }
    }
}
