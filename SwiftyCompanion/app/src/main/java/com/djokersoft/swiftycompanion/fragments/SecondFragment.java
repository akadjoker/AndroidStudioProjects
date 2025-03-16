package com.djokersoft.swiftycompanion.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.djokersoft.swiftycompanion.adapter.ProfilePagerAdapter;
import com.djokersoft.swiftycompanion.R;
import com.djokersoft.swiftycompanion.data.User;
import com.djokersoft.swiftycompanion.databinding.FragmentSecondBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
            if (user != null) {
                Log.d("SecondFragment", "Received user: " + user.getLogin() + " with level: " + user.getLevel());
                displayUserInfo();
                setupViewPager();
            } else {
                showError("No user data received");
            }
        } else {
            showError("No arguments received");
        }
    }

    private void displayUserInfo() {
        try {
            // Definir título da action bar
            if (getActivity() != null) {
                getActivity().setTitle(user.getLogin());
            }

            // Carregar imagem de perfil
            if (getContext() != null) {
                Glide.with(getContext())
                        .load(user.getImageUrl())
                        .placeholder(R.drawable.profile_placeholder)
                        .error(R.drawable.profile_error)
                        .circleCrop()
                        .into(binding.imageViewProfile);
            }

            // Exibir informações básicas
            binding.textViewLogin.setText(user.getLogin());
            binding.textViewEmail.setText(user.getEmail());
            binding.textViewLocation.setText(user.getLocation());
            binding.textViewWallet.setText("Wallet: " + user.getWallet());

            // Exibir nível
            int levelInt = user.getLevelInteger();
            int percentage = user.getLevelPercentage();
            binding.textViewLevel.setText(String.format("Level %d - %d%%", levelInt, percentage));
            binding.progressBarLevel.setProgress(percentage);

        } catch (Exception e) {
            Log.e("SecondFragment", "Error displaying user info", e);
            showError("Error displaying user information: " + e.getMessage());
        }
    }



    private void setupViewPager() {

        ProfilePagerAdapter pagerAdapter = new ProfilePagerAdapter(this, user);
        binding.viewPager.setAdapter(pagerAdapter);


        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Projects");
                            break;
                        case 1:
                            tab.setText("Skills");
                            break;
                    }
                }
        ).attach();
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        Log.e("SecondFragment", message);
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}