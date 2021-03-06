package example.com.groupeasy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.pojo.users_list;

/**
 * This adapter class to display a list of members of the app
 * It is linked with aboutChatRoom.java
 * Ir displays members within a group to mUserRecyclerView
 * **/

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    private static UserAdapter.UserViewHolder.ClickListener clickListener;
    Context mContext;
    private List<users_list> mLstGroups;

    public UserAdapter() {
        //Compulsary Empty constructor
    }

    public UserAdapter(List<users_list> mLstGroups) {
        this.mLstGroups = mLstGroups;
    }

    //onCreateViewHolder assigns a view for adapter to display in the activit
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false);

        switch (viewType) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                Toast.makeText(mContext, "Empty", Toast.LENGTH_LONG).show();
                break;
            case VIEW_TYPE_OBJECT_VIEW:

                return new UserViewHolder(rootView);
        }

        return new UserViewHolder(rootView);
    }

    //onBindViewHolder adds data to pre-initialized ui elements
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //Set data using pojo class's setter properties
        UserAdapter.UserViewHolder viewHolder = (UserAdapter.UserViewHolder) holder;
        viewHolder.userName.setText(mLstGroups.get(position).getName());
        String image = (mLstGroups.get(position).getImage());
        viewHolder.userStatus.setText(mLstGroups.get(position).getStatus());

        //Add user Display picture
//        if (image.isEmpty()) {
//            viewHolder.userDP.setImageResource(R.drawable.ic_default_user_single);
//        } else {
            Picasso.with(viewHolder.itemView.getContext())
                    .load(image)
                    .placeholder(R.drawable.single_user)
                    .resize(100, 100)
                    .into(((UserViewHolder) holder).userDP);
//        }
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

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView userName;
        private TextView userStatus;
        private CheckBox myCheck;
        private ImageView userDP;

        public UserViewHolder(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.user_name);
            userStatus = (TextView) itemView.findViewById(R.id.user_status);
            userDP = (ImageView) itemView.findViewById(R.id.user_dp);
            myCheck = (CheckBox) itemView.findViewById(R.id.my_check);
            myCheck.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
       /*     if (v.getId() == userName.getId()) {

                Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();

            } else if (v.getId() == userDP.getId()) {

                Toast.makeText(v.getContext(), "ClickedIMAGE", Toast.LENGTH_SHORT).show();
            }*/
        }

        public void setOnItemClickListener(ClickListener clickListener) {
//            UserAdapter.clickListener = clickListener;
        }

        public interface ClickListener {
//            void onItemClick(int position, View v);
//            void onItemLongClick(int position, View v);
        }
    }
}
