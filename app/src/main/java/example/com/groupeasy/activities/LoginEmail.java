package example.com.groupeasy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import example.com.groupeasy.R;

/**
 * Created by Harsh on 31-08-2017.
 */

public class LoginEmail extends AppCompatActivity {

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        mToolBar = (Toolbar) findViewById(R.id.create_acc_tool);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Create An Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}