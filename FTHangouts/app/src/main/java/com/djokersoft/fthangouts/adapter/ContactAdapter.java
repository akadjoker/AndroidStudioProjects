// ContactAdapter.java
package com.djokersoft.fthangouts.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.djokersoft.fthangouts.R;
import com.djokersoft.fthangouts.model.Contact;

import java.util.List;
import java.util.Random;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    
    private final List<Contact> contactList;
    private final Context context;
    private final OnContactClickListener listener;
    
 
    public interface OnContactClickListener {
        void onContactClick(Contact contact);
        void onMessageClick(Contact contact);
    }
    
    public ContactAdapter(Context context, List<Contact> contactList, OnContactClickListener listener) {
        this.context = context;
        this.contactList = contactList;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        
        holder.textViewName.setText(contact.getName());
        holder.textViewPhone.setText(contact.getPhoneNumber());
        
 
        String initial = "";
        if (contact.getName() != null && !contact.getName().isEmpty()) {
            initial = contact.getName().substring(0, 1).toUpperCase();
        }
        holder.textViewInitial.setText(initial);
        
 
        holder.textViewInitial.setBackgroundColor(getRandomColor(contact.getName()));
        
    
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onContactClick(contact);
            }
        });
        
        holder.buttonMessage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMessageClick(contact);
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return contactList.size();
    }
    
 
    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPhone;
        TextView textViewInitial;
        ImageButton buttonMessage;
        
        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewInitial = itemView.findViewById(R.id.textViewInitial);
            buttonMessage = itemView.findViewById(R.id.buttonMessage);
        }
    }
    
    
    private int getRandomColor(String seed) {
        if (seed == null || seed.isEmpty()) {
            seed = "default";
        }

        Random random = new Random(seed.hashCode());
        

        int r = random.nextInt(128) + 128; // 128-255
        int g = random.nextInt(128) + 128; // 128-255
        int b = random.nextInt(128) + 128; // 128-255
        
        return Color.rgb(r, g, b);
    }
    
 
    public void updateList(List<Contact> newList) {
        contactList.clear();
        contactList.addAll(newList);
        notifyDataSetChanged();
    }
}