package com.djokersoft.weather_app.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.djokersoft.weather_app.Global;
import com.djokersoft.weather_app.UpdatableFragment;
import com.djokersoft.weather_app.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements UpdatableFragment {

    private FragmentDashboardBinding binding;
    TextView textView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textView = binding.textDashboard;
        onTextChanged(Global.sharedText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onTextChanged(String newText)
    {
        textView.setText("Today\n" + newText);
    }
}