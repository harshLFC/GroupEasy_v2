package example.com.groupeasy.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import example.com.groupeasy.R;


public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Group> mLstGroups;

    public GroupAdapter(){};


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

    public class GroupViewHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
