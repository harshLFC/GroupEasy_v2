package example.com.groupeasy.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import example.com.groupeasy.R;
import example.com.groupeasy.fragments.SearchFragment;

public class NextBuildActivity extends AppCompatActivity{

    private Context context;
    private Button goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.next_build);
        this.context = NextBuildActivity.this;
        initElementsWithIds();
        initElementsWithListeners();

    }

    private void initElementsWithListeners() {
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initElementsWithIds() {
        goBack = (Button) findViewById(R.id.backButton);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
    }

}
