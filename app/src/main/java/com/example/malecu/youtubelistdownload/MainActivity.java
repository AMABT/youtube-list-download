package com.example.malecu.youtubelistdownload;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnGetUrlFragmentInteractionListener {

    protected String TAG = "MainActivity";
    protected YtRestClient ytRestClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // show start fragment - input url
        fab.setVisibility(View.INVISIBLE);
        findViewById(R.id.loading_spinner).setVisibility(View.INVISIBLE);
        setContent(new GetUrlFragment());

        // init rest client
        ytRestClient = new YtRestClient(getApplicationContext());
    }

    /**
     * Set fragment for main content
     *
     * @param fragment
     */
    protected void setContent(Fragment fragment) {
        // Replace content layout with fragment
        // If a fragment already exists there, it will be replaced with the new one
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        fragmentTransaction.replace(R.id.content_main, fragment);
        //fragmentTransaction.remove(fragmentManager.findFragmentById(R.id.content_main));
        //fragmentTransaction.add(R.id.content_main, fragment);
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            setContent(new GetUrlFragment());
            fab.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void receiveYoutubeUrl(String url) {

        final MainActivity mainActivity = this;
        findViewById(R.id.loading_spinner).setVisibility(View.VISIBLE);

        // TODO Change OnSuccessListener to List<Video>
        ytRestClient.getInfoAsync(url, new OnSuccessListener<List<Video>>() {
            @Override
            public void onSuccess(List<Video> value) {

                final List<Video> videoList = value;

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        findViewById(R.id.loading_spinner).setVisibility(View.INVISIBLE);
                        FloatingActionButton fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
                        fab.setVisibility(View.VISIBLE);
                        mainActivity.setContent(VideoFragment.newInstance(videoList));
                    }
                });
            }
        }, new OnErrorListener() {
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        });
    }
}
