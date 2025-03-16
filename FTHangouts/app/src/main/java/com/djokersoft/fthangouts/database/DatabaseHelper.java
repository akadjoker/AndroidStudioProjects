
package com.djokersoft.fthangouts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.djokersoft.fthangouts.model.Contact;
import com.djokersoft.fthangouts.model.Message;
import com.djokersoft.fthangouts.utils.GlobalValues;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "FtHangoutsApp";

    private static final String DATABASE_NAME = "ft_hangouts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACTS = "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_BIRTHDAY = "birthday";

    // Tabela de mensagens
    private static final String TABLE_MESSAGES = "messages";
    private static final String KEY_MESSAGE_ID = "id";
    private static final String KEY_CONTACT_ID = "contact_id";

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_IS_SENT = "is_sent";

    // Singleton instance
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void clearAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_MESSAGES, null, null);


        db.delete(TABLE_CONTACTS, null, null);

        Log.d(TAG, "All contacts and messages cleared from database");
    }


    public void clearAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, null, null);
        Log.d(TAG, "All messages cleared from database");
    }


    public void clearMessagesForContact(long contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGES, KEY_CONTACT_ID + " = ?",
                new String[] { String.valueOf(contactId) });
        Log.d(TAG, "Cleared all messages for contact ID: " + contactId);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {



        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_BIRTHDAY + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_MESSAGE_ID + " INTEGER PRIMARY KEY,"
                + KEY_CONTACT_ID + " INTEGER,"
                + KEY_MESSAGE + " TEXT,"

                + KEY_TIMESTAMP + " TEXT,"
                + KEY_IS_SENT + " INTEGER,"
                + "FOREIGN KEY(" + KEY_CONTACT_ID + ") REFERENCES " + TABLE_CONTACTS + "(" + KEY_ID + ")"
                + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);

        Log.d(TAG, "Databases created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    // ============================== CONTATOS ==============================

    public long addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhoneNumber());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_BIRTHDAY, contact.getBirthday());

        long id = db.insert(TABLE_CONTACTS, null, values);
        Log.d(TAG, "Contact added with id: " + id);
        return id;
    }

    public Contact getContact(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Contact contact = null;

        try {
            Cursor cursor = db.query(TABLE_CONTACTS, null, KEY_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow(KEY_ID);
                int nameIndex = cursor.getColumnIndexOrThrow(KEY_NAME);
                int phoneIndex = cursor.getColumnIndexOrThrow(KEY_PHONE);
                int emailIndex = cursor.getColumnIndexOrThrow(KEY_EMAIL);
                int addressIndex = cursor.getColumnIndexOrThrow(KEY_ADDRESS);
                int birthdayIndex = cursor.getColumnIndexOrThrow(KEY_BIRTHDAY);

                contact = new Contact(
                        cursor.getLong(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(phoneIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(addressIndex),
                        cursor.getString(birthdayIndex));
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Column not found in getContact: " + e.getMessage());
        }

        return contact;
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + KEY_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, null);


            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndexOrThrow(KEY_ID);
                int nameIndex = cursor.getColumnIndexOrThrow(KEY_NAME);
                int phoneIndex = cursor.getColumnIndexOrThrow(KEY_PHONE);
                int emailIndex = cursor.getColumnIndexOrThrow(KEY_EMAIL);
                int addressIndex = cursor.getColumnIndexOrThrow(KEY_ADDRESS);
                int birthdayIndex = cursor.getColumnIndexOrThrow(KEY_BIRTHDAY);


                do {
                    Contact contact = new Contact(
                            cursor.getLong(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getString(phoneIndex),
                            cursor.getString(emailIndex),
                            cursor.getString(addressIndex),
                            cursor.getString(birthdayIndex));


                    contactList.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Column not found in getAllContacts: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, "Retrieved " + contactList.size() + " contacts");
        return contactList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhoneNumber());
        values.put(KEY_EMAIL, contact.getEmail());
        values.put(KEY_ADDRESS, contact.getAddress());
        values.put(KEY_BIRTHDAY, contact.getBirthday());


        int result = db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getId()) });

        Log.d(TAG, "Updated contact id: " + contact.getId() + ", rows affected: " + result);
        return result;
    }

    public void deleteContact(long contactId) {
        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_MESSAGES, KEY_CONTACT_ID + " = ?",
                new String[] { String.valueOf(contactId) });


        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contactId) });

        Log.d(TAG, "Deleted contact id: " + contactId);
    }

    public Contact getContactByPhoneNumber(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        String phoneNumber = GlobalValues.formatPhoneNumber(number);
        Contact contact = null;

        try {
            Cursor cursor = db.query(TABLE_CONTACTS, null, KEY_PHONE + "=?",
                    new String[] { phoneNumber }, null, null, null);

            if (cursor.moveToFirst())
            {
                int idIndex = cursor.getColumnIndexOrThrow(KEY_ID);
                int nameIndex = cursor.getColumnIndexOrThrow(KEY_NAME);
                int phoneIndex = cursor.getColumnIndexOrThrow(KEY_PHONE);
                int emailIndex = cursor.getColumnIndexOrThrow(KEY_EMAIL);
                int addressIndex = cursor.getColumnIndexOrThrow(KEY_ADDRESS);
                int birthdayIndex = cursor.getColumnIndexOrThrow(KEY_BIRTHDAY);

                contact = new Contact(
                        cursor.getLong(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(phoneIndex),
                        cursor.getString(emailIndex),
                        cursor.getString(addressIndex),
                        cursor.getString(birthdayIndex));
            }

            if (cursor != null)
            {
                cursor.close();
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Column not found in getContactByPhoneNumber: " + e.getMessage());
        }

        return contact;
    }

    // ============================== MENSAGENS ==============================

    public long addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_ID, message.getContactId());
        values.put(KEY_MESSAGE, message.getContent());

        values.put(KEY_IS_SENT, message.isSent() ? 1 : 0);
        values.put(KEY_TIMESTAMP, message.getTimestamp());
        values.put(KEY_IS_SENT, message.isSent() ? 1 : 0);


        long id = db.insert(TABLE_MESSAGES, null, values);
        Log.d(TAG, "Message added with id: " + id+" contact "+ message.getContactId());
        return id;

    }

    public List<Message> getMessagesForContact(long contactId) {
        List<Message> messageList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MESSAGES +
                " WHERE " + KEY_CONTACT_ID + " = ?" +
                " ORDER BY " + KEY_TIMESTAMP + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(contactId) });

 
            if (cursor != null && cursor.moveToFirst()) {
                int messageIdIndex = cursor.getColumnIndexOrThrow(KEY_MESSAGE_ID);
                int contactIdIndex = cursor.getColumnIndexOrThrow(KEY_CONTACT_ID);
                int messageIndex = cursor.getColumnIndexOrThrow(KEY_MESSAGE);
                int timestampIndex = cursor.getColumnIndexOrThrow(KEY_TIMESTAMP);

                int isSentIndex = cursor.getColumnIndexOrThrow(KEY_IS_SENT);

    
                do {
                    Message message = new Message(
                            cursor.getLong(messageIdIndex),//id
                            cursor.getLong(contactIdIndex),    //contactId
                            cursor.getString(messageIndex),      //content

                            cursor.getString(timestampIndex),
                            cursor.getInt(isSentIndex) == 1);

 
                    messageList.add(message);
                } while (cursor.moveToNext());
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Column not found in getMessagesForContact: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, "Retrieved " + messageList.size() + " messages for contact id: " + contactId);
        return messageList;
    }

    public static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    // ============================== TESTE ==============================

    public void addSampleContacts() {
        // Verificar se já existem contatos
        if (getAllContacts().isEmpty()) {
            List<Contact> sampleContacts = new ArrayList<>();

            sampleContacts.add(new Contact("John Doe", " 910123456", "john.doe@email.com",
                    "Rua da Liberdade, 123, Lisboa", "1990-01-15"));

            sampleContacts.add(new Contact("Maria Silva", "920987654", "maria.silva@email.com",
                    "Avenida da República, 45, Porto", "1985-07-22"));

            sampleContacts.add(new Contact("António Santos", "960111222", "antonio.santos@email.com",
                    "Praça do Comércio, 7, Faro", "1992-03-30"));

            sampleContacts.add(new Contact("Isabel Oliveira", "930333444", "isabel.oliveira@email.com",
                    "Rua dos Clérigos, 28, Braga", "1988-11-05"));

            for (Contact contact : sampleContacts) {
                long contactId = addContact(contact);

                if (contactId != -1)
                {
                    addSampleMessages(contactId+1);
                }
            }

            Log.d(TAG, "Added " + sampleContacts.size() + " sample contacts");
        }
    }

    private void addSampleMessages(long contactId) {
        List<Message> sampleMessages = new ArrayList<>();

        // Mensagens para o primeiro contato (alternando entre enviadas e recebidas)
        String timestamp = getCurrentTimestamp();

        // Mensagem 1 (enviada)
        sampleMessages.add(new Message(contactId, "Olá, tudo bem?", timestamp, true));

        // Adicionar algumas horas ao timestamp para simular diferentes horários
        timestamp = advanceTimestamp(timestamp, 0, 1); // 1 hora depois

        // Mensagem 2 (recebida)
        sampleMessages.add(new Message(contactId, "Tudo trankilo! E contigo?", timestamp, false));

        timestamp = advanceTimestamp(timestamp, 0, 0, 15); // 15 minutos depois

        // Mensagem 3 (enviada)
        sampleMessages.add(new Message(contactId, "Tudo  trakilo! Vamos a um café?", timestamp, true));

        timestamp = advanceTimestamp(timestamp, 0, 0, 5); // 5 minutos depois

        // Mensagem 4 (recebida)
        sampleMessages.add(new Message(contactId, "Claro! Que tal amanhã às 10h?", timestamp, false));

        // Adicionar as mensagens ao banco de dados
        for (Message message : sampleMessages)
        {
            addMessage(message);
        }

        Log.d(TAG, "Added " + sampleMessages.size() + " sample messages for contact id: " + contactId);
    }

    private String advanceTimestamp(String timestamp, int days, int hours)
    {
        return advanceTimestamp(timestamp, days, hours, 0);
    }

    private String advanceTimestamp(String timestamp, int days, int hours, int minutes) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = sdf.parse(timestamp);

            if (date != null) {

                long newTimeMillis = date.getTime()
                        + (days * 24 * 60 * 60 * 1000L)
                        + (hours * 60 * 60 * 1000L)
                        + (minutes * 60 * 1000L);

                return sdf.format(new Date(newTimeMillis));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error advancing timestamp", e);
        }

        return timestamp;
    }
}