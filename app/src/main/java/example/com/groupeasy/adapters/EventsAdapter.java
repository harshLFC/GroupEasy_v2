package example.com.groupeasy.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.EventDetailsActivity;
import example.com.groupeasy.activities.lists_activity;
import example.com.groupeasy.pojo.list_primary;
import example.com.groupeasy.pojo.members_In;

/**
 * Created by Harsh on 11-09-2017.
 */

public class  EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static List<list_primary> mList1;
//    HashMap mList2;
    static List<members_In> mList2;
    Context mContext;
    LayoutInflater mInflater;
    View view;
    String GroupKey;
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    final DatabaseReference groupRef = database.getReference().child("Events").child("lists");

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static final String VIEW_TYPE_OBJECT_VIEW_TRY = "THis list is empty";

   public EventsAdapter(){

   }

    public EventsAdapter(List<list_primary> mLstGroups, Context context, String Key)
//    public EventsAdapter(List<list_primary> mLstGroups,Context context,HashMap<String, String> myMap)
    {
        this.mList1 = mLstGroups;
        this.mContext = context;
        this.GroupKey = Key;

//        this.mList2 = mLstGroups2;
//        this.mList2 = myMap;
    }

    public View getView(int position, View convertView, ViewGroup parent) {


    return view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view, parent, false);

        switch(viewType) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                Toast.makeText(mContext, "Empty",Toast.LENGTH_LONG).show();
                break;
            case VIEW_TYPE_OBJECT_VIEW:

                return new EventViewHolder(rootView);
        }

        return new EventViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final EventViewHolder viewHolder = (EventViewHolder) holder;

        /**Set event details to card**/
        viewHolder.eventName.setText(mList1.get(position).getName());
        String admin = mList1.get(position).getAdmin();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();
        DatabaseReference userImageRef = FirebaseDatabase.getInstance().getReference().child("members").child(admin);

        userImageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("name"))
                viewHolder.admin.setText(dataSnapshot.child("name").getValue().toString());
//                else
//                    viewHolder.admin.setText(mList1.get(position).getAdmin());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//not used
        String event_id = mList1.get(position).getId();

        //the following 2 hidden views for eventDetails screen
        viewHolder.eventID.setText(GroupKey);
        viewHolder.EventNum.setText(mList1.get(position).getId());

        String myDate = mList1.get(position).getFromDate();

        if(myDate!= null) {
//            viewHolder.EventDate.setText(myDate);

            /**Get string date back from server
             * 1. Split string into day and month
             * 2. Check month with array and display accordingly**/
            String[] parts = myDate.split("/");
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
                viewHolder.EventDate.setText(part1+" "+monthString);
            }
            else {
                monthString = "invalid Month";
            viewHolder.EventDate.setText(part1+monthString);
            }
        }

       //tests
        /* String test = mList2.get(position).getName();
        String test2 = mList1.get(position).getId();

        Log.d(test,"lala");
        Log.d(test2,"yaya");*/

        //if no location has been provided dont show
    try{
        if((mList1.get(position).getLocation()).isEmpty()) {
            viewHolder.locationText.setVisibility(View.GONE);
            viewHolder.locationImage.setVisibility(View.GONE);
               }
               else
            viewHolder.locationText.setText(mList1.get(position).getLocation());

    }
    catch (NullPointerException e){
        e.printStackTrace();
        }

//        viewHolder.eventID.setText(mList1.get(position).getId());

       /* DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child("lists");
        final String key = eventRef.getKey();

        String uid = eventRef.getKey();
        Log.i("uid", uid);*/

        //what does this do?
//        eventRef.addListenerForSingleValueEvent(eventListener);

        //set user image
        viewHolder.userImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = dataSnapshot.child("image").getValue().toString();
                Picasso.with(mContext)
                        .load(image)
                        .placeholder(R.drawable.ic_default_groups)
                        .resize(50,50)
                        .into(((EventViewHolder) holder).userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        if(!location.isEmpty()){
//        viewHolder.locationText.setVisibility(View.VISIBLE);
//        viewHolder.locationText.setText(mList2.get(position).getLocation());
//            viewHolder.locationImage.setVisibility(View.VISIBLE);
//        }

//////


        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LayoutInflater.from(mContext).inflate(R.layout.row_view_for_members_events,null,false);
//        view = mInflater.inflate(R.layout.row_view_for_members_events, null, false);

        //for loop int =0; i<list2.size; i++
//            textview tv = new textview

      /** Tried a lot to display list of members and almost got it working
       * but android + firebase is proving incapabale of display nested dynamic view
       * spent 2 weeks trying to get this to work in the end am putting it on hold
       * as a result of having constant bugs for displaying data which is repeating
       * and not abiding to the respective event **/
       /* for(int i=0;i<mList2.size();i++){

            int j = mList2.size();
            //code to add simple textView dynamically
            *//*TextView textView = new TextView(mContext);
            TextView textView2 = new TextView(mContext);
            textView.setText(mList2.get(i).getName());
            textView2.setText(mList2.get(i).getValue());

            viewHolder.linearView.addView(textView);
            viewHolder.linearView.addView(textView2);*//*

//            View view = new View(mContext);
            View view = mInflater.inflate(R.layout.row_view_for_members_events, null);

            TextView viewText = (TextView) view.findViewById(R.id.user_name);
            TextView viewResponse = (TextView) view.findViewById(R.id.user_response);

            viewText.setText(mList2.get(position).getName());
            viewResponse.setText(mList2.get(position).getValue());

            viewHolder.linearView.addView(view);

//            LayoutInflater.from(mContext).inflate(R.layout.row_view_for_members_events,null,false);


        }*/

        // add data by setText
//        liearlayout.add(child)

//        view = mInflater.inflate(R.layout.row_view_for_members_events, null);





        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = current_user.getUid();
        final String Groupkey = viewHolder.eventID.getText().toString();
        final String eventNum = viewHolder.EventNum.getText().toString();

        /**Check events -> participants get value of 'value'
         * 1. set text to ✓ if value is 'In'
         * 2. set Text to ✘ if value is 'Out'
         * 3. set Text to ? if value is 'Maybe'**/
        myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //first check if child exists
                if (dataSnapshot.hasChild("value")) {

                    if (dataSnapshot.child("value").getValue().equals("In")) {
                        viewHolder.addMe.setText("✓ You are In for this event");
                    }
                    if (dataSnapshot.child("value").getValue().equals("Out")) {
                        viewHolder.addMe.setText("✘ You are Out for this event");
                    }
                    if (dataSnapshot.child("value").getValue().equals("Maybe")) {
                        viewHolder.addMe.setText("? You are unsure for this event");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public int getItemCount() {
        return mList1.size();
//        +mList2.size();

    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView eventName;
        public TextView eventID;
        private TextView admin;
        private TextView addMe;
        private TextView locationText;
        private ImageView locationImage;
        private ImageView userImage;
        private ImageView fullEvent;
        private LinearLayout linearView;
        private CardView myCard;
        private ConstraintLayout myCOnstrained;

        private ImageView myHeart;
        private TextView DetailsText;
        private TextView EventNum;
        private TextView EventDate;

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        DatabaseReference userImageRef = FirebaseDatabase.getInstance().getReference().child("members").child(uid);
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child("lists");


        public EventViewHolder(View itemView) {
            super(itemView);

            eventName = (TextView) itemView.findViewById(R.id.event_name);
            admin = (TextView) itemView.findViewById(R.id.author_of_event);
            locationText = (TextView) itemView.findViewById(R.id.location_text);
            locationImage = (ImageView) itemView.findViewById(R.id.location_image);
            userImage = (ImageView) itemView.findViewById(R.id.user_dp);
            addMe = (TextView) itemView.findViewById(R.id.add_Me);
            eventID = (TextView) itemView.findViewById(R.id.event_key);
            linearView = (LinearLayout) itemView.findViewById(R.id.list_view_event);
//            fullEvent = (ImageView) itemView.findViewById(R.id.full_event);
            myCard = (CardView) itemView.findViewById(R.id.my_card);
            myCOnstrained = (ConstraintLayout) itemView.findViewById(R.id.my_constrained);

            DetailsText = (TextView) itemView.findViewById(R.id.details_txt);
            myHeart = (ImageView) itemView.findViewById(R.id.heart);
            EventNum = (TextView) itemView.findViewById(R.id.eventNum);
            EventDate = (TextView) itemView.findViewById(R.id.event_date);


            eventName.setOnClickListener(this);
            admin.setOnClickListener(this);
            addMe.setOnClickListener(this);

            DetailsText.setOnClickListener(this);
            myHeart.setOnClickListener(this);
//            fullEvent.setOnClickListener(this);


        }

        @Override
        public void onClick(final View v) {

           if (v.getId() == admin.getId()) {
                Toast.makeText(v.getContext(), "Admin", Toast.LENGTH_SHORT).show();
//            clickListener.onItemClick(getAdapterPosition(),v);
            }

            else if (v.getId() == myHeart.getId()) {


                Toast.makeText(v.getContext(), "Event Favourited !", Toast.LENGTH_SHORT).show();
                myHeart.setColorFilter(Color.RED);

//            clickListener.onItemClick(getAdapterPosition(),v);
            }

            else if (v.getId() == DetailsText.getId() || v.getId() == eventName.getId()){

                Intent intent= new Intent(v.getContext(), EventDetailsActivity.class);
                String name = eventName.getText().toString();
                intent.putExtra("name",name);

                String location = locationText.getText().toString();
                intent.putExtra("location",location);

                String ad_min = admin.getText().toString();
                intent.putExtra("admin",ad_min);

                String Groupkey = eventID.getText().toString();
                intent.putExtra("Groupkey",Groupkey);

                String eventNum = EventNum.getText().toString();
                intent.putExtra("eventNum",eventNum);

               String eventDate = EventDate.getText().toString();
                intent.putExtra("eventDate",eventDate);


                v.getContext().startActivity(intent);


            }

            else if (v.getId() == addMe.getId()){

                final String key = eventRef.getKey();
                Log.w("type_this",key);

                String test = eventRef.child(key).getKey();
                Log.w("type_this_test",key);

                final DatabaseReference groupRef = eventRef.child("").child(key);

                /**Code on respond to event**/
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Response");
                        builder.setIcon(R.drawable.ic_add_members_group);

                String temp = mList1.get(getAdapterPosition()).getName();
                builder.setMessage("Event: "+temp);

                /**On I'm in click**/
                builder.setPositiveButton("I'm In",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
//                                dialog.cancel();

                                /**Code to respond to the event **/
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final DatabaseReference myRef = database.getReference();

                                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                final String uid = current_user.getUid();
                                final String Groupkey = eventID.getText().toString();
                                final String eventNum = EventNum.getText().toString();
                                myRef.child("members").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).child("name").setValue(dataSnapshot.getValue().toString());
                                        myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).child("value").setValue("In");
                                        }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                /**Code to respond end**/

                                addMe.setText("✓ You are In for this event");
                                Snackbar snackbar = Snackbar
                                    .make(v, "You have been added to the event!", Snackbar.LENGTH_LONG)
                                    .setAction("- Remove me", new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View view) {

                                            /**Code to respond to the event **/
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            final DatabaseReference myRef = database.getReference();

                                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                            final String uid = current_user.getUid();
                                            final String Groupkey = eventID.getText().toString();
                                            final String eventNum = EventNum.getText().toString();
                                            myRef.child("members").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).child("name").setValue(null);
                                                    myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).child("value").setValue(null);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            /**Code to respond end**/



                                            addMe.setText("+ Respond to this event");
                                            Snackbar snackbar1 = Snackbar.make(v, "You have been removed from the event!", Snackbar.LENGTH_SHORT);
                                            View snackbarView = snackbar1.getView();
                                            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                            textView.setTextColor(Color.YELLOW);
                                            textView.setTextSize(14);
                                            snackbar1.show();
                                            /**write code to remove from event*/

                                            /***/
                                        }
                                    });

                                snackbar.setActionTextColor(Color.WHITE);
                                TextView snackbarActionTextView = (TextView)snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
                                snackbarActionTextView.setTextSize(10);

                                View snackbarView = snackbar.getView();
                                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                textView.setTextSize(14);

                                snackbar.show();
                            }
                        });

                builder.setNeutralButton("Maybe",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {

                                /**Code to respond to the event **/
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final DatabaseReference myRef = database.getReference();

                                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                final String uid = current_user.getUid();
                                final String Groupkey = eventID.getText().toString();
                                final String eventNum = EventNum.getText().toString();
                                myRef.child("members").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).child("name").setValue(dataSnapshot.getValue().toString());
                                        myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).child("value").setValue("Maybe");
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                /**Code to respond end**/


//                                        v.getContext().startActivity(new Intent(context, Setup.class));
                                //dialog.cancel();
                                addMe.setText("? You are unsure for this event");

                            }
                        });

                builder.setNegativeButton("I'm Out",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                /**Code to respond to the event **/
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final DatabaseReference myRef = database.getReference();

                                FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                final String uid = current_user.getUid();
                                final String Groupkey = eventID.getText().toString();
                                final String eventNum = EventNum.getText().toString();
                                myRef.child("members").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).child("name").setValue(dataSnapshot.getValue().toString());
                                        myRef.child("Events").child("lists").child(Groupkey).child(eventNum).child("participants").child(uid).child("value").setValue("Out");
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                /**Code to respond end**/


                                addMe.setText("✘ You are out for this event");
                                dialog.cancel();
                            }
                        });
                builder.create().show();




              /** code should add user with status (in/out/maybe) to 1.db 2.ui*/
                  userImageRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        final String uName = dataSnapshot.child("name").getValue().toString();
//                        addMe.setText(uName);

//                        locationText.setText(mList1.get(position).getLocation());
                        int position = 0;

                        String event_id = eventID.getText().toString();

//                        eventRef.child(event_id).child("members").child(uid).setValue(true);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            /** Code to change layout when arrow is clicked - expand view*/
     /*       else if(v.getId() == fullEvent.getId()){

                Toast.makeText(v.getContext(), "Clicked expand",Toast.LENGTH_SHORT).show();
                ViewGroup.LayoutParams params = linearView.getLayoutParams();
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                linearView.setLayoutParams(params);

//                params1.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                myCOnstrained.setLayoutParams(params);
//                ViewGroup.LayoutParams params1 = myCOnstrained.getLayoutParams();

            }*/
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList1.isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;

        }
//        else if (mList2.isEmpty()){
//            ???
//        }

        else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }
}
