package com.djokersoft.fthangouts.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "language";


    public static void setLocale(Context context, String language) {

        SharedPreferences prefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("appLanguage", language);
        editor.apply();


        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static String getLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        return prefs.getString("appLanguage", "en"); // Inglês como padrão
    }

    public static Context onAttach(Context context) {
        String lang = getLanguage(context);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.createConfigurationContext(config);
        }

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        return context;
    }
    private static void persist(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }


    private static void updateResources(Context context, String language) {
        Locale locale;
        if (language.equals("pt")) {
            locale = new Locale("pt", "PT");
        } else {
            locale = new Locale("en");
        }
        
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();


            configuration.setLocale(locale);



            context.createConfigurationContext(configuration);

    }
}
