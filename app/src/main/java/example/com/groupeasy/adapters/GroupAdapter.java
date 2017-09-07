package example.com.groupeasy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.chatRoomActivity;

import static example.com.groupeasy.R.id.image_view;


public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private List<Group> mLstGroups;
    private static GroupViewHolder.ClickListener clickListener;

    public GroupAdapter(){

    }


    /**add a constructor to the custom adapter to handle data that
     RecyclerView displays.data is in the form of a List of <Group> objects**/

    public GroupAdapter(List<Group> mLstGroups)
    {
        this.mLstGroups = mLstGroups;
    }

    /**We specify the layout that each item of the RecyclerView should use.This is done by
     inflating the layout using LayoutInflater, passing the output to the constructor of the custom ViewHolder**/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // TODO: 30-08-2017 remove comments
        //replace R.layout.group_view with your custom layout
        //this file indicates how your custom view should look like (just remember to set parent tags height to wrap content)

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_view, parent, false);
        return new GroupViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        GroupViewHolder viewHolder = (GroupViewHolder) holder;

        //set values to your views from mlstGroups here
        //ex. viewHolder.txtGroupName.settext(mLstGroups.get(position).groupName)
        viewHolder.textView.setText(mLstGroups.get(position).getGroupName());
//        viewHolder.imageView.setImage

        String image = "try";

        // getting context from view object

        Picasso.with(((GroupViewHolder) holder).imageView.getContext())
                .load(image)
                .resize(100,100)
                .into(((GroupViewHolder) holder).imageView);

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
        private TextView textLastMessage;

        public GroupViewHolder(View itemView) {
            super(itemView);
            //Initialize your views here
            //these views should be in your R.layout.groupview
            ///ex. txtGroupName = itemView.findViewById(R.id.txtname)
            imageView = (ImageView) itemView.findViewById(image_view);
            textView = (TextView) itemView.findViewById(R.id.message_text);
            textLastMessage = (TextView) itemView.findViewById(R.id.text_last_message);

            textView.setOnClickListener(this);
            textLastMessage.setOnClickListener(this);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == textView.getId() || v.getId() == textLastMessage.getId()) {
//            clickListener.onItemClick(getAdapterPosition(),v);
                Context context = v.getContext();

                Intent i = new Intent(context,chatRoomActivity.class);
                i.putExtra("room_name",((textView.getText().toString())));
                context.startActivity(i);

            }
            else if (v.getId() == imageView.getId()) {
                Toast.makeText(v.getContext(), "Will open up the image", Toast.LENGTH_SHORT).show();
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
