package example.com.groupeasy.activities;

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
import example.com.groupeasy.adapters.ParticipantsAdapter;
import example.com.groupeasy.pojo.list_details;
import example.com.groupeasy.pojo.members_In;

/**
 * This Class Displays all the details of the event including participants
 * **/

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
    private TextView textDescription;

    private TextView minPart;
    private TextView maxPart;
//    private TextView textTime;
    private TextView textDateFrom;
    private TextView textTimeFrom;
    private TextView textDateTo;
    private TextView textTimeTo;

    private TextView numOfPart;


    //dynamic data elements
    private ParticipantsAdapter mAdapter;
//    private List <list_details> mLstGroups;
    private List <members_In> mLstGroups2;
    private List <list_details> mLstGroups;
    private RecyclerView mRecyclerView;

    //firebase db elements
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    final DatabaseReference groupRef = myRef.child("Events").child("lists");
    private View View5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Link to XML layout*/
        setContentView(R.layout.activity_event_details);

        /* Calling methods for
        1. Init Tolbar
        1. Initialising elements by IDs
        2. Initialising elements with Listeners
        3. updating screen display*/
        setToolbar();
        initElementsById();
        initElementsByListeners();
        updateDisplay();

        //Initialize 2 arraylists and attach adapter to dynamic recyclerview
        // 1. for Participants
        // 2. for string details for event
        mLstGroups2 = new ArrayList<>();
        mLstGroups = new ArrayList<>();
        mAdapter = new ParticipantsAdapter(mLstGroups2,this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(EventDetailsActivity.this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /*Handling all onclick events(listeners)*/
    private void initElementsByListeners() {

        /**WHen the user presses thumbs up button
         * 1. His user id will be taken
         * 2. node will be created under that specific events participant child with the userid
         * his name with "IN" will be pushed**/
        ThumbUp.setOnClickListener(new View.OnClickListener() {
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

    /*Send data according to user feedback (thumbs up/down/question mark) and retrive and update list*/
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

        final String startDay = splited[0] ;
        final String startMonth = splited[1] ;

        Log.w(startDay,"Split1");
        Log.w(startMonth,"Split2");

        if(startDay.equals("No")){
            textDay.setText(startMonth);
            textMonth.setText(startDay);
        }
        else {
            textDay.setText(startDay);
            textMonth.setText(startMonth);
        }

        //set event location
        String location = getIntent().getStringExtra("location");
        if(!location.isEmpty())
            EventLocation.setText(location);

        //set admin name
        String admin = getIntent().getStringExtra("admin");
        if(!admin.isEmpty())
            EventAdmin.setText(admin);

        final String GroupKey = getIntent().getStringExtra("Groupkey");
        final String EventNum = getIntent().getStringExtra("eventNum");

        /**Code to add participants into adapter**/
        groupRef.child(GroupKey).child(EventNum).child("participants").child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mLstGroups2.removeAll(mLstGroups2);
                mLstGroups.removeAll(mLstGroups);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    members_In members = snapshot.getValue(members_In.class);
                    mLstGroups2.add(members);
                }
                mAdapter.notifyDataSetChanged();

                /**Add listener to 'extra' child and display elements in ui**/
                groupRef.child(GroupKey).child(EventNum).child("extra").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list_details listDetails = dataSnapshot.getValue(list_details.class);
                        mLstGroups.add(listDetails);

                        //setDetails
                        String EventDetails = mLstGroups.get(0).getDetails();
                        if(EventDetails!=null)
                        textDescription.setText(EventDetails);

                        //set Min and Max variables
                        String MinPart = mLstGroups.get(0).getMin();
                        if(MinPart!=null)
                        minPart.setText(MinPart);
                        String MaxPart = mLstGroups.get(0).getMax();
                        if(MaxPart!=null)
                        maxPart.setText(MaxPart);

                        //setTime
                        String FromTime = mLstGroups.get(0).getFromTime();
                        String ToTime = mLstGroups.get(0).getToTime();
                        String ToDate = mLstGroups.get(0).getToDate();
//                        textTime.setText("From"+startDay+startMonth+"At"+FromTime+"Till"+ToDate+ToTime);

                        if(FromTime!=null){
                            textTimeFrom.setText(FromTime);
                            textTimeFrom.setVisibility(View.VISIBLE);
                        }
                        if(ToTime!=null) {
                            textTimeTo.setText(ToTime);
                            textTimeTo.setVisibility(View.VISIBLE);
                        }
                        if(startDay!=null) {
                            textDateFrom.setText(startDay +" "+ startMonth);
                            textDateFrom.setVisibility(View.VISIBLE);
                        }
                        if(ToDate!=null) {
                            trimDate(ToDate);
                        }
                    }

                    //Convert data from number to text format
                    private void trimDate(String toDate) {
                        String[] parts = toDate.split("/");
                        String part1 = parts[0]; // 004
                        String part2 = parts[1]; // 034556

                        int i = Integer.parseInt(part2);

                        String[] str = {"Jan",
                                "Feb",
                                "Mar",
                                "April",
                                "May",
                                "Jun",
                                "Jul",
                                "Aug",
                                "Sept",
                                "Oct",
                                "Nov",
                                "Dec","Dec"};

                        String monthString;

                        if( i <str.length){
                            monthString =  str[i-1];
                            textDateTo.setText(part1+" "+monthString);
                            textDateTo.setVisibility(View.VISIBLE);
                        }
                        else {
                            monthString = "invalid Month";
                            textDateTo.setText(part1+" "+monthString);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                /**check user 'value' under participants and display appropriate icon**/
                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = current_user.getUid();

                if(dataSnapshot.hasChild(uid)) {
                    String response;

                    if (dataSnapshot.child(uid).child("value").getValue() != null) {

                        response = dataSnapshot.child(uid).child("value").getValue().toString();

                        if (response.equals("In"))
                        {
                            QuestionMarkYellow.setVisibility(View.INVISIBLE);
                            ThumbDownRed.setVisibility(View.INVISIBLE);
                            QuestionMark.setVisibility(View.VISIBLE);
                            ThumbDown.setVisibility(View.VISIBLE);

                            ThumbUp.setVisibility(View.INVISIBLE);
                            ThumbUpGreen.setVisibility(View.VISIBLE);
                        } else if (response.equals("Out"))
                        {
                            QuestionMarkYellow.setVisibility(View.INVISIBLE);
                            ThumbUpGreen.setVisibility(View.INVISIBLE);
                            QuestionMark.setVisibility(View.VISIBLE);
                            ThumbUp.setVisibility(View.VISIBLE);

                            ThumbDown.setVisibility(View.INVISIBLE);
                            ThumbDownRed.setVisibility(View.VISIBLE);
                        } else if (response.equals("Maybe"))
                        {
                            ThumbDownRed.setVisibility(View.INVISIBLE);
                            ThumbDown.setVisibility(View.VISIBLE);
                            ThumbUpGreen.setVisibility(View.INVISIBLE);
                            ThumbUp.setVisibility(View.VISIBLE);

                            QuestionMark.setVisibility(View.INVISIBLE);
                            QuestionMarkYellow.setVisibility(View.VISIBLE);
                        }
                    }
                }

                //if condition to check if there are no members responded to this event yet
                //if list is empty say no participants
                if(mLstGroups2.isEmpty())
                    ParticipantsInThisEvent.setText("No Participants in this event");
                    else {
                    ParticipantsInThisEvent.setText("Participants in this event");
                    mRecyclerView.setVisibility(View.VISIBLE);
                    //view5 is the small line below participants
                    View5.setVisibility(View.GONE);
                    numOfPart.setVisibility(View.VISIBLE);

//                  displaying number of people who have participated
                    int count = mLstGroups2.size();
                    numOfPart.setText("Number of participants: "+count+" ");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**Initialize all ui elements by linking to xml ids*/
    private void initElementsById() {

        EventName  = (TextView) findViewById(R.id.event_name);
        EventLocation  = (TextView) findViewById(R.id.event_location);
        EventAdmin  = (TextView) findViewById(R.id.text_admin);

        textDay  = (TextView) findViewById(R.id.text_day);
        textMonth  = (TextView) findViewById(R.id.text_month);
        textDescription  = (TextView) findViewById(R.id.text_description);
        minPart  = (TextView) findViewById(R.id.min_parrticipant);
        maxPart  = (TextView) findViewById(R.id.max_participant);

        textTimeFrom  = (TextView) findViewById(R.id.from_time);
        textTimeTo  = (TextView) findViewById(R.id.to_time);
        textDateFrom  = (TextView) findViewById(R.id.from_date);
        textDateTo = (TextView) findViewById(R.id.to_date);

        mRecyclerView = (RecyclerView) findViewById(R.id.event_details_recycler);
        ParticipantsInThisEvent = (TextView) findViewById(R.id.participants_in_this_event);

        numOfPart = (TextView) findViewById(R.id.num_of_part);

        ThumbUp = (ImageButton) findViewById(R.id.thumb_up);
        ThumbUpGreen = (ImageButton) findViewById(R.id.thumb_up_green_green);
        ThumbDown= (ImageButton) findViewById(R.id.thumb_down);
        ThumbDownRed= (ImageButton) findViewById(R.id.thumb_down_red);
        QuestionMark= (ImageButton) findViewById(R.id.question_mark);
        QuestionMarkYellow= (ImageButton) findViewById(R.id.question_mark_yellow);

        View5 = findViewById(R.id.view5);
    }

    //Customise toolbar
    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_event_details_tool);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
