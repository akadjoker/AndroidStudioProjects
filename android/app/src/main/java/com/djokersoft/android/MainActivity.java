package com.djokersoft.android;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.view.WindowManager;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.view.KeyEvent;
public class MainActivity extends Activity {




        public WebView web;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        web.setLayoutParams(layoutParams);
        setContentView(web);

        //     web.setSystemUiVisibility(
        //   //  View.SYSTEM_UI_FLAG_FULLSCREEN
        //      View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //     | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        //     );

        web.setWebViewClient(new WebViewClient());


        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDatabaseEnabled(true);

        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(true);
        settings.setUseWideViewPort(false);  // ← permite layout mais largo
        settings.setLoadWithOverviewMode(false); // ← força o scale inicial

        //  web.setInitialScale(10);
        web.loadUrl("file:///android_asset/www/index.html");




        //LinearLayout rootLayout = new LinearLayout(this);
        //rootLayout.addView(web);
        //setContentView(rootLayout);
    }
        @Override
        public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    }