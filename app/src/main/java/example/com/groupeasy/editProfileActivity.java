package example.com.groupeasy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class editProfileActivity extends AppCompatActivity {

    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mToolBar = (Toolbar) findViewById(R.id.myToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Edit your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
