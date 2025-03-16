package com.djokersoft.swiftycompanion.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.djokersoft.swiftycompanion.data.User;
import com.djokersoft.swiftycompanion.fragments.ProjectsFragment;
import com.djokersoft.swiftycompanion.fragments.SkillsFragment;

public class ProfilePagerAdapter extends FragmentStateAdapter {
    private final User user;

    public ProfilePagerAdapter(Fragment fragment, User user) {
        super(fragment);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return ProjectsFragment.newInstance(
                        user.getProjects(),
                        user.getCompletedProjects(),
                        user.getFailedProjects()
                );
            case 1:
                return SkillsFragment.newInstance(user.getSkills());

            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Projects, Skills, Achievements
    }
}