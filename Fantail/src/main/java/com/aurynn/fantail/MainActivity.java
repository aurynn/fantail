package com.aurynn.fantail;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.data.Post;
import com.alwaysallthetime.adnlib.data.PostList;
import com.alwaysallthetime.adnlib.response.PostListResponseHandler;
import com.aurynn.fantail.model.Settings;
import com.aurynn.fantail.sql.SettingsDAO;

import java.util.Locale;

public class MainActivity extends Activity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public synchronized Settings getSettings() {

        SettingsDAO dao = new SettingsDAO(this);
        dao.open();
        Settings settings = dao.getSettings();
        dao.close();
        return settings;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            switch (position) {
//
//            }
            // Is this always called?
            Log.d("placeholder", "MAKING A NEW ONE");
            switch (position) {
                case 0:
                    Log.d("placeholder", "Making a streamfragment");
                    return new StreamFragment();
                case 1:
                    Log.d("placeholder", "Making a streamfragment");
                    return new StreamForkment();
//                    return PlaceholderFragment.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public static class StreamFragment extends Fragment {
        public StreamFragment() {

            Log.d("StreamFragment", "made");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Log.d("StreamFragment", "Trying to create view...");
            View rootView = inflater.inflate(R.layout.fragment_stream, container, false);
            SettingsDAO d = new SettingsDAO(getActivity());
            d.open();
            Settings s = d.getSettings();
            d.close();

            Log.d("StreamFragment", "Loaded our client ID of " + s.getClientId().toString());

            AppDotNetClient client = new AppDotNetClient( s.getClientId() );
            // Cool. Now, we need the ADN client we created.
            // our default view is the spinner going SPIN SPIN SPIN

            final LayoutInflater inf = inflater;
            final StreamFragment closure = this;

            final View closureRoot = rootView;
            final Handler mHandler = new Handler();
            // This is not on the main thread?
            client.retrievePersonalizedStream(new PostListResponseHandler() {
                @Override
                public void onSuccess(final PostList responseData) {
                    // Whee!
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ScrollView root = (ScrollView) closureRoot;//.getActivity().findViewById(R.layout.fragment_stream);
//                            root.removeAllViews(); // Rip everything out of the root.
                            View progress = closure.getActivity().findViewById( R.id.progressBar );
                            progress.setVisibility(View.GONE);
                            LinearLayout inner = (LinearLayout) closure.getActivity().findViewById( R.id.innerLinearLayout );
                            for (Post post : responseData ) {
                                Log.d("response", "length: " + responseData.size() );
                                View v = View.inflate(closure.getActivity(), R.layout.component_post, null);
                                TextView sender = (TextView) v.findViewById(R.id.nameView);
                                TextView contentBlock = (TextView) v.findViewById(R.id.contentView);
                                contentBlock.setText(post.getText());
                                sender.setText( post.getUser().getName().toString()
                                        + "("
                                        + post.getUser().getUsername() +
                                        ")"
                                );
//                                ImageView avatar = (ImageView) v.findViewById(R.id.avatarView);
//                                avatar.setImageBitmap( post.getUser().getAvatarImage().g );
                                inner.addView(v);
                            }
                            progress.setVisibility(View.GONE);
                            inner.setVisibility(View.VISIBLE);
                            inner.invalidate();
                            root.invalidate(); // Refresh-yitimes.
                        }
                    });
                }
            });
            return rootView;
        }
    }

    public static class StreamForkment extends Fragment {
        public StreamForkment() {

            Log.d("StreamForkment", "made");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Log.d("StreamForkment", "Trying to create view...");
            View rootView = inflater.inflate(R.layout.fragment_stream, container, false);
            SettingsDAO d = new SettingsDAO(getActivity());
            d.open();
            Settings s = d.getSettings();
            d.close();

            Log.d("StreamForkment", "Loaded our client ID of " + s.getClientId().toString());

            AppDotNetClient client = new AppDotNetClient( s.getClientId() );
            // Cool. Now, we need the ADN client we created.
            // our default view is the spinner going SPIN SPIN SPIN

            final LayoutInflater inf = inflater;
            final StreamForkment closure = this;

            final View closureRoot = rootView;
            final Handler mHandler = new Handler();
            // This is not on the main thread?
            client.retrievePostsMentioningCurrentUser(new PostListResponseHandler() {
                @Override
                public void onSuccess(final PostList responseData) {
                    // Whee!
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ScrollView root = (ScrollView) closureRoot;//.getActivity().findViewById(R.layout.fragment_stream);
//                            root.removeAllViews(); // Rip everything out of the root.
                            View progress = closure.getActivity().findViewById( R.id.progressBar );
                            progress.setVisibility(View.GONE);
                            LinearLayout inner = (LinearLayout) closure.getActivity().findViewById( R.id.innerLinearLayout );
                            for (Post post : responseData ) {
                                Log.d("response", "length: " + responseData.size() );
                                View v = View.inflate(closure.getActivity(), R.layout.component_post, null);
                                TextView sender = (TextView) v.findViewById(R.id.nameView);
                                TextView contentBlock = (TextView) v.findViewById(R.id.contentView);
                                contentBlock.setText(post.getText());
                                sender.setText( post.getUser().getName().toString()
                                        + "("
                                        + post.getUser().getUsername() +
                                        ")"
                                );
//                                ImageView avatar = (ImageView) v.findViewById(R.id.avatarView);
//                                avatar.setImageBitmap( post.getUser().getAvatarImage().g );
                                inner.addView(v);
                            }
                            progress.setVisibility(View.GONE);
                            inner.setVisibility(View.VISIBLE);
                            inner.invalidate();
                            root.invalidate(); // Refresh-yitimes.
                        }
                    });
                }
            });
            return rootView;
        }
    }

}
