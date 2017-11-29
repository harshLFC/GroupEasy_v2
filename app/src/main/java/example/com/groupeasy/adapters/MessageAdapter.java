package example.com.groupeasy.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import example.com.groupeasy.R;
import example.com.groupeasy.activities.chatRoomActivity;
import example.com.groupeasy.pojo.chatMessage;
import example.com.groupeasy.pojo.users_list;

/**
 * This Adapter is
 * 1. Linked with chatRoomActivity.java and
 * 2. Inflates view listView
 * This class retrives chats from database
 *  It checks if message is from sender
 *  if it is, then inflates a msg_out view
 *  else inflates msg_in view**/

public class MessageAdapter extends FirebaseListAdapter<chatMessage> {
    Context mContext;
    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = current_user.getUid();
    /**
     * @param activity    The activity containing the ListView
     * @param modelClass  Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout This is the layout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of modelClass.
     * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private chatRoomActivity activity;

    public MessageAdapter(Activity activity, Class<chatMessage> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.activity = (chatRoomActivity) activity;
    }

    //populateView assigns a view for adapter to display in the activity

    @Override
    protected void populateView(final View v, final chatMessage model, int position) {

        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        final TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        final CircleImageView sender_image = (CircleImageView) v.findViewById(R.id.imageView2);

        messageText.setText(model.getContent());
        String user = model.getMsgFrom();

        //tried to do regular adapter init but crashed
//        messageUser.setText(mLstChat.get(position).getMessageUserId());

        mDatabase.child("members").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users_list usersList = dataSnapshot.getValue(users_list.class);
                String name = usersList.getName();
                String image = usersList.getImage();
//                sender_image.setVisibility(View.VISIBLE);
//                messageUser.setText(name);

                if(image.isEmpty()) {
                        sender_image.setImageResource(R.drawable.ic_default_user_single);
                }
            else    {
                    if (!(usersList.getId().equals(uid))) {
                        messageUser.setText(name);
                        Picasso.with(mContext)
                                .load(image)
                                .resize(100, 100)
                                .placeholder(R.drawable.single_user)
                                .centerCrop()
                                .into(sender_image);
                    }
                }
             }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        chatMessage chat_message = getItem(position);

            if (chat_message.getMsgFrom().equals(uid)) {
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        }

        else {
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);
        }

        //generating view
        populateView(view, chat_message, position);
        return view;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
}
