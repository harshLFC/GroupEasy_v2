package example.com.groupeasy.adapters;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.chatRoomActivity;
import example.com.groupeasy.pojo.chatMessage;

/**
 * Created by Harsh on 05-09-2017.
 */

public class MessageAdapter extends FirebaseListAdapter<chatMessage> {
    /**
     * @param activity    The activity containing the ListView
     * @param modelClass  Firebase will marshall the data at a location into an instance of a class that you provide
     * @param modelLayout This is the layout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of modelClass.
     * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */


    private chatRoomActivity activity;

    public MessageAdapter(Activity activity, Class<chatMessage> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);

        this.activity = (chatRoomActivity) activity;

    }

    @Override
    protected void populateView(View v, chatMessage model, int position) {

        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);

        messageText.setText(model.getContent());
        messageUser.setText(model.getMessageUserId());

        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        chatMessage chat_message = getItem(position);
        if (chat_message.getMessageUserId().equals(activity.getLoggedInUserName()))
            view = activity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        else
            view = activity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);

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
