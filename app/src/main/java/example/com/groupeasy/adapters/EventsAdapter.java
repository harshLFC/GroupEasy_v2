package example.com.groupeasy.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.activities.chatRoomActivity;
import example.com.groupeasy.pojo.new_list;

/**
 * Created by Harsh on 11-09-2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<new_list> mListl;
    Context mContext;

    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;

   public EventsAdapter(){

   }


    public EventsAdapter(List<new_list> mLstGroups)
    {
        this.mListl = mLstGroups;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view, parent, false);

        switch(viewType) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                Toast.makeText(mContext, "Empty",Toast.LENGTH_LONG).show();
                break;
            case VIEW_TYPE_OBJECT_VIEW:

                return new EventViewHolder(rootView);
        }

        return new EventViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        EventViewHolder viewHolder = (EventViewHolder) holder;
        //setValues
//        String adminName = "harsh";

        viewHolder.eventName.setText(mListl.get(position).getName());
        viewHolder.admin.setText(mListl.get(position).getLocation());


    }

    @Override
    public int getItemCount() {
        return mListl.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView eventName;
        private TextView admin;


        public EventViewHolder(View itemView) {
            super(itemView);

            eventName = (TextView) itemView.findViewById(R.id.event_name);
            admin = (TextView) itemView.findViewById(R.id.author_of_event);

            eventName.setOnClickListener(this);
            admin.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == eventName.getId() ) {
//            clickListener.onItemClick(getAdapterPosition(),v);
                Context context = v.getContext();

                Intent i = new Intent(context,chatRoomActivity.class);
                context.startActivity(i);

            }
            else if (v.getId() == admin.getId()) {
                Toast.makeText(v.getContext(), "Will open up the image", Toast.LENGTH_SHORT).show();
//            clickListener.onItemClick(getAdapterPosition(),v);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mListl.isEmpty()) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }


}
