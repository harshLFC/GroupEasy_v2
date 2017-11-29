package example.com.groupeasy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.pojo.members_In;

/**
 * This Adapter is
 * 1. Linked with eventDetailsActivity.java and
 * 2. Inflates view mRecycleView
 * This class retrives a list of participants for
 * that specific group
 * for that specific event and displays in a recyclerview**/

public class ParticipantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static ParticipantsAdapter.EventViewHolder.ClickListener clickListener;
    private List<members_In> mLstGroups;
    private Context mContext;

    public ParticipantsAdapter(List<members_In> mLstGroups, Context context) {
        this.mLstGroups = mLstGroups;
    }

    public ParticipantsAdapter() {
        //Compulsary Empty constructor
    }

    //onCreateViewHolder assigns a view for adapter to display in the activity
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_for_members_events, parent, false);

        switch (viewType) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                Toast.makeText(mContext, "Empty", Toast.LENGTH_LONG).show();
                break;
            case VIEW_TYPE_OBJECT_VIEW:

                return new EventViewHolder(rootView);
        }
        return new EventViewHolder(rootView);
    }

    //onBindViewHolder adds data to pre-initialized ui elements
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ParticipantsAdapter.EventViewHolder viewHolder = (ParticipantsAdapter.EventViewHolder) holder;

        viewHolder.userName.setText(mLstGroups.get(position).getName());
        String UserResponse = mLstGroups.get(position).getValue();
        viewHolder.userResponse.setText(UserResponse);

        //Failed code
        /**Trying to change emoticon colors to reflect status to event but failed**/
/*
        if(UserResponse.equals("In")){

            viewHolder.userDP.setBackgroundResource(R.drawable.single_user_nobg_green);
        }
        */

 /*       if(viewHolder.userResponse.getText().toString().equals("In")){

            viewHolder.userDP.setBackgroundResource(R.drawable.single_user_nobg_green);
            Toast.makeText(mContext, "Equals IN" ,Toast.LENGTH_SHORT).show();
            Log.w("in bracket","userresponse");

        }
        String image = (mLstGroups.get(position).getImage());
        viewHolder.userStatus.setText(mLstGroups.get(position).getStatus());

         getting context from view object and trying to load user dp in but failed

        if (image.isEmpty()) {
            viewHolder.userDP.setImageResource(R.drawable.ic_default_user_single);
        } else {
            Picasso.with(viewHolder.itemView.getContext())
                    .load(image)
                    .placeholder(R.drawable.ic_default_user_single)
                    .resize(100, 100)
                    .into(((UserViewHolder) holder).userDP);
        }*/
    }

    @Override
    public int getItemCount() {
        return mLstGroups.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mLstGroups.isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userName;
        private TextView userResponse;
        private ImageView userDP;

        public EventViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userResponse = (TextView) itemView.findViewById(R.id.user_response);
            userDP = (ImageView) itemView.findViewById(R.id.user_dp);
//            userName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            no need for item clicks at this stage, this will be a view only list
      /*      if (v.getId() == userName.getId()) {

                Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();

            } else if (v.getId() == userDP.getId()) {

                Toast.makeText(v.getContext(), "ClickedIMAGE", Toast.LENGTH_SHORT).show();
            }*/
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            ParticipantsAdapter.clickListener = clickListener;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);
            void onItemLongClick(int position, View v);
        }
    }
}
