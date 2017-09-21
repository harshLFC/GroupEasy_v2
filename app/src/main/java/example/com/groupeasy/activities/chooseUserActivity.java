package example.com.groupeasy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import example.com.groupeasy.R;
import example.com.groupeasy.adapters.UserAdapter;
import example.com.groupeasy.adapters.UsersSelectAdapter;
import example.com.groupeasy.pojo.list_details;
import example.com.groupeasy.pojo.list_primary;
import example.com.groupeasy.pojo.users_list;

public class chooseUserActivity extends AppCompatActivity {

    RecyclerView mUserRecyclerView;
    Toolbar mToolbar;
    //adapter
    //list
    private UsersSelectAdapter mUserAdapter;
    private List<users_list> mLstGroups;

    private Context context;
    //firebase

    private TextView emptyView;

    /** Firebase db init*/
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference userRef = myRef.child("members").child("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        ButterKnife.bind(this);

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

        initElementsWithIds();
        createListView();
        initElementsWithListeners();


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



    private void initElementsWithListeners()
    {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_users_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_choose_users_done) {

            Toast.makeText(this, "Clicked! ",Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }

    private void initElementsWithIds() {

    emptyView = (TextView) findViewById(R.id.empty_view_users);

    }

    private void createListView() {

        userRef.keepSynced(true);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups.removeAll(mLstGroups);

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    users_list usersList = snapshot.getValue(users_list.class);
                    mLstGroups.add(usersList);
                }

                mUserAdapter.notifyDataSetChanged();

//                if(mLstGroups.isEmpty()){
//                    emptyView.setVisibility(View.VISIBLE);
//                    mUserRecyclerView.setVisibility(View.GONE);
//                }
//                else{
//
//                    emptyView.setVisibility(View.GONE);
//                    mUserRecyclerView.setVisibility(View.VISIBLE);
//
//                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
    }
//
//    public void RecyclerListViewClicked(View v, int position){
//
//
//
//
//    }

}
