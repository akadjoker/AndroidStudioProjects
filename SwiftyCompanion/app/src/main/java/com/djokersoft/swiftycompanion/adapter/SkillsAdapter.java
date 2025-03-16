package com.djokersoft.swiftycompanion.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.djokersoft.swiftycompanion.R;
import com.djokersoft.swiftycompanion.data.Skill;

import java.util.List;

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.SkillViewHolder> {
    private final List<Skill> skills;

    public SkillsAdapter(List<Skill> skills) {
        this.skills = skills;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        Skill skill = skills.get(position);
        holder.bind(skill);
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    static class SkillViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewSkillName;
        private final TextView textViewSkillLevel;
        private final ProgressBar progressBarSkill;

        public SkillViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSkillName = itemView.findViewById(R.id.textViewSkillName);
            textViewSkillLevel = itemView.findViewById(R.id.textViewSkillLevel);
            progressBarSkill = itemView.findViewById(R.id.progressBarSkill);
        }

        public void bind(Skill skill) {
            textViewSkillName.setText(skill.getName());
            textViewSkillLevel.setText(skill.getLevelDisplay());
            progressBarSkill.setProgress(skill.getLevelPercentage());
        }
    }
}
