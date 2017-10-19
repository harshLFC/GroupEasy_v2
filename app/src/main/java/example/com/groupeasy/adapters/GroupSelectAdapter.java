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
import example.com.groupeasy.pojo.new_groups;

import static example.com.groupeasy.R.id.image_view;


public class GroupSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<new_groups> mLstGroups;
    private static GroupViewHolder.ClickListener clickListener;
    Context mContext;

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;

    public GroupSelectAdapter(){

    }

    /**add a constructor to the custom adapter to handle data that
     RecyclerView displays.data is in the form of a List of <Group> objects**/
    public GroupSelectAdapter(List<new_groups> mLstGroups)
    {
        this.mLstGroups = mLstGroups;
    }

    /**We specify the layout that each item of the RecyclerView should use.This is done by
     inflating the layout using LayoutInflater, passing the output to the constructor of the custom ViewHolder**/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_select_view, parent, false);

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GroupViewHolder viewHolder = (GroupViewHolder) holder;

        //set values to your views from mlstGroups here
        //ex. viewHolder.txtGroupName.settext(mLstGroups.get(position).groupName)
        viewHolder.textView.setText(mLstGroups.get(position).getName());
        String image = (mLstGroups.get(position).getThumb_image());
        viewHolder.groupKey.setText(mLstGroups.get(position).getGroup_id());

        // getting context from view object

        if(image.isEmpty()){
                viewHolder.imageGroupView.setImageResource(R.drawable.ic_default_groups);
        }
        else    {
            Picasso.with(mContext)
                    .load(image)
                    .placeholder(R.drawable.ic_default_groups)
                    .resize(100,100)
                    .into(((GroupViewHolder) holder).imageGroupView);
        }
    }

    @Override
    public int getItemCount() {
        return mLstGroups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //declare views of your custom class here
        //ex. TextView txtGroupName
        private TextView textView;
        private ImageView imageGroupView;
        private TextView groupKey;
        private CheckBox checkBox;
        private Context mContext;

        public GroupViewHolder(View itemView) {
            super(itemView);
            //Initialize your views here
            //these views should be in your R.layout.groupview
            ///ex. txtGroupName = itemView.findViewById(R.id.txtname)
            imageGroupView = (ImageView) itemView.findViewById(image_view);
            textView = (TextView) itemView.findViewById(R.id.message_text);
            groupKey = (TextView) itemView.findViewById(R.id.group_key);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);

            textView.setOnClickListener(this);
            imageGroupView.setOnClickListener(this);
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == checkBox.getId() ) {
//

            }
        }

        public void setOnItemClickListener(ClickListener clickListener) {
            GroupSelectAdapter.clickListener = clickListener;
            Toast.makeText( mContext, "",Toast.LENGTH_SHORT).show();
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
