package example.com.groupeasy.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import example.com.groupeasy.R;

public class GroupDetailsActivity extends AppCompatActivity {

    public static final String TAG = GroupDetailsActivity.class.getSimpleName();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        this.context = GroupDetailsActivity.this;
        initElementsWithIds();
        initElementsWithListeners();
    }

    private void initElementsWithListeners() {
    }

    private void initElementsWithIds() {

    }
}
