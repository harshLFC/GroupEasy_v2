package example.com.groupeasy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import example.com.groupeasy.adapters.EventsAdapter;
import example.com.groupeasy.pojo.new_groups;

public class lists_activity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EventsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<new_groups> mLstGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        initElementsByIds();
        initElementsByListeners();

        mLstGroups = new ArrayList<>();

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    private void initElementsByListeners() {

    }

    private void initElementsByIds() {

        mRecyclerView = (RecyclerView) findViewById(R.id.events_recycler_view);
    }
}
