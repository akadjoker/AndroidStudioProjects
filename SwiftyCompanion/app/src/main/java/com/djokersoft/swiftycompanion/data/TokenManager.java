package com.djokersoft.swiftycompanion.data;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREF_NAME = "OAuth2Tokens";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_EXPIRY_TIME = "expiry_time";

    private final SharedPreferences preferences;
    private static TokenManager instance;

    private TokenManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context.getApplicationContext());
        }
        return instance;
    }


    public void storeTokens(String accessToken, String refreshToken) {
        // Por padrão expira para 2 horas (7200 segundos)
        long expiryTime = System.currentTimeMillis() + (7200 * 1000);

        preferences.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .putLong(KEY_EXPIRY_TIME, expiryTime)
                .apply();
    }


    public void storeToken(String accessToken, long expiryTime) {
        preferences.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putLong(KEY_EXPIRY_TIME, expiryTime)
                .apply();
    }

    public String getAccessToken() {
        return preferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public String getRefreshToken() {
        return preferences.getString(KEY_REFRESH_TOKEN, null);
    }

    public boolean hasToken() {
        return getAccessToken() != null;
    }

    public boolean hasTokens() {
        return getAccessToken() != null && getRefreshToken() != null;
    }

    public boolean isTokenExpired() {
        long expiryTime = preferences.getLong(KEY_EXPIRY_TIME, 0);

        return System.currentTimeMillis() > (expiryTime - 10000);
    }

    public void clearTokens() {
        preferences.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_EXPIRY_TIME)
                .apply();
    }
}