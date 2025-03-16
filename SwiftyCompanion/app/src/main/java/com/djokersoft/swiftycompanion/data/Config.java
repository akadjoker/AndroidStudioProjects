package com.djokersoft.swiftycompanion.data;

import com.djokersoft.swiftycompanion.BuildConfig;

public class Config {

    public static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    public static final String CLIENT_SECRET = BuildConfig.CLIENT_SECRET;
    public static final String REDIRECT_URI = "com.djokersoft.swiftycompanion://oauth-callback";
    public static final String AUTH_URL = "https://api.intra.42.fr/oauth/authorize";
    public static final String TOKEN_URL = "https://api.intra.42.fr/oauth/token";
    public static final String API_BASE_URL = "https://api.intra.42.fr/v2/";
}