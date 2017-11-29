package example.com.groupeasy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.pojo.users_list;

/**
 * This adapter class to display a list of members of the app
 * It is linked with chooseUserActivity.java
 * It displays members within a group to mListGroups
 * User can select the members he wishes to add in group using the checkboxes
 **/

public class UsersSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    Context mContext;
    //init two ists, one of users data from firebase, second for search functionality
    private List<users_list> mLstGroups;
    private List<users_list> mFilteredList;
    private int selectedPos = 0;
    private List<users_list> studentist;

    public UsersSelectAdapter(){

    }

    /**add a constructor to the custom adapter to handle data that
     RecyclerView displays.data is in the form of a List of <Group> objects**/
    public UsersSelectAdapter(List<users_list> mLstGroups)
    {
        this.mLstGroups = mLstGroups;
        this.mFilteredList = mLstGroups;
    }

    /**We specify the layout that each item of the RecyclerView should use.This is done by
     inflating the layout using LayoutInflater, passing the output to the constructor of the custom ViewHolder**/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);

        switch(viewType) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                Toast.makeText(mContext, "Empty",Toast.LENGTH_LONG).show();
                break;
            case VIEW_TYPE_OBJECT_VIEW:

                return new UserViewHolder(rootView);
        }
        return new UserViewHolder(rootView);
    }

    /***Override the onBindViewHolder to specify the contents of each item of the RecyclerView.
    * This method is very similar to the getView method of a ListView's adapter.
     * Here's where I set the values of the name, admin, and photo fields of the RecycleView.
     **/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
       final UserViewHolder viewHolder = (UserViewHolder) holder;
       final int pos = position;

        viewHolder.userName.setText(mLstGroups.get(position).getName());
        String image = (mLstGroups.get(position).getImage());
        viewHolder.userStatus.setText(mLstGroups.get(position).getStatus());
        viewHolder.userMagicId.setText(mLstGroups.get(position).getId());

        String groupId = (mLstGroups.get(position).getId());

        /**mFiltered list is the list used for search functionality**/
        viewHolder.userName.setText(mFilteredList.get(pos).getName());
        viewHolder.userStatus.setText(mFilteredList.get(pos).getStatus());
        viewHolder.userMagicId.setText(mFilteredList.get(pos).getId());

        /**Get current user id**/
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();

        viewHolder.myCheck.setChecked(mFilteredList.get(position).isSelected());
        viewHolder.myCheck.setTag(mFilteredList.get(position));

        if(viewHolder.userMagicId.getText().toString().equals(uid)){
            viewHolder.myCheck.setChecked(true);

        }

        //Failed code
// tring to implement the logic for having users already checked if in the group,

/*        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("groups").child("-KvCqeoV8oR408WTPks9").child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //this code is giving me the children who are set to true, but i dont know how to
                // 1. check the right group
                // 2. keep the checkbox selected
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (mFilteredList.get(position).getId().equals(dataSnapshot.getValue().toString())) {

                        Log.w("userSelectedTest", dataSnapshot.getValue().toString());
                    }
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //Getting checkbox value by attaching a TAG
        viewHolder.myCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                users_list contact = (users_list) cb.getTag();
                contact.setSelected(cb.isChecked());
                mFilteredList.get(pos).setSelected(cb.isChecked());
                }
        });


        //Setting user DP
        if (image.isEmpty()) {
            viewHolder.userDP.setImageResource(R.drawable.ic_default_user_single);
        } else {
            Picasso.with(mContext)
                    .load(image)
                    .placeholder(R.drawable.single_user)
                    .resize(100, 100)
                    .into(((UserViewHolder) holder).userDP);
        }

        viewHolder.itemView.setSelected(selectedPos == position);

    }

    @Override
    public int getItemCount() {
//        return mLstGroups.size();
        return mFilteredList.size();

    }

    public List<users_list> getUserId() {
        return mLstGroups;
    }

    /**Logic for filtering users in the below overriden method**/
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mLstGroups;
                } else {

                    List<users_list> filteredList = new ArrayList<>();

                    for (users_list androidVersion : mLstGroups) {

                        if (androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getName().toLowerCase().contains(charString) || androidVersion.getId().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<users_list>) filterResults.values;
                notifyDataSetChanged();
            }
        };

//        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mLstGroups.isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userName;
        private TextView userStatus;
//        private TextView userLastSeen;
private CheckBox myCheck;
        private ImageView userDP;

        private TextView userMagicId;

                public UserViewHolder(View itemView) {
            super(itemView);
            //Initialize your views here
            //these views should be in your R.layout.groupview
            ///ex. txtGroupName = itemView.findViewById(R.id.txtname)

            userName = (TextView) itemView.findViewById(R.id.user_name);
            userStatus = (TextView) itemView.findViewById(R.id.user_status);
//            userLastSeen = (TextView) itemView.findViewById(R.id.last_seen);
                    myCheck = (CheckBox) itemView.findViewById(R.id.my_check);
            userMagicId = (TextView) itemView.findViewById(R.id.magic_user_id);

                    userDP = (ImageView) itemView.findViewById(R.id.user_dp);
                    userName.setOnClickListener(this);
                    userDP.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
//            Toast.makeText(v.getContext(), selectedPos,Toast.LENGTH_SHORT).show();
//
//            if (v.getId() == userName.getId()) {
//
//                Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
//
//            } else if (v.getId() == userDP.getId()) {
//
//                Toast.makeText(v.getContext(), "ClickedIMAGE", Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
