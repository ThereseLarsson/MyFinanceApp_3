package com.example.thereselarsson.da401a_assignment_1;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

//Main Menu in the app with a navigation drawer
public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private TextView userName;
    private String name;
    private SummaryFragment resultFragment;
    private EnterTransactionFragment enterTransactionFragment;
    private ViewTransactionFragment viewTransactionFragment;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = Startup.db.getPersonName();
        context = getApplicationContext();

        /**
         * code for the navigation drawer
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                userName = findViewById(R.id.navigation_userName);
                userName.setText(name);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //display the GreetingFragment by default
        GreetingFragment greetingFragment = new GreetingFragment();
        setFragment(greetingFragment, false);
    }

    /**
     * (dynamically) changes the fragment located in the MainActivity
     * @param fragment, the fragment to display
     * @param backstack
     */
    private void setFragment(Fragment fragment, boolean backstack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragmentContainer, fragment);

        if(backstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions(); //try to force the commit to run immediately
    }

    /**
     * close the navigation drawer if the back-button on the smartphone is pressed
     * if the back-button is pressed when the navigation drawer is closed, do nothing
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //do nothing
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

    /**
     * method for handling when an item is selected from the drawer navigation
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_result) {
            resultFragment = new SummaryFragment();
            setFragment(resultFragment, false);

        } else if (id == R.id.nav_add) {
            enterTransactionFragment = new EnterTransactionFragment();
            setFragment(enterTransactionFragment, false);

        } else if (id == R.id.nav_view) {
            viewTransactionFragment = new ViewTransactionFragment();
            setFragment(viewTransactionFragment, false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
