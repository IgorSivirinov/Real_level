package com.example.changelevel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.changelevel.ui.uiMain.tasks.uiTaskList.mind.MindFragment;
import com.example.changelevel.ui.uiMain.tasks.uiTaskList.sport.SportFragment;
import com.example.changelevel.ui.uiMain.tasks.uiTaskList.additional.AdditionalFragment;

public class PageAdapter extends FragmentPagerAdapter {
    private int behavior;
    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm);
        this.behavior = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AdditionalFragment();
            case 1:
                return new SportFragment();
            case 2:
                return new MindFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return behavior;
    }

}
