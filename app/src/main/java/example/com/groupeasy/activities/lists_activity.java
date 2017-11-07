package example.com.groupeasy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.EventsAdapter;
import example.com.groupeasy.pojo.list_primary;
import example.com.groupeasy.pojo.members_In;

public class lists_activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EventsAdapter mAdapter;
//    private EventsAdapter mDataAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<list_primary> mLstGroups;
    private List<members_In> mLstGroups2;
    private TextView emptyView;
    private String groupKey;
//    HashMap<String,String> myMap = new HashMap<>();
//    HashMap<String,String> myMapNext = new HashMap<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference groupRef = myRef.child("Events").child("lists");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

//        View rootView = inflater.inflate(R.layout.activity_lists,container,false);

        emptyView = (TextView) findViewById(R.id.empty_view_list);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.lists_app_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Active Events");
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
//                Log.w(String.valueOf(num),"amIGettingNUm");
//                getData(num);

                /**Create Ui passing num of children in participants as parameter to loop through and display members */
                createListView(num);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        long num = 0;
//        createListView(num);
        initElementsByListeners();
        initElementsByIds();

        mLstGroups = new ArrayList<>();
//        mLstGroups2 = new ArrayList<>();

        mAdapter = new EventsAdapter(mLstGroups,this,groupKey);

//        mAdapter = new EventsAdapter(mLstGroups,this,mLstGroups2);

        mRecyclerView = (RecyclerView) findViewById(R.id.events_recycler_view);
//        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(lists_activity.this));

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void initElementsByIds() {

        emptyView = (TextView) findViewById(R.id.empty_view_list);
    }

    private void initElementsByListeners() {

    }

    private void createListView(final long num) {

//        final DatabaseReference groupRef = myRef.child("Events").child("lists");
        groupRef.keepSynced(true);
        groupKey = getIntent().getExtras().get("groupKey").toString();


        /** for loop for looping through the num of children param got through variable num*/
        for(int i=0; i<num ;i++) {
            /**Getting Participants */
                String temp = String.valueOf((i-1));

           /* groupRef.child(groupKey).child(temp).child("participants").child("").addChildEventListener(new ChildEventListener() {
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
            });*/

                 /* groupRef.child(groupKey).child(temp).child("participants").child("").addValueEventListener(new ValueEventListener() {
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
            });*/
        }

        /** Getting Events */
        groupRef.child(groupKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups.removeAll(mLstGroups);

                for(DataSnapshot snapshot : dataSnapshot.child("").getChildren()) {
                    list_primary newList = snapshot.getValue(list_primary.class);
                    mLstGroups.add(newList);

                    /*for (DataSnapshot ds : snapshot.child("participants").child("").getChildren()) {

                        members_In members = ds.getValue(members_In.class);
                        mLstGroups2.add(members);

                    }*/
                }
//                mAdapter.notifyDataSetChanged();

//
//                for(DataSnapshot snapshot : dataSnapshot.child("").child("participants").child("").getChildren()){
//                    members_In members = snapshot.getValue(members_In.class);
//                    mLstGroups2.add(members);
//
//                    mLstGroups2.add(members);
//
//                }

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
    }

   /* private void getData(long num) {

        Log.w(String.valueOf(num),"iAmGettingDataHEre");
        Integer numOfChildren = Integer.parseInt(String.valueOf(num));
//        Integer temp =


//        for(int i=0; i<numOfChildren ;i++) {
            *//**Getting Participants *//*
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