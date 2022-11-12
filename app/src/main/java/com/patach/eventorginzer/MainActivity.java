package com.patach.eventorginzer;

import static com.patach.eventorginzer.Utils.Constant.getUserEmail;
import static com.patach.eventorginzer.Utils.Constant.getUserPic;
import static com.patach.eventorginzer.Utils.Constant.getUsername;
import static com.patach.eventorginzer.Utils.Constant.setUserLoginStatus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.patach.eventorginzer.Adapter.ViewPagerAdapter;
import com.patach.eventorginzer.Fragment.EventFragment;
import com.patach.eventorginzer.Fragment.PostFragment;
import com.patach.eventorginzer.Screens.AddEventActivity;
import com.patach.eventorginzer.Screens.AddPostActivity;
import com.patach.eventorginzer.Screens.FriendActivity;
import com.patach.eventorginzer.Screens.LoginActivity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    private DrawerLayout drawer;
    NavigationView navigationView;
    TextView user_name,user_mail;
    CircleImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.may_viewpager);

        tabLayout.setupWithViewPager(viewPager);
        navigationView =findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(this,drawer, (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar),R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();
        View headerView=navigationView.getHeaderView(0);
        user_name=headerView.findViewById(R.id.name_user);
        user_mail=headerView.findViewById(R.id.user_email);
        userImage=headerView.findViewById(R.id.user_image);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                        case R.id.logout:
                        setUserLoginStatus(MainActivity.this, false);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        break;

                        case R.id.home:
                        setUpViewPager(viewPager);
                        break;

                        case R.id.friendlist:
                            startActivity(new Intent(MainActivity.this, FriendActivity.class));
                            break;

                            case R.id.add_event:
                            startActivity(new Intent(MainActivity.this, AddEventActivity.class));
                        break;

                        case R.id.add_post:
                        startActivity(new Intent(MainActivity.this, AddPostActivity.class));
                        break;



                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    @Override
    protected void onStart() {

        user_name.setText(getUsername(MainActivity.this));
        user_mail.setText(getUserEmail(MainActivity.this));
        Picasso.with(this)
                .load(getUserPic(MainActivity.this))
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(userImage);
        setUpViewPager(viewPager);
        super.onStart();
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new EventFragment(),"Events");
        viewPagerAdapter.addFragment(new PostFragment(),"Posts");
        viewPager.setAdapter(viewPagerAdapter);
    }



    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do you want to exit?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        MainActivity.super.onBackPressed();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}