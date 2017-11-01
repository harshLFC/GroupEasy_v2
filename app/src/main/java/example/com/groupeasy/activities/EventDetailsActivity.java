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
    private ImageButton ThumbDown;
    private ImageButton ThumbDownRed;
    private ImageButton QuestionMark;
    private ImageButton QuestionMarkYellow;
    private TextView textMonth;
    private TextView textDay;


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

//                ThumbUp.setVisibility(View.INVISIBLE);
//                ThumbUpGreen.setVisibility(View.VISIBLE);

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



        QuestionMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                QuestionMark.setVisibility(View.INVISIBLE);
//                QuestionMarkYellow.setVisibility(View.VISIBLE);

                final String GroupKey = getIntent().getStringExtra("Groupkey");
                final String EventNum = getIntent().getStringExtra("eventNum");

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                myRef.child("members").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        groupRef.child(GroupKey).child(EventNum).child("participants").child(uid).child("name").setValue(dataSnapshot.getValue().toString());
                        groupRef.child(GroupKey).child(EventNum).child("participants").child(uid).child("value").setValue("Maybe");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        ThumbDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String GroupKey = getIntent().getStringExtra("Groupkey");
                final String EventNum = getIntent().getStringExtra("eventNum");

                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                myRef.child("members").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        groupRef.child(GroupKey).child(EventNum).child("participants").child(uid).child("name").setValue(dataSnapshot.getValue().toString());
                        groupRef.child(GroupKey).child(EventNum).child("participants").child(uid).child("value").setValue("Out");
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });


    }

    private void updateDisplay() {

        /** Initialize default icon colors ***/
        ThumbUp.setVisibility(View.VISIBLE);
        ThumbUpGreen.setVisibility(View.INVISIBLE);
        ThumbDown.setVisibility(View.VISIBLE);
        ThumbDownRed.setVisibility(View.INVISIBLE);
        QuestionMark.setVisibility(View.VISIBLE);
        QuestionMarkYellow.setVisibility(View.INVISIBLE);

        //Set event name
        String name = getIntent().getStringExtra("name");
        EventName.setText(name);

        //set event date
        String date = getIntent().getStringExtra("eventDate");

        //\\s+ is regex for blank space
        String[] splited = date.split("\\s+");

        String a = splited[0] ;
        String b = splited[1] ;

        Log.w(a,"Split1");
        Log.w(b,"Split2");

        if(a.equals("No")){
            textDay.setText(b);
            textMonth.setText(a);
        }
        else {
            textDay.setText(a);
            textMonth.setText(b);
        }

        //set event location
        String location = getIntent().getStringExtra("location");
        if(!location.isEmpty())
            EventLocation.setText(location);


        String admin = getIntent().getStringExtra("admin");
        if(!location.isEmpty())
            EventAdmin.setText(admin);

        String GroupKey = getIntent().getStringExtra("Groupkey");
        String EventNum = getIntent().getStringExtra("eventNum");

//        Log.w(GroupKey,"ThisIsGroupKey");


/**Code to add participants into adapter**/
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

                /**check user 'value' under participants and display appropriate icon**/
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                if(dataSnapshot.hasChild(uid)) {
//                    Toast.makeText(EventDetailsActivity.this, "User has child here", Toast.LENGTH_SHORT).show();
                    String test;

//                    test = dataSnapshot.child(uid).child("value").getValue().toString();

                    if (dataSnapshot.child(uid).child("value").getValue() != null) {

                        test = dataSnapshot.child(uid).child("value").getValue().toString();


                        if (test.equals("In")) {
                            QuestionMarkYellow.setVisibility(View.INVISIBLE);
                            ThumbDownRed.setVisibility(View.INVISIBLE);
                            QuestionMark.setVisibility(View.VISIBLE);
                            ThumbDown.setVisibility(View.VISIBLE);

                            ThumbUp.setVisibility(View.INVISIBLE);
                            ThumbUpGreen.setVisibility(View.VISIBLE);
                        } else if (test.equals("Out")) {
                            QuestionMarkYellow.setVisibility(View.INVISIBLE);
                            ThumbUpGreen.setVisibility(View.INVISIBLE);
                            QuestionMark.setVisibility(View.VISIBLE);
                            ThumbUp.setVisibility(View.VISIBLE);

                            ThumbDown.setVisibility(View.INVISIBLE);
                            ThumbDownRed.setVisibility(View.VISIBLE);
                        } else if (test.equals("Maybe")) {
                            ThumbDownRed.setVisibility(View.INVISIBLE);
                            ThumbDown.setVisibility(View.VISIBLE);
                            ThumbUpGreen.setVisibility(View.INVISIBLE);
                            ThumbUp.setVisibility(View.VISIBLE);

                            QuestionMark.setVisibility(View.INVISIBLE);
                            QuestionMarkYellow.setVisibility(View.VISIBLE);
                        }


                    } else {
                        Toast.makeText(EventDetailsActivity.this, "User DOES NOT has child here", Toast.LENGTH_SHORT).show();


                    }
                }

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

        textDay  = (TextView) findViewById(R.id.text_day);
        textMonth  = (TextView) findViewById(R.id.text_month);

        mRecyclerView = (RecyclerView) findViewById(R.id.event_details_recycler);
        ParticipantsInThisEvent = (TextView) findViewById(R.id.participants_in_this_event);

        ThumbUp = (ImageButton) findViewById(R.id.thumb_up);
        ThumbUpGreen = (ImageButton) findViewById(R.id.thumb_up_green_green);
        ThumbDown= (ImageButton) findViewById(R.id.thumb_down);
        ThumbDownRed= (ImageButton) findViewById(R.id.thumb_down_red);
        QuestionMark= (ImageButton) findViewById(R.id.question_mark);
        QuestionMarkYellow= (ImageButton) findViewById(R.id.question_mark_yellow);


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
