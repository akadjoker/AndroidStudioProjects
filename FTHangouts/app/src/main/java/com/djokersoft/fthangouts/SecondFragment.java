
package com.djokersoft.fthangouts;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.djokersoft.fthangouts.database.DatabaseHelper;
import com.djokersoft.fthangouts.model.Contact;
import com.google.android.material.textfield.TextInputEditText;

public class SecondFragment extends Fragment {

    private static final String TAG = "djokersoft";

    private TextInputEditText editTextName, editTextPhone, editTextEmail,
            editTextAddress, editTextBirthday;
    private Button buttonSave, buttonDelete, buttonMessage;

    private long contactId = -1;
    private Contact currentContact;
    private boolean isNewContact = true;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);


        editTextName = view.findViewById(R.id.editTextName);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextAddress = view.findViewById(R.id.editTextAddress);
        editTextBirthday = view.findViewById(R.id.editTextBirthday);

        buttonSave = view.findViewById(R.id.buttonSave);
        buttonDelete = view.findViewById(R.id.buttonDelete);
        buttonMessage = view.findViewById(R.id.buttonMessage);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dbHelper = DatabaseHelper.getInstance(requireContext());

        // Verificar se estamos editando um contato existente ou criando um novo
        if (getArguments() != null && getArguments().containsKey("contact_id")) {
            contactId = getArguments().getLong("contact_id");
            isNewContact = false;


            loadContactDetails(contactId);
        } else {

            buttonDelete.setVisibility(View.GONE);
            buttonMessage.setVisibility(View.GONE);
        }


        setupButtonListeners();
    }

    private void loadContactDetails(long contactId) {
        currentContact = dbHelper.getContact(contactId);

        if (currentContact != null) {
            editTextName.setText(currentContact.getName());
            editTextPhone.setText(currentContact.getPhoneNumber());
            editTextEmail.setText(currentContact.getEmail());
            editTextAddress.setText(currentContact.getAddress());
            editTextBirthday.setText(currentContact.getBirthday());

            Log.d(TAG, "Loaded contact: " + currentContact.toString());
        } else {
            Log.e(TAG, "Contact not found with ID: " + contactId);
            //"Contact not found"
            Toast.makeText(requireContext(), getString(R.string.contact_not_found), Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    private void setupButtonListeners() {
        // Botão Salvar
        buttonSave.setOnClickListener(v -> saveContact());

        // Botão Excluir
        buttonDelete.setOnClickListener(v -> confirmDelete());

        // Botão Mensagem
        buttonMessage.setOnClickListener(v -> goToMessages());
    }

    private void saveContact() {
        // Validar os campos
        if (!validateFields()) {
            return;
        }

        // Criar ou atualizar um objeto Contact
        Contact contact;
        if (isNewContact) {
            contact = new Contact();
        } else {
            contact = currentContact;
        }

        // Preencher os campos
        contact.setName(editTextName.getText().toString().trim());
        contact.setPhoneNumber(editTextPhone.getText().toString().trim());
        contact.setEmail(editTextEmail.getText().toString().trim());
        contact.setAddress(editTextAddress.getText().toString().trim());
        contact.setBirthday(editTextBirthday.getText().toString().trim());

        // Salvar no banco de dados
        if (isNewContact) {
            long newId = dbHelper.addContact(contact);
            Log.d(TAG, "New contact added with ID: " + newId);
            //Contact added successfully
            Toast.makeText(requireContext(), getString(R.string.contact_added), Toast.LENGTH_SHORT).show();
        } else {
            int rowsAffected = dbHelper.updateContact(contact);
            Log.d(TAG, "Contact updated, rows affected: " + rowsAffected);
            //"Contact updated successfully"
            Toast.makeText(requireContext(), getString(R.string.contact_updated), Toast.LENGTH_SHORT).show();
        }

        // Voltar para a lista de contatos
        NavHostFragment.findNavController(this).navigateUp();
    }

    private boolean validateFields() {
        boolean isValid = true;

        //   nome (obrigatório)
        if (editTextName.getText().toString().trim().isEmpty())
        {
            editTextName.setError(getString(R.string.name_required));
            isValid = false;
        }

        //   telefone (obrigatório)
        if (editTextPhone.getText().toString().trim().isEmpty()) {
            editTextPhone.setError(getString(R.string.phone_required));
            isValid = false;
        }

        return isValid;
    }

    private void confirmDelete() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_contact))
                .setMessage(getString(R.string.confirm_delete_contact))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    deleteContact();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void deleteContact() {
        if (contactId != -1) {
            dbHelper.deleteContact(contactId);
            Log.d(TAG, "Contact deleted with ID: " + contactId);
            Toast.makeText(requireContext(), getString(R.string.contact_deleted), Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    private void goToMessages() {
        if (contactId != -1) {
            Bundle bundle = new Bundle();
            bundle.putLong("contact_id", contactId);
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_SecondFragment_to_MessageFragment, bundle);
        }
    }
}