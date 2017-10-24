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
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.EventsAdapter;
import example.com.groupeasy.pojo.list_primary;

public class lists_activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EventsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<list_primary> mLstGroups;
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
        mAdapter = new EventsAdapter(mLstGroups);

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

//        groupRef.child(groupKey).child("").addValueEventListener(new ValueEventListener() {
        groupRef.child("-Kx8REumOYknDMHjHEsq").child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups.removeAll(mLstGroups);

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    list_primary newList = snapshot.getValue(list_primary.class);
                    mLstGroups.add(newList);

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
