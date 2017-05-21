package com.resultstrack.navigationdrawer1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.resultstrack.navigationdrawer1.commonUtilities.DownloadImage;
import com.resultstrack.navigationdrawer1.commonUtilities.RTGlobal;
import com.resultstrack.navigationdrawer1.model.DBAdapter;
import com.resultstrack.navigationdrawer1.model.LocalSettings;
import com.resultstrack.navigationdrawer1.model.appUser;

import java.io.File;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Initialize Database*/
        DBAdapter.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.getSupportActionBar().setTitle("ResultsTrack - Home");
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Dashboard"));
        tabLayout.addTab(tabLayout.newTab().setText("Summary"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final TabPageAdapter adapter = new TabPageAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set User Session default
        setUserSessionDefault();
    }

    private void setUserSessionDefault() {
        appUser oUser = RTGlobal.get_appUser();

        NavigationView nView = (NavigationView)findViewById(R.id.nav_view);
        nView.setNavigationItemSelectedListener(this);

        View header = nView.getHeaderView(0);
        TextView tvName=(TextView)header.findViewById(R.id.tvUserName);
        TextView tvEmail=(TextView)header.findViewById(R.id.tvEmail);
        ImageView ivUserImage = (ImageView)header.findViewById(R.id.ivUserImage);
        tvName.setText(oUser.getFstname() +" " + oUser.getLstname());
        tvEmail.setText(oUser.getEmail());
        //Find Image locally or Download from server
        new DownloadImage(this,oUser.getImage(),ivUserImage).execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Display alert message when back button has been pressed
            //backButtonHandler();
           /* Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            int count = getSupportFragmentManager().getBackStackEntryCount();

            /*if(count > 1)
                getSupportFragmentManager().popBackStack();
            else {
                super.onBackPressed();
            }*/
            if(count==0) {
                backButtonHandler();
            }else {
                if(count==1){
                    tabLayout.setVisibility(View.VISIBLE);
                    Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.pager);
                    if (currentFragment instanceof DashboardFragment) {
                        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                        fragTransaction.detach(currentFragment);
                        fragTransaction.attach(currentFragment);
                        fragTransaction.commit();
                    }
                }
                super.onBackPressed();
            }
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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment childFragment=null;
        Class fragmentClass = null;

        if (id == R.id.nav_register_child) {
            fragmentClass = RegisterChildFragment.class;
        } else if (id == R.id.nav_add_committee_member) {
            fragmentClass = RegisterCMember.class;
        } else if (id == R.id.nav_mom) {
            fragmentClass = SendMom.class;
        } else if (id == R.id.nav_reportIncident) {
            fragmentClass = IncidentReportingFragment.class;
        } else if (id == R.id.nav_surveylist) {
            fragmentClass = SurveyFragment.class;
        } else if (id == R.id.nav_sync) {
            fragmentClass = RegisterChildFragment.class;
        } else if (id == R.id.nav_messages) {
            fragmentClass = RegisterChildFragment.class;
        }else if (id == R.id.nav_signout){
            //Delete User
            appUser user = RTGlobal.get_appUser();
            user.deleteUserData();
            //Clear Cache
            LocalSettings setting = new LocalSettings().getLocalSettings("RememberMe").get(0);
            setting.deleteSetting();
            //Remove File
            File fileDir = getApplicationContext().getFilesDir();
            String fileFullName =fileDir + "/" +setting.getType();
            if(new File(fileFullName).delete()){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
            }
        }

        try {
            childFragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabLayout.setVisibility(View.INVISIBLE);
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, childFragment)
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onFragmentInteraction(String uri){

    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this,R.style.datepicker);
        // Setting Dialog Title
        alertDialog.setTitle("Leave application?");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to leave the application?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.resultstrack3dlogo);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        MainActivity.super.onBackPressed();
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });
        // Showing Alert Message
        alertDialog.show();
    }
}
