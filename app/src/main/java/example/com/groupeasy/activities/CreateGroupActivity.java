package example.com.groupeasy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import example.com.groupeasy.R;

public class CreateGroupActivity extends AppCompatActivity {

    private Context context;
    private ImageView ivClose, groupDP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        context = CreateGroupActivity.this;
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        initElementsWithIds();
        initElementsWithListeners();
    }

    private void initElementsWithListeners() {

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        groupDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Will open gallery",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initElementsWithIds() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        groupDP = (ImageView) findViewById(R.id.GroupDPImageView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
