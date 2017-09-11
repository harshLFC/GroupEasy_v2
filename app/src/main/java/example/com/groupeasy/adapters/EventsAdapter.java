package example.com.groupeasy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.pojo.new_list;

/**
 * Created by Harsh on 11-09-2017.
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<new_list> mListl;
    Context mContext;

    public EventsAdapter(List<new_list> mLstGroups)
    {
        this.mListl = mLstGroups;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view, parent, false);

        return new GroupAdapter.GroupViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        EventViewHolder viewHolder = (EventViewHolder) holder;
        //setValues
        String adminName = "harsh";

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
        }

        @Override
        public void onClick(View v) {

        }
    }


}
