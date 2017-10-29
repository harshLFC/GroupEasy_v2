package example.com.groupeasy.activities;

import android.app.usage.UsageEvents;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import example.com.groupeasy.R;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView EventName;
    private TextView EventLocation;
    private TextView EventAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);


        setToolbar();
        initElementsById();
        updateDisplay();



    }

    private void updateDisplay() {

        String name = getIntent().getStringExtra("name");
        EventName.setText(name);


        String location = getIntent().getStringExtra("location");
        if(!location.isEmpty()){

            EventLocation.setText(location);
        }

        String admin = getIntent().getStringExtra("admin");
        if(!location.isEmpty()){

            EventAdmin.setText(admin);
        }

    }

    private void initElementsById() {

        EventName  = (TextView) findViewById(R.id.event_name);
        EventLocation  = (TextView) findViewById(R.id.event_location);
        EventAdmin  = (TextView) findViewById(R.id.text_admin);

    }

    private void setToolbar() {
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow_left);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.my_event_details_tool);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Event");
        getSupportActionBar().setShowHideAnimationEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
