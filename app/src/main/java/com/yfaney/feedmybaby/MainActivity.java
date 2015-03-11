package com.yfaney.feedmybaby;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    // Control Variables
    private boolean isFirstScreen = true;
    BabyInfoDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(dbManager == null) dbManager = new BabyInfoDBManager(this);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mDrawerLayout.isDrawerOpen(Gravity.START)){
            mDrawerLayout.closeDrawers();
        }
        /*if(dbManager.countBabies() == 0){
            mTitle = getString(R.string.title_section3);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new BabyInfoFragment())
                    .commit();
        }
        else if(isFirstScreen){
            isFirstScreen = false;
            mTitle = getString(R.string.title_section1);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new FeedInfoFragment())
                    .commit();
        }*/
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(dbManager == null) dbManager = new BabyInfoDBManager(this);
        switch(position){
            case 0:
                if(dbManager.countBabies() > 0){
                    mTitle = getString(R.string.title_section1);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, FeedInfoFragment.newInstance())
                            .commit();
                }
                else{
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, BabyInfoFragment.newInstance())
                            .commit();
                    Toast.makeText(this, getString(R.string.toast_baby_info_input), Toast.LENGTH_SHORT).show();
                }
                //fragmentManager.beginTransaction().replace(R.id.container, FeedInfo);
                break;
            case 1:
                if(dbManager.countBabies() > 0){
                    mTitle = getString(R.string.title_section2);
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, GraphFragment.newInstance())
                            .commit();
                }
                else{
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, BabyInfoFragment.newInstance())
                            .commit();
                    Toast.makeText(this, getString(R.string.toast_baby_info_input), Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BabyInfoFragment.newInstance()).commit();
                break;
            case 3:
                mTitle = getString(R.string.title_section9);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, DonateFragment.newInstance()).commit();
                break;
        }
        //fragmentManager.beginTransaction()
        //        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
        //        .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                mTitle = getString(R.string.title_section9);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }else if(id == R.id.action_export){
            exportFile();
            return true;
        }else if(id == R.id.action_send_to){
            sendEmail();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(BabyInfo id) {

    }
    @Override
    public void onFragmentChange(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }
    @Override
    public void onFragmentAdd(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public void callAddActivity(Class activity) {
        Intent i = new Intent(this, activity);
        startActivity(i);
    }

    @Override
    public void callAddActivityWithValues(Class activity, String[] values){
        Intent intent = new Intent(this, activity);
        intent.putExtra("PARAM_LENGTH", values.length);
        for(int i = 0 ; i < values.length ; i++){
            String paramName = "PARAM_" + Integer.toString(i);
            intent.putExtra(paramName, values[i]);
        }
        startActivity(intent);
    }

    @Override
    public void callEditActivity(Class activity, int index){
        Intent i = new Intent(this, activity);
        i.putExtra("EditMode", true);
        i.putExtra("EditIndex", index);
        startActivity(i);
    }
    @Override
    public void callEditActivityWithValues(Class activity, String[] values, int index){
        Intent intent = new Intent(this, activity);
        intent.putExtra("EditMode", true);
        intent.putExtra("EditIndex", index);
        intent.putExtra("PARAM_LENGTH", values.length);
        for(int i = 0 ; i < values.length ; i++){
            String paramName = "PARAM_" + Integer.toString(i);
            intent.putExtra(paramName, values[i]);
        }
        startActivity(intent);
    }


    // UI Control Event
    public void onBabyAddClicked(View v){
        Intent i = new Intent(this, NewBabyActivity.class);
        startActivity(i);
    }
    public void onFeedAddClicked(View v){
        Intent i = new Intent(this, NewBabyActivity.class);
        startActivity(i);
    }
    public void onDonateClicked(View v){
        Locale current = getResources().getConfiguration().locale;
        String url = "";
        if(current.getCountry().equals("KR")){
            url = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=YH696WG9F8VFN&lc=KR&item_name=yfaney%2fMobile%20Application&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted";
        }else{
            url = "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=B2JQTM2Q47Y34&lc=US&item_name=yfaney%2fMobile%20Application&amount=1%2e00&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donate_LG%2egif%3aNonHosted";
        }
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    /**
     * Export the user list into CSV file
     * */
    private boolean exportFile(){
        Time now = new Time();
        now.setToNow();
        String fileName = "FeedMyBaby-" + now.format("%Y-%m-%d %H:%M:%S") + ".csv";
        String filePath = "/FeedMyBaby";
        if(dbManager.exportDataIntoCSV(filePath, fileName)){
            Toast.makeText(this, "Data was exported into " + "<SD Card Path>/FeedMyBaby/" + fileName, Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            Toast.makeText(this, "Data was not saved. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private boolean sendEmail(){
        Time now = new Time();
        now.setToNow();
        String fileName = "FeedMyBaby-" + now.format("%Y-%m-%d %H:%M:%S") + ".csv";
        String filePath = "/FeedMyBaby";
        if(dbManager.exportDataIntoCSV(filePath, fileName)){
            // Send e-Mail Routine
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            //String userID = getIntent().getStringExtra("UserID");
            //intent.putExtra(Intent.EXTRA_EMAIL, new String[] { adminEmail });
            intent.putExtra(Intent.EXTRA_SUBJECT, "FeedMyBaby Data - " + now.format("%Y-%m-%d %H:%M:%S"));
            intent.putExtra(Intent.EXTRA_TEXT, "Sent by FeedMyBaby for Android");
            File root = Environment.getExternalStorageDirectory();
            File path = new File(root, filePath);
            if(path.exists()){
                File file = new File(path, fileName);
                if (!file.exists() || !file.canRead()) {
                    Toast.makeText(this, "Attachment Error", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    Uri uri = Uri.parse("file://" + file);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(Intent.createChooser(intent, "Send email..."));
                    Toast.makeText(this, "Data was exported into " + "<SD Card Path>/FeedMyBaby/" + fileName, Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            else{
                Toast.makeText(this, "Attachment Error", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        else{
            Toast.makeText(this, "Data was not saved. Please try again.", Toast.LENGTH_SHORT).show();
            return false;
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
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
