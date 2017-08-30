package example.com.groupeasy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import example.com.groupeasy.R;

public class chatRoomActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private String room_name;
    private TextView roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_main);

        initElementWithIds();

        //snippet to stop keyboard from appearing onCreate
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // get room name from last intent and override the chatroom title
        room_name = getIntent().getExtras().get("room_name").toString();
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(room_name);
        roomName.setText(room_name);
        roomName.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       }

    private void initElementWithIds() {

        mToolBar = (Toolbar) findViewById(R.id.toolbar_chat);
        roomName = (TextView) findViewById(R.id.room_name);


    }
}
