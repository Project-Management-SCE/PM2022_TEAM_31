package com.subzzz.getoverhere;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.subzzz.getoverhere.Util.UserApi;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "EnhancedIntentService";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState == null) {
            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
            if (fragment == null) {
                fragment = new MapsFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            }
        }

        toolBarSync();
        navigationView = findViewById(R.id.nav_side_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuSetUp();

    }



    private void MenuSetUp() {
        UserApi currentUser = UserApi.getInstance();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_side_view);
        View headerView = navigationView.getHeaderView(0);
        TextView userName = (TextView)headerView.findViewById(R.id.userFullNameText);
        TextView userMail = (TextView)headerView.findViewById(R.id.userEmailText);
        userName.setText(String.format("%s %s", currentUser.getCurrentUser().getFirstName(), currentUser.getCurrentUser().getLastName()));
        userMail.setText(currentUser.getCurrentUser().getEmail());
        switch (currentUser.getUType()) {
            case "Admin":
                displayAdminMenu();
                break;
            case "Driver":
                displayDriverMenu();
                break;
            case "Passenger":
                displayPassengerMenu();
                break;
            default:
                break;
        }
    }

    private void displayPassengerMenu() {
        navigationView.getMenu().setGroupVisible(R.id.nav_group_admin,false);
        navigationView.getMenu().setGroupVisible(R.id.nav_group_driver,false);
    }

    private void displayDriverMenu() {
        navigationView.getMenu().setGroupVisible(R.id.nav_group_admin,false);
        navigationView.getMenu().setGroupVisible(R.id.nav_group_passenger,false);
    }

    private void displayAdminMenu() {
        navigationView.getMenu().setGroupVisible(R.id.nav_group_passenger,false);
        navigationView.getMenu().setGroupVisible(R.id.nav_group_driver,false);
    }


    @SuppressLint("UseSupportActionBar")
    private void toolBarSync() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                replaceFragment(new MapsFragment());
                break;
            case R.id.nav_profile:
                replaceFragment(new ProfileFragment());
                break;
            case R.id.nav_history:
                //todo add history fragment
                break;
            case R.id.nav_approve_new_driver:
                replaceFragment(new ApproveNewDriversFragment());
                break;
            case R.id.nav_manage_users:
                //todo add manage users fragment

                break;
            case R.id.nav_update_rates:
                openRatesDialog();
                break;
            case R.id.nav_act_driver:
                //todo add act as driver fragment/action
                actAs("Driver");
                break;
            case R.id.nav_act_passenger:
                //todo add act as passenger fragment/action
                actAs("Passenger");
                break;
            case R.id.nav_describe_car:
                //todo add describe car fragment
                break;
            case R.id.nav_register_as_driver:
                //todo add register as driver fragment
                replaceFragment(new RegisterAsDriverFragment());
                break;
            case R.id.nav_share_app:
                //todo
                break;
            case R.id.nav_settings:
                replaceFragment(new SettingsFragment());
                break;
            case R.id.nav_sign_out:
                signOutUser();
                break;
            default:
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openRatesDialog() {
        UpdateRatesDialog updateRatesDialog = new UpdateRatesDialog();
        updateRatesDialog.show(getSupportFragmentManager(),null);
    }


    private void actAs(String act_as) {
        UserApi.getInstance().setCurrentStatus(act_as);
    }



    private void replaceFragment(Fragment fragment) {
        Fragment container = fragmentManager.findFragmentById(R.id.fragment_container);
        assert container != null;
        if(checkFragmentInstance(fragment,container)){
            return;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

    private boolean checkFragmentInstance(Fragment frag1, Fragment container){
        return frag1.getClass().equals(container.getClass());
    }


    private void signOutUser() {
        mAuth.signOut();
        UserApi.getInstance().setCurrentUser(null);
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}