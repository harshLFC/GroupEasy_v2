package example.com.groupeasy.activities;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.DashboardPagerAdapter;
import example.com.groupeasy.fragments.GroupFragment;
import example.com.groupeasy.fragments.ProfileFragment;
import example.com.groupeasy.fragments.SearchFragment;
import example.com.groupeasy.utility.AppConstants;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private Toolbar toolBar;
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DashboardPagerAdapter adapter;
    private Toolbar myTool;

    /** set icons to your tabs*/
    private int[] tabIcons = {
            R.drawable.eye_white_48,
            R.drawable.ic_user_group,
            R.drawable.user_profile
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.context = DashboardActivity.this;
        initElementsWithIds();
        initToolbar();
        setupViewPager(viewPager);
        setupTabIcons();
        initElementsWithListeners();


    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void initElementsWithListeners() {

    }

    /* initialization of your toolbar with title ,color etc */
    private void initToolbar() {
        myTool.setTitle(AppConstants.DASHBOARD_ACTIVITY_TITLE);
        myTool.setTitleTextColor(ContextCompat.getColor(context,R.color.white));
        setSupportActionBar(myTool);
    }

    /** this method is used to initialize the widgets and fields and toolbar*/
    private void initElementsWithIds() {
        myTool = (Toolbar) findViewById(R.id.mToolBar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    }

    /** used to set the tab layout  */
    private void setupViewPager(ViewPager viewPager) {
        adapter = new DashboardPagerAdapter(getSupportFragmentManager());

        /** add more fragments if you want to*/
        adapter.addFragment(new SearchFragment(), "Search");
        adapter.addFragment(new GroupFragment(), "Groups");
        adapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    /** set icons to your tabs*/
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.activity_main2_drawer,menu);

    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if(id == R.id.settings){
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.help){
            Toast.makeText(this, "Log ot clicked", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}

