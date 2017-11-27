package example.com.groupeasy.activities;

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
import example.com.groupeasy.adapters.EventsAdapter;
import example.com.groupeasy.pojo.list_primary;

/**
 * This Class Displays a listview of all active events from that group
 * **/

public class activeEvents extends AppCompatActivity {

    //initilize varaibles
    private RecyclerView mRecyclerView;
    private EventsAdapter mAdapter;
    private List<list_primary> mLstGroups;
    private TextView emptyView;
    private String groupKey;

    /** Firebase db init*/
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference groupRef = myRef.child("Events").child("lists");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Link to XML layout*/
        setContentView(R.layout.activity_active_events);

        emptyView = (TextView) findViewById(R.id.empty_view_list);

        //Toolbar intialization
        Toolbar mToolbar = (Toolbar) findViewById(R.id.lists_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Active Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        groupKey = getIntent().getExtras().get("groupKey").toString();

        /** Attach Listener to get num of children in participants*/
        groupRef.child(groupKey).child("").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long num = dataSnapshot.getChildrenCount();
                /**Create Ui passing num of children in participants as parameter to loop through and display members */
                createListView(num);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /* Calling methods for
        1. Initialising elements by IDs
        2. Initialising elements with Listeners*/
        initElementsByListeners();
        initElementsByIds();

        //Initialize arraylist and attach adapter to dynamic recyclerview
        mLstGroups = new ArrayList<>();
        mAdapter = new EventsAdapter(mLstGroups,this,groupKey);
        mRecyclerView = (RecyclerView) findViewById(R.id.events_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activeEvents.this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initElementsByIds() {
    /*Initialize ui element by linking to xml ids*/
        emptyView = (TextView) findViewById(R.id.empty_view_list);
    }

    private void initElementsByListeners() {
    //all clickshandled by adapter
    }

    //Generate a listview of events
    private void createListView(final long num) {

        //offline functionality
        groupRef.keepSynced(true);
        groupKey = getIntent().getExtras().get("groupKey").toString();

        /** Getting Events */
        groupRef.child(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups.removeAll(mLstGroups);

                for(DataSnapshot snapshot : dataSnapshot.child("").getChildren()) {
                    //adds object from snapshot into object of list_primary pojo class
                    list_primary newList = snapshot.getValue(list_primary.class);
                    //adds elements to top of recyclerview 'mLstGroups'
                    mLstGroups.add(0,newList);

                    /**Failed ! code for trying to implement dynamic participant list**/
                    /*for (DataSnapshot ds : snapshot.child("participants").child("").getChildren()) {
                        members_In members = ds.getValue(members_In.class);
                        mLstGroups2.add(members);
                    }*/
                }
                mAdapter.notifyDataSetChanged();

                if(mLstGroups.isEmpty()){
                    emptyView.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                }
                else{
                    emptyView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        /*//Failed code
        *//** for loop for looping through the num of children param got through variable num*//*
        for(int i=0; i<num ;i++) {
            *//**Failed ! Getting Participants *//*
                String temp = String.valueOf((i-1));

           *//* groupRef.child(groupKey).child(temp).child("participants").child("").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mLstGroups2.removeAll(mLstGroups2);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        members_In members = snapshot.getValue(members_In.class);
                        mLstGroups2.add(members);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });*//*

                 *//* groupRef.child(groupKey).child(temp).child("participants").child("").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mLstGroups2.removeAll(mLstGroups2);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        members_In members = snapshot.getValue(members_In.class);
                        mLstGroups2.add(members);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }*/

    }

   /* private void getData(long num) {

        Log.w(String.valueOf(num),"iAmGettingDataHEre");
        Integer numOfChildren = Integer.parseInt(String.valueOf(num));
//        for(int i=0; i<numOfChildren ;i++) {
            *//**Failed ! Getting Participants *//*
            groupRef.child(groupKey).child("-Kx8T3J3ddUyfOivXc-x").child("participants").child("").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mLstGroups2.removeAll(mLstGroups2);

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        members_In members = snapshot.getValue(members_In.class);
                        mLstGroups2.add(members);
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
//        }
    }*/

    @Override
    public void onStart() {
        super.onStart();
    }

}