package com.djokersoft.swiftycompanion;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;

import com.djokersoft.swiftycompanion.service.ApiService;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.djokersoft.swiftycompanion.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "djokersoft";

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private boolean isAuthenticating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (!isNetworkConnected())
        {
            showOfflineErrorFragment("No network connection");
        }else {

              NavController navController = NavHostFragment.findNavController( getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main));
              appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
              NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
              ensureAuthentication();
        }


    }

    public void ensureAuthentication() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting to 42 API...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiService.ensureValidToken(this, new ApiService.ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result)
            {
                Log.d(TAG, "Authentication successful");
                progressDialog.dismiss();
                isAuthenticating = true;

            }

            @Override
            public void onError(String errorMessage) {
                Log.e(TAG, "Error checking authentication: " + errorMessage);
                progressDialog.dismiss();
                isAuthenticating = false;
                showOfflineErrorFragment(errorMessage);
            }
        });
    }

    public boolean checkAuthentication()
    {
        return isAuthenticating;
    }

    public void showOfflineErrorFragment(String errorMessage) {
        Bundle bundle = new Bundle();
        bundle.putString("error_message", errorMessage);

        NavController navController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main));


        if (navController.getCurrentDestination().getId() != R.id.offlineErrorFragment)
        {

            navController.navigate(R.id.action_global_offlineErrorFragment, bundle);
        }
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = NavHostFragment.findNavController(getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main));
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    public boolean isNetworkConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network activeNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
    }
}

