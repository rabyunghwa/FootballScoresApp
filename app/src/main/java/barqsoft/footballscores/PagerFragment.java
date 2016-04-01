package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.service.MyFetchService;
import barqsoft.footballscores.util.LogUtil;
import barqsoft.footballscores.util.NetworkUtil;

/**
 * Created by yehya khaled on 2/27/2015.
 */
public class PagerFragment extends Fragment {

    public static final int NUM_PAGES = 5;
    public ViewPager mPagerHandler;
    private MainScreenFragment[] viewFragments = new MainScreenFragment[5];

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pager_fragment, container, false);
        mPagerHandler = (ViewPager) rootView.findViewById(R.id.pager);

        setUpViewPager(mPagerHandler);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mPagerHandler);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_pager);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

//        myPageAdapter mPagerAdapter = new myPageAdapter(getChildFragmentManager());
//        for (int i = 0;i < NUM_PAGES;i++) {
//            Date fragmentdate = new Date(System.currentTimeMillis()+((i-2)*86400000));
//            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
//            viewFragments[i] = new MainScreenFragment();
//            viewFragments[i].setFragmentDate(mformat.format(fragmentdate));
//            LogUtil.log_i("info", "date string: " + mformat.format(fragmentdate));
//        }

        if (NetworkUtil.isNetworkAvailable()) {
            update_scores();
        } else {
            Toast.makeText(getActivity(), "No internet connection.", Toast.LENGTH_SHORT).show();
        }


//        mPagerHandler.setAdapter(mPagerAdapter);
        //mPagerHandler.setCurrentItem(MainActivity.current_fragment);
        return rootView;
    }

    private void setUpViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.accent_material_light)), "CAT");
//        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.ripple_material_light)), "DOG");
//        adapter.addFrag(new DummyFragment(getResources().getColor(R.color.button_material_dark)), "MOUSE");
//        viewPager.setAdapter(adapter);
        myPageAdapter mPagerAdapter = new myPageAdapter(getChildFragmentManager());
        for (int i = 0; i < NUM_PAGES; i++) {
            Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            viewFragments[i] = new MainScreenFragment();
            viewFragments[i].setFragmentDate(mformat.format(fragmentdate));
            LogUtil.log_i("info", "date string: " + mformat.format(fragmentdate));
        }
        mPagerHandler.setAdapter(mPagerAdapter);
    }

    private void update_scores() {
        Intent service_start = new Intent(getActivity(), MyFetchService.class);
        getActivity().startService(service_start);
    }

//    private class myPageAdapter extends FragmentStatePagerAdapter {
//        @Override
//        public Fragment getItem(int i)
//        {
//            return viewFragments[i];
//        }
//
//        @Override
//        public int getCount()
//        {
//            return NUM_PAGES;
//        }
//
//        public myPageAdapter(FragmentManager fm)
//        {
//            super(fm);
//        }
//        // Returns the page title for the top indicator
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return getDayName(getActivity(),System.currentTimeMillis()+((position-2)*86400000));
//        }
//        public String getDayName(Context context, long dateInMillis) {
//            // If the date is today, return the localized version of "Today" instead of the actual
//            // day name.
//
//            Time t = new Time();
//            t.setToNow();
//            int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
//            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
//            if (julianDay == currentJulianDay) {
//                return context.getString(R.string.today);
//            } else if ( julianDay == currentJulianDay +1 ) {
//                return context.getString(R.string.tomorrow);
//            }
//             else if ( julianDay == currentJulianDay -1) {
//                return context.getString(R.string.yesterday);
//            }
//            else {
//                Time time = new Time();
//                time.setToNow();
//                // Otherwise, the format is just the day of the week (e.g "Wednesday".
//                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
//                return dayFormat.format(dateInMillis);
//            }
//        }
//    }

    private class myPageAdapter extends FragmentPagerAdapter {
        @Override
        public Fragment getItem(int i) {
            return viewFragments[i];
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        public myPageAdapter(FragmentManager fm) {
            super(fm);
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return getDayName(getActivity(), System.currentTimeMillis() + ((position - 2) * 86400000));
        }

        public String getDayName(Context context, long dateInMillis) {
            // If the date is today, return the localized version of "Today" instead of the actual
            // day name.

            Time t = new Time();
            t.setToNow();
            int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
            int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
            if (julianDay == currentJulianDay) {
                return context.getString(R.string.today);
            } else if (julianDay == currentJulianDay + 1) {
                return context.getString(R.string.tomorrow);
            } else if (julianDay == currentJulianDay - 1) {
                return context.getString(R.string.yesterday);
            } else {
                Time time = new Time();
                time.setToNow();
                // Otherwise, the format is just the day of the week (e.g "Wednesday".
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
                return dayFormat.format(dateInMillis);
            }
        }
    }
}
