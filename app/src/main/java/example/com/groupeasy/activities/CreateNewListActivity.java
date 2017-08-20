package example.com.groupeasy.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import example.com.groupeasy.R;
import example.com.groupeasy.fragments.DatePickerFragment;
import example.com.groupeasy.fragments.TimePickerFragment;

/** Activity which creates new list */
@RequiresApi(api = Build.VERSION_CODES.N)
public class CreateNewListActivity extends AppCompatActivity {

    public static final String TAG = CreateNewListActivity.class.getSimpleName();
    private Context context;
    private TextView tvRangeLimit;
    private TextView eventName;
    private TextView location;
    private TextView TvFrom;
    private TextView TvTo;
    private CrystalSeekbar participantRangeBar;
    private ImageView ivClose;
    private Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);
        context = CreateNewListActivity.this;
        initElementsWithIds();
        initElementsWithListeners();
        changeTime();
    }

    private void changeTime() {
            //code to receive data and change clock
        String Hours, Mins;

    }

    private void initElementsWithListeners() {
        participantRangeBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                tvRangeLimit.setText(String.valueOf(value));
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
    }

    public void initElementsWithIds() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        participantRangeBar = (CrystalSeekbar) findViewById(R.id.range_seekbar);
        tvRangeLimit = (TextView) findViewById(R.id.tv_range_limit);
        location = (EditText) findViewById(R.id.location);
        eventName = (EditText) findViewById(R.id.eventName);
        TvFrom = (EditText) findViewById(R.id.tv_from_date);
        TvTo = (EditText) findViewById(R.id.tv_to_date);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,DashboardActivity.class);
        startActivity(intent);
        finish();
    }


    public void saveDetails(View view) {


       //Code for form Validation and subsequent code
        String EventName;
        EventName = eventName.getText().toString();
        if(EventName == "" || EventName == null || EventName.equals(null) ){
            Toast.makeText(this, "Please enter a Name for the event",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Intent for choosing groups to assign",Toast.LENGTH_SHORT).show();
        }

    }

    public void dateFrom(View v) {

        // code for opening calender
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Date Picker");


    }
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    private void updateDisplay() {

    }

    public void dateTo(View view) {

        // code for opening calender
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Date Picker");

    }

    public void timeFrom(View view) {

        // code for opening clock
//this code did not work -       newFragment.show(getSupportFragmentManager(), "timePicker");
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"Time Picker");

    }

    public void timeTo(View view) {

        // code for opening clock
        DialogFragment newFragment = new TimePickerFragment();
//this code did not work -       newFragment.show(getSupportFragmentManager(), "timePicker");
        newFragment.show(getFragmentManager(),"Time Picker");

    }

}

 class myOnDateChangedListener implements DatePicker.OnDateChangedListener{

//     EditText TvFrom = (EditText) findViewById(R.id.tv_from_date);

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

//        TvFrom.setText(dayOfMonth+(monthOfYear+1)+year);
//        TvTo.setText(dayOfMonth+(monthOfYear+1)+year);
    }
}