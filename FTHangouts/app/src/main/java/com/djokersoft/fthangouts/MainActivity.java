package com.djokersoft.fthangouts;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;

import com.djokersoft.fthangouts.model.Contact;
import com.djokersoft.fthangouts.model.Message;
import com.djokersoft.fthangouts.utils.GlobalValues;
import com.djokersoft.fthangouts.utils.LocaleHelper;
import com.djokersoft.fthangouts.utils.SMSReceiver;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.djokersoft.fthangouts.database.DatabaseHelper;
import com.djokersoft.fthangouts.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "djokersoft";
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private long backgroundTime;
    private boolean wasInBackground = false;
    private static final int REQUEST_SMS_PERMISSION = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleHelper.setLocale(this, LocaleHelper.getLanguage(this));

       

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);



   
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> 
        {
            if (destination.getId() == R.id.FirstFragment) 
            {
                binding.fab.setImageResource(R.drawable.ic_add);
                binding.fab.setOnClickListener(view -> navController.navigate(R.id.action_FirstFragment_to_SecondFragment));
                binding.fab.show();
            } else if (destination.getId() == R.id.SecondFragment || destination.getId() == R.id.MessageFragment)
            {
                binding.fab.hide();
            }
        });

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

      //  dbHelper.clearAllContacts();

    //    dbHelper.clearAllMessages();


       // dbHelper.addSampleContacts();


        Log.d(TAG, "Database initialized");

        binding.fab.setImageResource(R.drawable.ic_add);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_FirstFragment_to_SecondFragment);
             }
         });

        List<Contact> contacts = dbHelper.getAllContacts();
        Log.d("MainActivity", "Found " + contacts.size() + " contacts in the database");
        loadSavedHeaderColor();
        checkSmsPermissions();

    }


    private void showMessageNotification(String sender, String message)
    {
        String toastMessage = sender + ": " + message;
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        Log.d(TAG, "Message received: " + toastMessage);
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (wasInBackground) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String formattedTime = sdf.format(new Date(backgroundTime));
            String message = getString(R.string.background_time, formattedTime);

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            wasInBackground = false;
        }




    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundTime = System.currentTimeMillis();
        wasInBackground = true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showColorDialog() {
        String[] colors = {
                getString(R.string.red),
                getString(R.string.green),
                getString(R.string.blue),
                getString(R.string.purple)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_header_color)
                .setItems(colors, (dialog, which) -> {
                    int color;
                    switch (which) {
                        case 0: // Red
                            color = getResources().getColor(R.color.red);
                            break;
                        case 1: // Green
                            color = getResources().getColor(R.color.green);
                            break;
                        case 2: // Blue
                            color = getResources().getColor(R.color.blue);
                            break;
                        case 3: // Purple
                            color = getResources().getColor(R.color.purple);
                            break;
                        default:
                            color = getResources().getColor(R.color.primary);
                            break;
                    }

                    changeHeaderColor(color);
                });

        builder.create().show();
    }

    private void changeHeaderColor(int color) {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("headerColor", color);
        editor.apply();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
            binding.toolbar.setBackgroundColor(color);
        }

        getWindow().setStatusBarColor(darker(color, 0.8f));

    }

    private int darker(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    private void loadSavedHeaderColor() {
        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int color = prefs.getInt("headerColor", getResources().getColor(R.color.primary));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
            binding.toolbar.setBackgroundColor(color);
        }

        getWindow().setStatusBarColor(darker(color, 0.8f));

    }

    private void showLanguageDialog() {
        String[] languages = {
                getString(R.string.language_english),
                getString(R.string.language_portuguese)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_language)
                .setItems(languages, (dialog, which) -> {
                    String languageCode;
                    switch (which) {
                        case 0: // English
                            languageCode = "en";
                            break;
                        case 1: // Portuguese
                            languageCode = "pt";
                            break;
                        default:
                            languageCode = "en";
                            break;
                    }

                    LocaleHelper.setLocale(this, languageCode);

                    Toast.makeText(this, R.string.language_changed, Toast.LENGTH_SHORT).show();
                    recreateActivity();
                });

        builder.create().show();
    }

    @SuppressLint("UnsafeIntentLaunch")
    private void recreateActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_settings, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        String currentLanguage = LocaleHelper.getLanguage(this);

        SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
        int currentColor = prefs.getInt("headerColor", getResources().getColor(R.color.primary));

        RadioGroup radioGroupLanguage = view.findViewById(R.id.radioGroupLanguage);
        RadioButton radioEnglish = view.findViewById(R.id.radioEnglish);
        RadioButton radioPortuguese = view.findViewById(R.id.radioPortuguese);

        if (currentLanguage.equals("pt")) {
            radioPortuguese.setChecked(true);
        } else {
            radioEnglish.setChecked(true);
        }

        Button buttonRed = view.findViewById(R.id.buttonRed);
        Button buttonGreen = view.findViewById(R.id.buttonGreen);
        Button buttonBlue = view.findViewById(R.id.buttonBlue);
        Button buttonPurple = view.findViewById(R.id.buttonPurple);

        final int[] selectedColor = { currentColor };
        final String[] selectedLanguage = { currentLanguage };

        buttonRed.setOnClickListener(v -> {
            selectedColor[0] = getResources().getColor(R.color.red);
            highlightSelectedColorButton(buttonRed, buttonGreen, buttonBlue, buttonPurple);
        });

        buttonGreen.setOnClickListener(v -> {
            selectedColor[0] = getResources().getColor(R.color.green);
            highlightSelectedColorButton(buttonGreen, buttonRed, buttonBlue, buttonPurple);
        });

        buttonBlue.setOnClickListener(v -> {
            selectedColor[0] = getResources().getColor(R.color.blue);
            highlightSelectedColorButton(buttonBlue, buttonRed, buttonGreen, buttonPurple);
        });

        buttonPurple.setOnClickListener(v -> {
            selectedColor[0] = getResources().getColor(R.color.purple);
            highlightSelectedColorButton(buttonPurple, buttonRed, buttonGreen, buttonBlue);
        });

        if (currentColor == getResources().getColor(R.color.red)) {
            highlightSelectedColorButton(buttonRed, buttonGreen, buttonBlue, buttonPurple);
        } else if (currentColor == getResources().getColor(R.color.green)) {
            highlightSelectedColorButton(buttonGreen, buttonRed, buttonBlue, buttonPurple);
        } else if (currentColor == getResources().getColor(R.color.blue)) {
            highlightSelectedColorButton(buttonBlue, buttonRed, buttonGreen, buttonPurple);
        } else if (currentColor == getResources().getColor(R.color.purple)) {
            highlightSelectedColorButton(buttonPurple, buttonRed, buttonGreen, buttonBlue);
        }

        radioGroupLanguage.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEnglish) {
                selectedLanguage[0] = "en";
            } else if (checkedId == R.id.radioPortuguese) {
                selectedLanguage[0] = "pt";
            }
        });

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonApply = view.findViewById(R.id.buttonApply);

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonApply.setOnClickListener(v -> {

            boolean needRecreate = false;

            if (selectedColor[0] != currentColor) {
                changeHeaderColor(selectedColor[0]);
            }

            if (!selectedLanguage[0].equals(currentLanguage)) {
                LocaleHelper.setLocale(MainActivity.this, selectedLanguage[0]);
                needRecreate = true;
            }

            dialog.dismiss();

            if (needRecreate) {
                Toast.makeText(MainActivity.this, R.string.language_changed, Toast.LENGTH_SHORT).show();
                recreateActivity();
            }
        });


        Button buttonClearMessages = view.findViewById(R.id.buttonClearMessages);
        Button buttonClearContacts = view.findViewById(R.id.buttonClearContacts);
        boolean haveContacts = false;
        boolean haveMessages = false;

        {
            DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
            List<Contact> contacts = dbHelper.getAllContacts();
            if (!contacts.isEmpty())
            {
                haveContacts = true;
                for (Contact contact : dbHelper.getAllContacts())
                {
                    List<Message> messages =   dbHelper.getMessagesForContact(contact.getId());
                    if (!messages.isEmpty())
                    {
                        haveMessages = true;
                        break;
                    }
                }
            }
        }

        if (!haveContacts)
        {
            buttonClearMessages.setVisibility(View.GONE);
            buttonClearContacts.setVisibility(View.GONE);
        } else
        {
                buttonClearMessages.setVisibility(View.VISIBLE);
                if (!haveMessages)
                    buttonClearContacts.setVisibility(View.VISIBLE);
        }

        buttonClearMessages.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.clear_all_messages)
                    .setMessage(R.string.confirm_clear_messages)
                    .setPositiveButton(R.string.confirm, (confirmDialog, which) -> {

                        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
                        dbHelper.clearAllMessages();


                        Toast.makeText(this,getText( R.string.messages_cleared), Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                        Toast.makeText(MainActivity.this, R.string.language_changed, Toast.LENGTH_SHORT).show();
                        recreateActivity();

                        // Se estiver na tela de mensagens, atualizá-la
                       // MessageEvent.notifyNewMessage(-1); // -1 significa todas as conversas
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });


        buttonClearContacts.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.clear_all_contacts)
                    .setMessage(R.string.confirm_clear_contacts)
                    .setPositiveButton(R.string.confirm, (confirmDialog, which) -> {

                        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
                        dbHelper.clearAllContacts();


                        Toast.makeText(this, getText(R.string.contacts_cleared), Toast.LENGTH_SHORT).show();


                        dialog.dismiss();

                        Toast.makeText(MainActivity.this, R.string.language_changed, Toast.LENGTH_SHORT).show();
                        recreateActivity();


                       // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                      //  navController.popBackStack(R.id.FirstFragment, false);

                        // Notificar que os contatos foram apagados
                       // ContactEvent.notifyContactsChanged();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
    }

    private void highlightSelectedColorButton(Button selected, Button... others) {
        selected.setAlpha(1.0f);
        selected.setScaleX(1.1f);
        selected.setScaleY(1.1f);

        for (Button other : others) {
            other.setAlpha(0.6f);
            other.setScaleX(1.0f);
            other.setScaleY(1.0f);
        }
    }


    private void checkSmsPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.SEND_SMS
                    },
                    REQUEST_SMS_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_SMS_PERMISSION) {
            boolean allGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                Log.d(TAG, "All SMS permissions granted");
            } else {
                Log.d(TAG, "Some SMS permissions were denied");
                Toast.makeText(this, "SMS functionality may not work properly without permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

}