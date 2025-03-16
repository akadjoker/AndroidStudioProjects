package com.djokersoft.swiftycompanion.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.djokersoft.swiftycompanion.service.ApiService;
import com.djokersoft.swiftycompanion.MainActivity;
import com.djokersoft.swiftycompanion.R;
import com.djokersoft.swiftycompanion.data.User;
import com.djokersoft.swiftycompanion.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private static final String TAG = "djokersoft";

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSearch.setOnClickListener(v ->
                {
                    searchUser();

                });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void searchUser()
    {
        String username = binding.editTextSearch.getText().toString().trim();
        if (username.isEmpty())
        {
            binding.editTextSearch.setError("Please enter a username");
            Log.d(TAG,"Username is empty");
            return;
        }

        // Esconder o teclado
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
       imm.hideSoftInputFromWindow(binding.editTextSearch.getWindowToken(), 0);

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.buttonSearch.setEnabled(false);
        binding.textViewStatus.setVisibility(View.GONE);

        ApiService.searchUser(requireContext(), username, new ApiService.ApiCallback<User>()
        {
            @Override
            public void onSuccess(User user)
            {

                requireActivity().runOnUiThread(() -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.buttonSearch.setEnabled(true);

                    Log.d(TAG,"Found user"+user.toString());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("user", user);
                    NavController navController = NavHostFragment.findNavController(FirstFragment.this);
                    navController.navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
                });

            }

            @Override
            public void onError(String errorMessage)
            {
                requireActivity().runOnUiThread(() -> {

                binding.progressBar.setVisibility(View.GONE);
                binding.buttonSearch.setEnabled(true);
                binding.textViewStatus.setText(errorMessage);
                binding.textViewStatus.setVisibility(View.VISIBLE);
                Log.d(TAG,"Error serach user"+errorMessage);

                if (errorMessage.contains("Not authenticated") || errorMessage.contains("Authentication failed")  )
                {

                    if (getActivity() instanceof MainActivity)
                    {
                        ((MainActivity) getActivity()).ensureAuthentication();
                    }
                }else
                    if (errorMessage.contains("Unable to resolve"))
                    {
                            Log.d(TAG,"goto Error serach user"+errorMessage);
                        if (getActivity() instanceof MainActivity)
                        {
                            ((MainActivity) getActivity()).showOfflineErrorFragment(errorMessage);
                        }
                    }

            });
            }
        });
    }

}