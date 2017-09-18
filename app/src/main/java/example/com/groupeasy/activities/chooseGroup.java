package example.com.groupeasy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.GroupAdapter;
import example.com.groupeasy.adapters.GroupSelectAdapter;
import example.com.groupeasy.pojo.new_groups;

public class chooseGroup extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private GroupSelectAdapter mGroupAdapter;
    private List<new_groups> mLstGroups;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference groupRef = myRef.child("groups").child("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);

        mToolbar = (Toolbar) findViewById(R.id.choose_groups_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Choose Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initElementsWithIds();
        createListView();
        initElementsWithListeners();

        mLstGroups = new ArrayList<>();
        // initialize adapter to our List of <group>
        mGroupAdapter = new GroupSelectAdapter(mLstGroups);

        //Initialize the Recycler view and set an adapter to populate the JSON data
        mRecyclerView = (RecyclerView) findViewById(R.id.choose_group_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // a RecyclerView needs a LayoutManager to manage the positioning of its items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //a RecyclerView needs an adapter to access its data
        mRecyclerView.setAdapter(mGroupAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void initElementsWithListeners() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_assign) {

            Toast.makeText(this, "Clicked on assign",Toast.LENGTH_SHORT).show();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void createListView() {
        groupRef.keepSynced(true);

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups.removeAll(mLstGroups);

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    new_groups newGroups = snapshot.getValue(new_groups.class);
                    mLstGroups.add(newGroups);
                }
//                    new_groups newGroups  = dataSnapshot.getValue(new_groups.class);
//                    mLstGroups.add(newGroups);

                mGroupAdapter.notifyDataSetChanged();

                //COde for displaying something when RecyclerView data is empty

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void initElementsWithIds() {

    }
}
