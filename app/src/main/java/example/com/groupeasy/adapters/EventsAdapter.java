package example.com.groupeasy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.lists_activity;
import example.com.groupeasy.pojo.list_primary;
import example.com.groupeasy.pojo.members_In;

/**
 * Created by Harsh on 11-09-2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static List<list_primary> mListl;
//    HashMap mList2;
    static List<members_In> mList2;
    Context mContext;
    LayoutInflater mInflater;
    View view;
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    final DatabaseReference groupRef = database.getReference().child("Events").child("lists");

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static final String VIEW_TYPE_OBJECT_VIEW_TRY = "THis list is empty";

   public EventsAdapter(){

   }

    public EventsAdapter(List<list_primary> mLstGroups, Context context, List<members_In> mLstGroups2)
//    public EventsAdapter(List<list_primary> mLstGroups,Context context,HashMap<String, String> myMap)
    {
        this.mListl = mLstGroups;
        this.mContext = context;
        this.mList2 = mLstGroups2;
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

//        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_for_members_events, parent, false);

//        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_for_members_events,parent,false);
//        view = mInflater.inflate(R.layout.row_view_for_members_events, parent, false);

//                view = mInflater.inflate(R.layout.row_view_for_members_events, null);
        return new EventViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final EventViewHolder viewHolder = (EventViewHolder) holder;

        viewHolder.eventName.setText(mListl.get(position).getName());
        viewHolder.admin.setText(mListl.get(position).getAdmin());
        String event_id = mListl.get(position).getId();

        String test = mList2.get(position).getName();
        String test2 = mListl.get(position).getId();

        Log.d(test,"lala");
        Log.d(test2,"yaya");

        //if no location has been provided dont show
    try{
        if((mListl.get(position).getLocation())!= null) {
            viewHolder.locationText.setVisibility(View.VISIBLE);
            viewHolder.locationText.setText(mListl.get(position).getLocation());
            viewHolder.locationImage.setVisibility(View.VISIBLE);
               }

            }
    catch (NullPointerException e){
        e.printStackTrace();
        }

        viewHolder.eventID.setText(mListl.get(position).getId());

        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Events").child("lists");
        final String key = eventRef.getKey();

        String uid = eventRef.getKey();
        Log.i("uid", uid);

        //what does this do?
//        eventRef.addListenerForSingleValueEvent(eventListener);

        viewHolder.userImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = dataSnapshot.child("image").getValue().toString();
                Picasso.with(mContext)
                        .load(image)
                        .placeholder(R.drawable.ic_default_groups)
                        .resize(50,50)
                        .into(((EventsAdapter.EventViewHolder) holder).userImage);
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
        LayoutInflater.from(mContext).inflate(R.layout.row_view_for_members_events,null,false);
        view = mInflater.inflate(R.layout.row_view_for_members_events, null, false);

        //for loop int =0; i<list2.size; i++
//            textview tv = new textview

        for(int i=0;i<mList2.size();i++){

            TextView textView = new TextView(mContext);
            TextView textView2 = new TextView(mContext);
            textView.setText(mList2.get(i).getName());
            textView2.setText(mList2.get(i).getValue());

            viewHolder.linearView.addView(textView);
            viewHolder.linearView.addView(textView2);

        }

        // add data by setText
//        liearlayout.add(child)

        view = mInflater.inflate(R.layout.row_view_for_members_events, null);

    }

    @Override
    public int getItemCount() {
        return mListl.size();
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
        private LinearLayout linearView;

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

            eventName.setOnClickListener(this);
            admin.setOnClickListener(this);
            addMe.setOnClickListener(this);

        }

        @Override
        public void onClick(final View v) {

            if (v.getId() == eventName.getId() ) {
//            clickListener.onItemClick(getAdapterPosition(),v);
                Toast.makeText(v.getContext(), "Something to do with name", Toast.LENGTH_SHORT).show();
            }
            else if (v.getId() == admin.getId()) {
                Toast.makeText(v.getContext(), "Admin", Toast.LENGTH_SHORT).show();
//            clickListener.onItemClick(getAdapterPosition(),v);
            }
            else if (v.getId() == addMe.getId()){

                final String key = eventRef.getKey();
                Log.w("type_this",key);

                String test = eventRef.child(key).getKey();
                Log.w("type_this_test",key);

                final DatabaseReference groupRef = eventRef.child("").child(key);

                userImageRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String uName = dataSnapshot.child("name").getValue().toString();
                        addMe.setText(uName);

//                        locationText.setText(mListl.get(position).getLocation());
                        int position = 0;

                        String event_id = eventID.getText().toString();

                        eventRef.child(event_id).child("members").child(uid).setValue(true);

                        Snackbar snackbar = Snackbar
                                .make(v, "You have been added to the event!", Snackbar.LENGTH_LONG)
                                .setAction("- Remove me", new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        Snackbar snackbar1 = Snackbar.make(v, "You have been removed from the event!", Snackbar.LENGTH_SHORT);
                                        View snackbarView = snackbar1.getView();
                                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                        textView.setTextColor(Color.YELLOW);
                                        textView.setTextSize(14);
                                        snackbar1.show();
                                    }
                                });

                        snackbar.setActionTextColor(Color.WHITE);
                        TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById( android.support.design.R.id.snackbar_action );
                        snackbarActionTextView.setTextSize(10);

                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        textView.setTextSize(14);

                        snackbar.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mListl.isEmpty()) {
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
