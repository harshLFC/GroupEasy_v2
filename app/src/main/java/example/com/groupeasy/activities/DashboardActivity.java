package example.com.groupeasy.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.DashboardPagerAdapter;
import example.com.groupeasy.fragments.GroupFragment;
import example.com.groupeasy.fragments.ProfileFragment;

/**
 * This is a placeholder class, it hosts two fragments
 * 1. Group Fragment
 * 2. Profile Fragment
 * **/

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();

    //Import ui elementss
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DashboardPagerAdapter adapter;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    /** set icons to your tabs*/
    private int[] tabIcons = {
//            R.drawable.eye_white_48,
            R.drawable.ic_user_group_green,
            R.drawable.user_profile_green
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //set layout onStart this is the psvm(s args[]); of android
        super.onCreate(savedInstanceState);
        /*Link to XML layout*/
        setContentView(R.layout.activity_dashboard);
        this.context = DashboardActivity.this;

        mAuth = FirebaseAuth.getInstance();

        /* Calling methods for
        1. Initialising elements by IDs
        2. Viewpager for tabs
        3. Tab customisation
        4. Initialising elements with Listeners*/
        initElementsWithIds();
        setupViewPager(viewPager);
        setupTabIcons();
        initElementsWithListeners();

        //Just display welcome message for a new/user loging in
        Intent intent = getIntent();
        if(intent.hasExtra("userName")){
            Bundle bd = getIntent().getExtras();
            if(!bd.getString("userName").equals(null)){
                WelcomeUser();
            }
        }
    }

    private void WelcomeUser() {
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("members").child(uid);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                Toast.makeText(DashboardActivity.this, "Welcome " + name,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void initElementsWithListeners() {
    }

    /* initialization of your toolbar with title ,color etc */

    /** this method is used to initialize the widgets and fields and toolbar*/
    private void initElementsWithIds() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }

    /** used to set the tab layout  */
    private void setupViewPager(ViewPager viewPager) {
        adapter = new DashboardPagerAdapter(getSupportFragmentManager());

        /** add more fragments if you want to**/
//      adapter.addFragment(new UsersFragment(), "Users");
        adapter.addFragment(new GroupFragment(), "Groups");
        adapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    /** set icons to your tabs*/
    private void setupTabIcons() {
       //set custom color to icons
        int tabIconColor = ContextCompat.getColor(context, R.color.colorPrimary);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(0)
                .getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(1)
                .getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //Creating custom menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_main2_drawer,menu);
        return true;
    }

    //Handling custom menu clicks
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if(id == R.id.log_out){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);

            Toast toast = Toast.makeText(this,"You have been logged out", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else
            if(id == R.id.help){
                Intent i = new Intent(context,WelcomeActivity.class);
                startActivity(i);
                finish();
        }
        else if (id == R.id.all_users){
            Intent intent = new Intent(context,allUsersActivity.class);
            startActivity(intent);
        }
        return true;
    }
}

