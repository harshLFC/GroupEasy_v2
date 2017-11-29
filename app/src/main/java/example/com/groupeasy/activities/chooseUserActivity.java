package example.com.groupeasy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.UsersSelectAdapter;
import example.com.groupeasy.pojo.new_groups;
import example.com.groupeasy.pojo.users_list;

/**
 * This Class opens up a list of all users of all for adding into a group
 * Was one of the most complex pages of this app as it not only involved communicating with adaptet,
 * but also transferring checkbox values of the mebers we wised to add on that exact row.
 * **/

public class chooseUserActivity extends AppCompatActivity {

    final DatabaseReference userRef = myRef.child("members");
    final DatabaseReference groupRef = myRef.child("groups").child("");
    //initilize varaibles
    RecyclerView mUserRecyclerView;
    Toolbar mToolbar;
    String imagePic = "";
    String imageThumb = "";
    String group_id = "";
    /**
     * Firebase db init
     */
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    private TextView emptyView;
    //adapter
    private UsersSelectAdapter mUserAdapter;
    //list
    private List<users_list> mLstGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Link to XML layout*/
        setContentView(R.layout.activity_choose_user);

        //Toolbar intialization
        mToolbar = (Toolbar) findViewById(R.id.choose_users_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* Calling methods for
        1. Initialising elements by IDs
        2. Populating users data into a listview
        3. Initialising elements with Listeners*/
        initElementsWithIds();
        createListView();
        initElementsWithListeners();

        //Initialize arraylist and attach adapter to dynamic recyclerview
        mLstGroups = new ArrayList<>();
        // initialize adapter to our List of <group>
        mUserAdapter = new UsersSelectAdapter(mLstGroups);
        mUserRecyclerView = (RecyclerView) findViewById(R.id.choose_users_recyclerview);
        mUserRecyclerView.setHasFixedSize(true);
        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //a RecyclerView needs an adapter to access its data
        mUserRecyclerView.setAdapter(mUserAdapter);
        mUserRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initElementsWithListeners() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final String groupName = getIntent().getStringExtra("groupName");
        imagePic = getIntent().getStringExtra("imagePic");
        imageThumb = getIntent().getStringExtra("imageThumb");
        group_id = getIntent().getStringExtra("group_id");

        //code for on 'done' button click
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_choose_users_done) {

            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = current_user.getUid();
            List<users_list> uList = mUserAdapter
                    .getUserId();
            final HashMap<String, Boolean> myMap = new HashMap<>();

            //Loop to put groupsIn the members list and set value to true
            for (int i = 0; i < uList.size(); i++) {
                users_list singleUser = uList.get(i);
                if (singleUser.isSelected()) {

                    myMap.put(singleUser.getId(), true);
                    //Update Users DB with groupsIn TRUE value
                    userRef.child(singleUser.getId()).child("groupsIn").child(group_id).setValue(true);
                }
            }

//            Update Members DB with groups TRUE value
//            this code is not working here, but is working inside the group because groups is not created before
            /**THe user crating group is added by default**/
            myMap.put(uid,true);

            //set Value of members in group to all in myMap
            groupRef.child(group_id).child("members").child("").setValue(myMap);
            userRef.child(uid).child("groupsIn").child(group_id).setValue(true);
            //initiate a msgRef for pushing sample data
            final DatabaseReference msgRef = myRef.child("messages").child("");

            //code to push create group data to firebase
            myRef.child("members").child(uid).child("id").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final String admin = dataSnapshot.getValue().toString();
                    final String last_msg = "You have no messages in the group";

                    //if the user does not get an intent with an image in it then it will go as an empty string
                    if (imagePic == null) imagePic = "";
                    if (imageThumb == null) imageThumb = "";

                    new_groups newGroups = new new_groups(admin, imagePic, last_msg, groupName, group_id, imageThumb);

                    //Update Group DB with details
                    groupRef.child(group_id).setValue(newGroups);

                    //Update Msg DB with Dummy value to initialize
                    msgRef.child(group_id).setValue(true);

                    //Update Members DB with groups TRUE value
                    groupRef.child(group_id).child("members").child("").setValue(myMap);

                    Toast.makeText(chooseUserActivity.this, "Group has been created ", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            //end code push

        }
        return super.onOptionsItemSelected(item);
    }

    /**Send data as query to the search functionality in adapter**/

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mUserAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void initElementsWithIds() {
        emptyView = (TextView) findViewById(R.id.empty_view_users);
    }

    private void createListView() {

        //THis code loops through all members in the app and shows them in list
        //what i want to do is 1. check if user is in group, if he is keep his checkbox ticked

        userRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups.removeAll(mLstGroups);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //if condition for the user is already in group ( if decided to implement the fab for add memebers in group

                    users_list usersList = snapshot.getValue(users_list.class);
                    mLstGroups.add(usersList);

                }

                //this is how the adapter gets its db reference
                mUserAdapter.notifyDataSetChanged();

//              code for showing something if there are no users for the app, which is highly unlikely
//                if(mLstGroups.isEmpty()){
//                    emptyView.setVisibility(View.VISIBLE);
//                    mUserRecyclerView.setVisibility(View.GONE);
//                }
//                else{
//                    emptyView.setVisibility(View.GONE);
//                    mUserRecyclerView.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
