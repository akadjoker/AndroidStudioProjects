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

import com.djokersoft.swiftycompanion.data.Project;
import com.djokersoft.swiftycompanion.adapter.ProjectsAdapter;
import com.djokersoft.swiftycompanion.databinding.FragmentProjectsBinding;

import java.util.ArrayList;
import java.util.List;
public class ProjectsFragment extends Fragment {
    private FragmentProjectsBinding binding;
    private List<Project> projects;
    private int completedProjects;
    private int failedProjects;

    public static ProjectsFragment newInstance(List<Project> projects, int completed, int failed) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("projects", new ArrayList<>(projects));
        args.putInt("completed", completed);
        args.putInt("failed", failed);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProjectsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            projects = getArguments().getParcelableArrayList("projects");
            completedProjects = getArguments().getInt("completed", 0);
            failedProjects = getArguments().getInt("failed", 0);

            setupProjectsView();
        }
    }

    private void setupProjectsView() {

        binding.textViewCompletedProjects.setText(String.valueOf(completedProjects));
        binding.textViewFailedProjects.setText(String.valueOf(failedProjects));


        if (projects == null || projects.isEmpty()) {
            binding.textViewNoProjects.setVisibility(View.VISIBLE);
            binding.recyclerViewProjects.setVisibility(View.GONE);
            return;
        }

        binding.textViewNoProjects.setVisibility(View.GONE);
        binding.recyclerViewProjects.setVisibility(View.VISIBLE);


        ProjectsAdapter adapter = new ProjectsAdapter(projects);
        binding.recyclerViewProjects.setAdapter(adapter);
        binding.recyclerViewProjects.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewProjects.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}