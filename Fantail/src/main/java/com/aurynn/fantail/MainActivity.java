package com.aurynn.fantail;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
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
import com.alwaysallthetime.adnlib.response.PostResponseHandler;
import com.aurynn.fantail.fragments.Compose;
import com.aurynn.fantail.model.Settings;
import com.aurynn.fantail.sql.SettingsDAO;

import java.util.Locale;

public class MainActivity extends Activity implements ActionBar.TabListener, Compose.ComposeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private Handler stream;
    private Handler mentions;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private AppDotNetClient client;

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
                // We can also trigger a refresh in here.
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
        client = new AppDotNetClient( getSettings().getClientId() );
    }

    public AppDotNetClient getClient() {
        return client;
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
            case R.id.action_compose:
                composePost();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void composePost() {
        // We want to create our dialog fragment and switch to it.
        android.app.FragmentManager fm = getFragmentManager();
        // Could set some things up here to pass it forwards.
        Compose frg = new Compose();
        // Composition action has now started.
        frg.setMenuVisibility(false);
        frg.show(fm, "newPost");
    }
    private void refresh() {
        // Refreshes our stream.
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

    public void onPostMessage(DialogFragment dialog) {
        // We have gotten usefulness to happen!
        View v = dialog.getView();
        Compose frg = (Compose) dialog;
        String postText = frg.getPostMessage();
        Log.d("rtext", postText);

        Post post = new Post(postText);

        this.client.createPost( post, new PostResponseHandler() {
            @Override
            public void onSuccess(Post responseData) {
                // hurray!
                // We should notify the main stream
                Log.d("Post new post", "succeeded");
            }
        });
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
                    return new StreamFragment(StreamFragment.PERSONAL_STREAM);
                case 1:
                    return new StreamFragment(StreamFragment.MENTIONS);
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
            }
            return null;
        }
    }

    public static class StreamFragment extends Fragment {

        public static final int PERSONAL_STREAM = 1;
        public static final int MENTIONS = 2;
        public static final int MESSAGES = 3;

        private int mode;

        public StreamFragment(int mode) {
            this.mode = mode;
            Log.d("StreamFragment", "made");
        }

        private Handler action;

        public void refresh() {
            // Handles refreshing the stream and rendering it to ourself.
            final StreamFragment closure = this;

            MainActivity activity = (MainActivity) getActivity();
            switch (mode) {
                case PERSONAL_STREAM:
                    activity.getClient().retrievePersonalizedStream(responseHandler);
                    return;
                case MENTIONS:
                    activity.getClient().retrievePostsMentioningCurrentUser(responseHandler);
//                case MESSAGES:
//                    activity.getClient().retrieveCurrentUserMessages();
            }


        }
        private PostListResponseHandler responseHandler = new PostListResponseHandler() {
            @Override
            public void onSuccess(final PostList responseData) {
                // This happens on the background thread.
                // We are using a handler to pass back to the UI thread in order to update the
                // page and provide refresh powers.
                // In order to do this, we need an thing that can be passed the resultset.
                // Do we just pass the resultset forward via a message?
                action.post(new Runnable() {
                    @Override
                    public void run() {
                        ScrollView root = (ScrollView) getView();//.getActivity().findViewById(R.layout.fragment_stream);
//                            root.removeAllViews(); // Rip everything out of the root.
                        View progress = root.findViewById(R.id.progressBar);
                        progress.setVisibility(View.GONE);
                        LinearLayout inner = (LinearLayout) getActivity().findViewById(R.id.innerLinearLayout);
                        // This code must be abstracted away as part of the refresh/update system.

                        for (Post post : responseData) {
                            Log.d("response", "length: " + responseData.size());
                            View v = View.inflate(getActivity(), R.layout.component_post, null);
                            TextView sender = (TextView) v.findViewById(R.id.nameView);
                            TextView contentBlock = (TextView) v.findViewById(R.id.contentView);
                            contentBlock.setText(post.getText());
                            sender.setText(post.getUser().getName().toString()
                                    + " ("
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
                        root.invalidate(); // Refresh-yitimes
                    }
                });
            }
        };


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            Log.d("StreamFragment", "Trying to create view...");
            View rootView = inflater.inflate(R.layout.fragment_stream, container, false);

            action = new Handler();
            refresh();

            return rootView;
        }
    }

}
