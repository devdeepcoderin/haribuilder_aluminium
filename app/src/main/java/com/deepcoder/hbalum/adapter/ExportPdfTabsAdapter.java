package com.deepcoder.hbalum.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ExportPdfTabsAdapter extends FragmentStateAdapter {

    private final ArrayList<String> titles = new ArrayList<>();
    private final ArrayList<Fragment> fragments = new ArrayList<>();

    public ExportPdfTabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ExportPdfTabsAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public int getCount() {
        return fragments.size();
    }

    public void addFragment(String title, Fragment fragment) {
        titles.add(title);
        fragments.add(fragment);
    }

    public void addFragment(Fragment fragment) {
        titles.add("");
        fragments.add(fragment);
    }

    public String getPageTitle(int position) {
        return titles.get(position);
    }

    public Fragment getFragment(int position) {
        return fragments.get(position);
    }
}
