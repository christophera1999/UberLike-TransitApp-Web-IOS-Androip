package com.tranxitpro.app.Activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tranxitpro.app.Fragments.OnGoingTrips;
import com.tranxitpro.app.Fragments.PastTrips;
import com.tranxitpro.app.R;

public class HistoryActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String tabTitles[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        String strTag = getIntent().getExtras().getString("tag");
        tabTitles = new String[]{"Past Trips", "Upcoming Trips"};

        viewPager.setAdapter(new SampleFragmentPagerAdapter(tabTitles, getSupportFragmentManager(),
                this));

        if (strTag != null) {
            if (strTag.equalsIgnoreCase("past")) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(1);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[];
        private Context context;

        public SampleFragmentPagerAdapter(String tabTitles[], FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            this.tabTitles = tabTitles;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PastTrips();
                case 1:
                    return new OnGoingTrips();
                default:
                    return new PastTrips();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

}
