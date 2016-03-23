package com.dhenao.miestadio.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClaseGestionTabs extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentos = new ArrayList<>();
    private final List<String> titulosFragmentos = new ArrayList<>();

    public ClaseGestionTabs(FragmentManager fm) {
            super(fm);
        }

    @Override
        public Fragment getItem(int position) {
            return fragmentos.get(position);
        }

    @Override
        public int getCount() {
            return fragmentos.size();
        }

    public void addFragment(Fragment fragment, String title) {
            fragmentos.add(fragment);
            titulosFragmentos.add(title);
        }

    @Override
        public CharSequence getPageTitle(int position) {
            return titulosFragmentos.get(position);
        }
}
