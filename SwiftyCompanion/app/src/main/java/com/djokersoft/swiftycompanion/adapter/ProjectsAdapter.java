package com.djokersoft.swiftycompanion.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.djokersoft.swiftycompanion.data.Project;
import com.djokersoft.swiftycompanion.R;

import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder> {
    private final List<Project> projects;

    public ProjectsAdapter(List<Project> projects) {
        this.projects = projects;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.bind(project);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewProjectName;
        private final TextView textViewProjectStatus;
        private final TextView textViewProjectMark;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProjectName = itemView.findViewById(R.id.textViewProjectName);
            textViewProjectStatus = itemView.findViewById(R.id.textViewProjectStatus);
            textViewProjectMark = itemView.findViewById(R.id.textViewProjectMark);
        }

        public void bind(Project project) {
            textViewProjectName.setText(project.getName());
            textViewProjectStatus.setText(project.getStatusDisplay());
            textViewProjectMark.setText(project.getMarkDisplay());

            // Definir cor com base no status
            int color;
            if (!project.isFinished()) {
                color = itemView.getContext().getResources().getColor(R.color.colorPrimary);
            } else if (project.isSuccessful()) {
                color = itemView.getContext().getResources().getColor(android.R.color.holo_green_light);
            } else {
                color = itemView.getContext().getResources().getColor(android.R.color.holo_red_light);
            }
            textViewProjectStatus.setTextColor(color);
        }
    }
}