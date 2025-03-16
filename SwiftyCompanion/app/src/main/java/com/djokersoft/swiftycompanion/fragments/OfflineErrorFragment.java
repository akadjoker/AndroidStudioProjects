package com.djokersoft.swiftycompanion.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.djokersoft.swiftycompanion.MainActivity;
import com.djokersoft.swiftycompanion.databinding.FragmentOfflineErrorBinding;

public class OfflineErrorFragment extends Fragment {

    private FragmentOfflineErrorBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOfflineErrorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        String errorMessage = "Unable to connect to 42 API";
        if (getArguments() != null && getArguments().containsKey("error_message")) {
            errorMessage = getArguments().getString("error_message");
        }


        binding.textViewErrorMessage.setText(errorMessage);

        if (!((MainActivity) requireActivity()).isNetworkConnected())
        {
            binding.buttonRetry.setVisibility(View.GONE);
        }

        binding.buttonRetry.setOnClickListener(v ->
        {

            ((MainActivity) requireActivity()).ensureAuthentication();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}