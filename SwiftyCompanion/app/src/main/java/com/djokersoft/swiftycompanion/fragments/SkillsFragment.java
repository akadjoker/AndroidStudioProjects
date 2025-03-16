package com.djokersoft.swiftycompanion.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.djokersoft.swiftycompanion.data.Skill;
import com.djokersoft.swiftycompanion.adapter.SkillsAdapter;
import com.djokersoft.swiftycompanion.databinding.FragmentSkillsBinding;

import java.util.ArrayList;
import java.util.List;

public class SkillsFragment extends Fragment {
    private FragmentSkillsBinding binding;
    private List<Skill> skills;

    public static SkillsFragment newInstance(List<Skill> skills) {
        SkillsFragment fragment = new SkillsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("skills", new ArrayList<>(skills));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSkillsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            skills = getArguments().getParcelableArrayList("skills");
            setupSkillsView();
        }
    }

    private void setupSkillsView() {
        if (skills == null || skills.isEmpty()) {
            binding.textViewNoSkills.setVisibility(View.VISIBLE);
            binding.recyclerViewSkills.setVisibility(View.GONE);
            return;
        }

        binding.textViewNoSkills.setVisibility(View.GONE);
        binding.recyclerViewSkills.setVisibility(View.VISIBLE);

        // Use o novo adaptador independente
        SkillsAdapter adapter = new SkillsAdapter(skills);
        binding.recyclerViewSkills.setAdapter(adapter);
        binding.recyclerViewSkills.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewSkills.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}