package example.com.groupeasy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import example.com.groupeasy.R;
import example.com.groupeasy.pojo.Groups;


public class AdapterForAllGroups extends BaseAdapter {

    private List<Groups> listOfAllGroups;
    private LayoutInflater inflater;
    private Context context;

    public AdapterForAllGroups(Context con ,List<Groups> list)
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
            convertView = inflater.inflate(R.layout.row_view_for_groups,null);
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
