/*
package example.com.groupeasy.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import example.com.groupeasy.R;

*/
/** This activity is used to create polls
 *  also you can add/delete options for the polls
 * *//*

public class CreatePollActivity extends AppCompatActivity {

    private final static String TAG = CreatePollActivity.class.getSimpleName();
    private Context context;
    private Button  btnAddOptions;
    private LinearLayout layoutForOptions;
    private ImageView ivClose;
    private TextView savePoll;

    //database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        this.context = CreatePollActivity.this;

        initElementsWithIds();
        initElementsWithListeners();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initElementsWithListeners() {
        */
/** add your options on add option button*//*

        btnAddOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                */
/** inflate the options view and add it to the main view
                 * also you can delete your option*//*

                LayoutInflater inflater = getLayoutInflater();

                final View addView = inflater.inflate(R.layout.row_view_for_add_options,null);
                addView.setPadding(30,20,30,16);

                EditText etOption = (EditText) addView.findViewById(R.id.et_option);
                ImageView ivDeleteOption = (ImageView) addView.findViewById(R.id.iv_delete_option);

                */
/** delete your particular option *//*

                ivDeleteOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout) addView.getParent()).removeView(addView);
                    }
                });

                // adding this new option to your main view

                layoutForOptions.addView(addView);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        savePoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(v.getContext(), "Will Push Poll Data to server",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    */
/* initialize your widgets with their ids *//*

    private void initElementsWithIds() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        btnAddOptions = (Button) findViewById(R.id.btn_add_options);
        layoutForOptions = (LinearLayout) findViewById(R.id.layout_for_options);
        savePoll = (TextView) findViewById(R.id.save_poll);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
*/
