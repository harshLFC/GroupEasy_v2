package example.com.groupeasy.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.UserAdapter;
import example.com.groupeasy.pojo.users_list;

public class allUsersActivity extends AppCompatActivity {


    /** Ui elements init */
    private RecyclerView mUserRecyclerView;
    private UserAdapter mUserAdapter;
    private List<users_list> mLstGroups;
    private TextView emptyView;
    private Toolbar mToolBar;

    /** Firebase db init*/
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference userRef = myRef.child("members").child("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolBar = (Toolbar) findViewById(R.id.all_users_app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        initElementsWithIds();
        createListView();
        initElementsWithListeners();


        mLstGroups = new ArrayList<>();
        // initialize adapter to our List of <group>
        mUserAdapter = new UserAdapter(mLstGroups);

        mUserRecyclerView = (RecyclerView) findViewById(R.id.users_list);
        mUserRecyclerView.setHasFixedSize(true);

        mUserRecyclerView.setLayoutManager(new LinearLayoutManager(allUsersActivity.this));

        //a RecyclerView needs an adapter to access its data
        mUserRecyclerView.setAdapter(mUserAdapter);
        mUserRecyclerView.setItemAnimator(new DefaultItemAnimator());

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

                if(mLstGroups.isEmpty()){
                    emptyView.setVisibility(View.VISIBLE);
                    mUserRecyclerView.setVisibility(View.GONE);
                }
                else{

                    emptyView.setVisibility(View.GONE);
                    mUserRecyclerView.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initElementsWithListeners() {

    }
}
