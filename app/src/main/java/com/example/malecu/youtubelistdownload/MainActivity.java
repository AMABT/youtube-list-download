package com.example.malecu.youtubelistdownload;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                downloadFiles();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // init API Rest Client
        ytRestClient = new YtRestClient(getApplicationContext());

        // init helper
        DownloadList.get().init(getApplicationContext());
        DownloadList.get().setYtRestClient(ytRestClient);

        // show start fragment - input url
        fab.setVisibility(View.INVISIBLE);
        findViewById(R.id.loading_spinner).setVisibility(View.INVISIBLE);

        // application started from share menu
        if (getIntent().getAction().equals(Intent.ACTION_SEND)) {
            String url = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            setContent(new GetUrlFragment(url));
        } else {
            setContent(new GetUrlFragment());
        }

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
        // as you specify a parent context in AndroidManifest.xml.
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
        final View spinner = findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.VISIBLE);

        /**
         * Call the API to get information for current URL - it can return a list of videos or just one (list with one element)
         * Show video list fragment on success
         */
        final Cancellable cancellable = ytRestClient.getInfoAsync(url, new OnSuccessListener<List<Video>>() {
            @Override
            public void onSuccess(List<Video> value) {

                DownloadList.get().setVideoList(value);

                /**
                 * The HTTP call runs on different thread, runOnUiThread must be used because we change the active fragment
                 */
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        spinner.setVisibility(View.INVISIBLE);
                        FloatingActionButton fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
                        fab.setVisibility(View.VISIBLE);
                        mainActivity.setContent(VideoFragment.newInstance());
                    }
                });
            }
        }, new OnErrorListener() {
            @Override
            public void onError(Exception exception) {
                // This function is not called when the action is canceled, spinner hide must be implemented on cancel too
                spinner.setVisibility(View.INVISIBLE);
                Log.e(TAG, exception.getMessage());
            }
        });

        /**
         * Cancel API call on spinner click
         */
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancellable.cancel();
                spinner.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void downloadFiles() {

        final View spinner = findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.VISIBLE);
        final MainActivity mainActivity = this;

        DownloadList.get().downloadVideoList(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setVisibility(View.INVISIBLE);
                        Snackbar.make(spinner, "Download complete", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        }, new OnErrorListener() {
            @Override
            public void onError(Exception exception) {

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setVisibility(View.INVISIBLE);
                        Snackbar.make(spinner, "Download failed", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });


        /**
         * Cancel API call on spinner click
         */
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadList.get().cancelDownload();
                spinner.setVisibility(View.INVISIBLE);
            }
        });
    }
}
