package example.com.groupeasy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.DashboardActivity;
import example.com.groupeasy.activities.chatRoomActivity;
import example.com.groupeasy.pojo.new_groups;

import static example.com.groupeasy.R.id.image_view;

/**
 * This Adapter is
 * 1. Linked with groupFragment.java and
 * 2. Inflates view mGroupRecyclerView**/

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<new_groups> mLstGroups;
    private static GroupViewHolder.ClickListener clickListener;
    Context mContext;
    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    static FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;

    public GroupAdapter(){

    }

    /**add a constructor to the custom adapter to handle data that
     RecyclerView displays.data is in the form of a List of <Group> objects**/
    public GroupAdapter(List<new_groups> mLstGroups)
    {
        this.mLstGroups = mLstGroups;
    }

    /**We specify the layout that each item of the RecyclerView should use.This is done by
     inflating the layout using LayoutInflater, passing the output to the constructor of the custom ViewHolder**/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_view, parent, false);

        switch(viewType) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                Toast.makeText(mContext, "Empty",Toast.LENGTH_LONG).show();
                break;
            case VIEW_TYPE_OBJECT_VIEW:

                return new GroupViewHolder(rootView);
        }

        // TODO: 30-08-2017 remove comments
        //replace R.layout.group_view with your custom layout
        //this file indicates how your custom view should look like (just remember to set parent tags height to wrap content)

        return new GroupViewHolder(rootView);
    }

    /***Override the onBindViewHolder to specify the contents of each item of the RecyclerView.
     * This method is very similar to the getView method of a ListView's adapter.
     * Here's where you have to set the values of the name, admin, and photo fields of the RecycleView.
    **/
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final GroupViewHolder viewHolder = (GroupViewHolder) holder;

        //set values to your views from mlstGroups here
        //ex. viewHolder.txtGroupName.settext(mLstGroups.get(position).groupName)
        viewHolder.textView.setText(mLstGroups.get(position).getName());
        String image = (mLstGroups.get(position).getThumb_image());
        viewHolder.textLastMessage.setText(mLstGroups.get(position).getLast_msg());
        viewHolder.groupKey.setText(mLstGroups.get(position).getGroup_id());
        viewHolder.Admin.setText(mLstGroups.get(position).getAdmin());
        final String groupID = mLstGroups.get(position).getGroup_id();
        viewHolder.textLastMessage.setText(mLstGroups.get(position).getLast_msg());

        String lastMsg =  mLstGroups.get(position).getLast_msg();
        if (lastMsg.equals("You have no messages in the group")){
            viewHolder.textLastMessage.setTextColor(Color.GRAY);

        }

        // Failed code
        //Code to display last seen message by Referencing the message database and looping through its children

//        mDatabase.child("messages").child(groupID).child("groupMsgs").child("").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.child("").exists()){
//                    Query query = mDatabase.child("messages").child(groupID).child("groupMsgs").orderByKey().limitToLast(1);
//                    query.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
////                String msg = dataSnapshot.child("content").getValue().toString();
////                Log.w(msg,"msg");
//                            for(DataSnapshot child : dataSnapshot.getChildren()){
//
//                                Log.d("Testing", child.child("content").getValue().toString());
//                                viewHolder.textLastMessage.setText(child.child("content").getValue().toString());
//                                viewHolder.textLastMessage.setTextColor(Color.BLACK);
//                            }
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                        }
//                    });
//                }
//                else
//                    viewHolder.textLastMessage.setText("No messages in group");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        /**code for showing rectangle and setting value according to the number of events present**/
        mDatabase.child("Events").child("lists").child(groupID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot!= null) {
                    //trying to introduce i variable to loop hw many events are there and display that number
                    //but failed code
//                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    int numOfEvents = (int) dataSnapshot.getChildrenCount();
                    Log.w(String.valueOf(numOfEvents),"nsdhajksdajsd");


                        viewHolder.myCircle.setVisibility(View.VISIBLE);
                        viewHolder.myCircle.setText(String.valueOf((int) (dataSnapshot.getChildrenCount())));
                        viewHolder.myCircle.setTextColor(Color.WHITE);
//                    }
                    //note : remove the above for loop for showing 0 events across all rows

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //code to set name as admin, but for some reason its crashing randomly
        //solved error by having a if condition
        mDatabase.child("members").child(mLstGroups.get(position).getAdmin()).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {
                    viewHolder.Admin.setText(dataSnapshot.getValue().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // getting context from view object
        if(image.isEmpty()){
            viewHolder.imageGroupView.setImageResource(R.drawable.ic_default_groups);
            viewHolder.imageGroupView.setAlpha(0f);
        }
        else    {
            //Handling image display
            Picasso.with(mContext)
                    .load(image)
                    .placeholder(R.drawable.multi_user)
                    .resize(100,100)
                    .onlyScaleDown()
                    .into(((GroupViewHolder) holder).imageGroupView);
        }
    }

    @Override
    public int getItemCount() {
        return mLstGroups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        //declaring views of your custom class
        private TextView textView;
        private TextView Admin;
        private ImageView imageGroupView;
        private TextView textLastMessage;
        private TextView groupKey;
        private LinearLayout groupLinear;
        private TextView myCircle;

        public GroupViewHolder(View itemView) {
            super(itemView);
            //Initialize your views here
            //these views should be in your R.layout.groupview
            ///ex. txtGroupName = itemView.findViewById(R.id.txtname)
            imageGroupView = (ImageView) itemView.findViewById(image_view);
            textView = (TextView) itemView.findViewById(R.id.message_text);
            Admin = (TextView) itemView.findViewById(R.id.textViewAdmin);
            textLastMessage = (TextView) itemView.findViewById(R.id.text_last_message);
            groupKey = (TextView) itemView.findViewById(R.id.group_key);
            groupLinear = (LinearLayout) itemView.findViewById(R.id.group_linear);

            myCircle = (TextView) itemView.findViewById(R.id.circle);

            imageGroupView.setOnClickListener(this);
            groupLinear.setOnClickListener(this);
            groupLinear.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Handling click actions
            if (v.getId() == groupLinear.getId() |v.getId() == imageGroupView.getId() ) {
                Context context = v.getContext();

                Intent i = new Intent(context,chatRoomActivity.class);
//                i.putExtra("room_name",((textView.getText().toString())));
                i.putExtra("groupKey",((groupKey.getText().toString())));
                i.putExtra("eventNum",((myCircle.getText().toString())));

                context.startActivity(i);
                ((Activity)context).finish();

            }
//            else if (v.getId() == imageGroupView.getId()) {
//            Toast.makeText(v.getContext(), "Will open up the image", Toast.LENGTH_SHORT).show();
//            clickListener.onItemClick(getAdapterPosition(),v);
//            }
                    }

//handle long clicks
        @Override
        public boolean onLongClick(View v) {

            if (v.getId() == groupLinear.getId()) {
//            clickListener.onItemClick(getAdapterPosition(),v);
                final Context context = v.getContext();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you wish to Exit this group?")
                        .setTitle("Exit group")
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

//              TODO
                /**delete 1(.members>groupIn)
                 *  delete 2(groups>group>members>member removeValue();
                 3.(Check if no users, then 1(.groups>delete group) 2(.messages>group>delete)
                 by setting .removeValue();
                **/
                String uid = current_user.getUid();

                mDatabase.child("members").child(uid).child("groupsIn").child((groupKey.getText().toString())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                        Toast.makeText(context, "Group Left !",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context,DashboardActivity.class);
                        context.startActivity(i);
                        ((Activity)context).finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                 }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create().show();
            }
            return true;
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            GroupAdapter.clickListener = clickListener;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);
            void onItemLongClick(int position, View v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mLstGroups.isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }
}
