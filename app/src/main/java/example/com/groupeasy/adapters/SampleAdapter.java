package example.com.groupeasy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.pojo.sampleGroupsPojo;


public class SampleAdapter extends BaseAdapter {

    private List<sampleGroupsPojo> listOfAllGroups;
    private LayoutInflater inflater;
    private Context context;

    public SampleAdapter(Context con , List<sampleGroupsPojo> list)
    {
        this.context = con;
        this.listOfAllGroups = list;
        this.inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listOfAllGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfAllGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder  = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.group_view,null);
        }
        else
        {

        }
        return convertView;
    }

    private class ViewHolder
    {
        private TextView tvGroupName;
    }

}
