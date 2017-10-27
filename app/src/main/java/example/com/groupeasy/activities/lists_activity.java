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
    HashMap<String,String> myMap = new HashMap<>();
    HashMap<String,String> myMapNext = new HashMap<>();
    private TextView emptyView;
    private String groupKey;

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

//        initElementsByIds(rootView);
        createListView();
        initElementsByListeners();

        mLstGroups = new ArrayList<>();
        mLstGroups2 = new ArrayList<>();
        groupKey = getIntent().getExtras().get("groupKey").toString();

//        mAdapter = new EventsAdapter(mLstGroups,this,myMap);
        mAdapter = new EventsAdapter(mLstGroups,this,mLstGroups2);
//        mDataAdapter = new EventsAdapter(groupKey);

        mRecyclerView = (RecyclerView) findViewById(R.id.events_recycler_view);
//        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(lists_activity.this));

//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//        return rootView;

    }

    private void initElementsByIds(View view) {

        emptyView = (TextView) view.findViewById(R.id.empty_view_list);
    }

    private void initElementsByListeners() {

    }

    private void createListView() {

        groupRef.keepSynced(true);
        groupKey = getIntent().getExtras().get("groupKey").toString();

        groupRef.child("-Kx8REumOYknDMHjHEsq").child("-Kx8T3J3ddUyfOivXc-x").child("participants").child("").addValueEventListener(new ValueEventListener() {
//        groupRef.child("-Kx8REumOYknDMHjHEsq").child("").child("participants").child("")
//                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mLstGroups2.removeAll(mLstGroups2);

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                            String wdff = snapshot.getValue().toString();
                            Log.w(wdff,"wdff");
//                            members_In members = dataSnapshot.getValue(members_In.class);
                            members_In members = snapshot.getValue(members_In.class);
                            String name = snapshot.getKey();
                            String value = snapshot.child("").getValue().toString();
                            Log.w(name,"thisIsWorking");
                            Log.w(value,"thisIsWorkingToo?");
                            mLstGroups2.add(members);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

//        groupRef.child(groupKey).child("").addValueEventListener(new ValueEventListener() {
        groupRef.child("-Kx8REumOYknDMHjHEsq").child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups.removeAll(mLstGroups);
//                mLstGroups2.removeAll(mLstGroups2);

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    list_primary newList = snapshot.getValue(list_primary.class);
                    Log.w(newList.getName(),"thisIsWorking");

//                    for(int i=0;i<(snapshot.child("participants").getChildrenCount())-1;i++){
                        long count = snapshot.child("participants").getChildrenCount();

//                   member_In members = snapshot.child("participants").child("").getValue(members_In.class);
//                    Log.w(members.getName(),"thisIsWorkingToo?");
//                    Log.w(members.getValue(),"thisIsWorkingToo?");
//        }
//                    String in = newList.getIn("in");
//                    HashMap<String,Object> mymap = (HashMap<String, Object>) newList.getIn();
                    String allIn = snapshot.child("participants").child("").getValue().toString();
//                    members_In members = (snapshot.child("participants").child("")).getValue(members_In.class);

                    Log.w(allIn,"Thesshouldgivemeallin");
                    myMap.put(snapshot.child("participants").child("").getKey(),snapshot.child("participants").child("").getValue().toString());

                    //add participants
                    mLstGroups.add(newList);
//                    mLstGroups2.add(members);
                }

                //code to iterate through map
                for(Map.Entry<String,String> entry : myMap.entrySet()){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    Log.w(key,"keyIz");
//                    Log.w(value,"valueIz");
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
    }

    @Override
    public void onStart() {
        super.onStart();
    }


}
