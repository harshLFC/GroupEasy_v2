package example.com.groupeasy.activities;

import android.app.usage.UsageEvents;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.adapters.EventDetailsAdapter;
import example.com.groupeasy.adapters.EventsAdapter;
import example.com.groupeasy.pojo.list_details;
import example.com.groupeasy.pojo.members_In;

public class EventDetailsActivity extends AppCompatActivity {

    //local ui elements
    private TextView EventName;
    private TextView EventLocation;
    private TextView EventAdmin;
    private TextView ParticipantsInThisEvent;
    private ImageButton ThumbUp;
    private ImageButton ThumbUpGreen;


    //dynamic data elements
    private EventDetailsAdapter mAdapter;
//    private List <list_details> mLstGroups;
    private List <members_In> mLstGroups2;
    private RecyclerView mRecyclerView;

    //firebase db elements
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference groupRef = myRef.child("Events").child("lists");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        setToolbar();
        initElementsById();
        initElementsByListeners();
        updateDisplay();

        mLstGroups2 = new ArrayList<>();
        mAdapter = new EventDetailsAdapter(mLstGroups2,this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(EventDetailsActivity.this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    private void initElementsByListeners() {

        /**WHen the user presses thumbs up button
         * 1. His user id will be taken
         * 2. node will be created under that specific events participant child with the userid
         * his name with "IN" will be pushed**/
        ThumbUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ThumbUp.setVisibility(View.INVISIBLE);
                ThumbUpGreen.setVisibility(View.VISIBLE);

                final String GroupKey = getIntent().getStringExtra("Groupkey");
                final String EventNum = getIntent().getStringExtra("eventNum");

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                myRef.child("members").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        groupRef.child(GroupKey).child(EventNum).child("participants").child(uid).child("name").setValue(dataSnapshot.getValue().toString());
                        groupRef.child(GroupKey).child(EventNum).child("participants").child(uid).child("value").setValue("In");


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    private void updateDisplay() {

        String name = getIntent().getStringExtra("name");
        EventName.setText(name);


        String location = getIntent().getStringExtra("location");
        if(!location.isEmpty())
            EventLocation.setText(location);


        String admin = getIntent().getStringExtra("admin");
        if(!location.isEmpty())
            EventAdmin.setText(admin);

        String GroupKey = getIntent().getStringExtra("Groupkey");
        String EventNum = getIntent().getStringExtra("eventNum");

//        Log.w(GroupKey,"ThisIsGroupKey");



//        groupRef.child(groupKey).child(temp).child("participants").child("").addValueEventListener(new ValueEventListener() {
        groupRef.child(GroupKey).child(EventNum).child("participants").child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLstGroups2.removeAll(mLstGroups2);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    members_In members = snapshot.getValue(members_In.class);
                    mLstGroups2.add(members);
                }
                mAdapter.notifyDataSetChanged();

                //if condition to check if there are no members responded to this event yet
                //if list is empty say no participants
                if(mLstGroups2.isEmpty())
                    ParticipantsInThisEvent.setText("No Participants in this event");
                    else
                    ParticipantsInThisEvent.setText("Participants in this event");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    private void initElementsById() {

        EventName  = (TextView) findViewById(R.id.event_name);
        EventLocation  = (TextView) findViewById(R.id.event_location);
        EventAdmin  = (TextView) findViewById(R.id.text_admin);

        mRecyclerView = (RecyclerView) findViewById(R.id.event_details_recycler);
        ParticipantsInThisEvent = (TextView) findViewById(R.id.participants_in_this_event);

        ThumbUp = (ImageButton) findViewById(R.id.thumb_up);
        ThumbUpGreen = (ImageButton) findViewById(R.id.thumb_up_green_green);


    }

    private void setToolbar() {
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow_left);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_event_details_tool);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
