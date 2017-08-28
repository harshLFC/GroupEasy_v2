package example.com.groupeasy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.DashboardActivity;
import example.com.groupeasy.activities.chatRoom;


public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Group> mLstGroups;
    private static GroupViewHolder.ClickListener clickListener;


    public GroupAdapter(){}


    public GroupAdapter(List<Group> mLstGroups) {
        this.mLstGroups = mLstGroups;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //replace R.layout.group_view with your custom layout
        //this file indicates how your custom view should look like (just remember to set parent tags height to wrapcontent)
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_view, parent, false);
        return new GroupViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GroupViewHolder viewHolder = (GroupViewHolder) holder;
        //set values to your views from mlstGroups here
        //ex. viewHolder.txtGroupName.settext(mLstGroups.get(position).groupName)
        viewHolder.textView.setText(mLstGroups.get(position).getGroupName());
    }

    @Override
    public int getItemCount() {
        return mLstGroups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //declare views of your custom class here
        //ex. TextView txtGroupName
        private TextView textView;
        private ImageView imageView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            //Initialize your views here
            //these views should be in your R.layout.groupview
            ///ex. txtGroupName = itemView.findViewById(R.id.txtname)
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == textView.getId()) {
                Toast.makeText(v.getContext(), "Clicked Text!!!", Toast.LENGTH_SHORT).show();
//            clickListener.onItemClick(getAdapterPosition(),v);
                Context context = v.getContext();

                Intent i = new Intent(context,chatRoom.class);
                i.putExtra("room_name",((TextView)v).getText().toString());
                context.startActivity(i);

            }
            else if (v.getId() == imageView.getId()) {
                Toast.makeText(v.getContext(), "Clicked Image!!!", Toast.LENGTH_SHORT).show();
//            clickListener.onItemClick(getAdapterPosition(),v);
            }
        }




        public void setOnItemClickListener(ClickListener clickListener) {
            GroupAdapter.clickListener = clickListener;
        }

        public interface ClickListener {
            void onItemClick(int position, View v);
            void onItemLongClick(int position, View v);
        }
    }
}
