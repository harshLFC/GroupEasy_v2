package example.com.groupeasy.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
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

    private EditText eventName;
    private EditText location;

    private TextView tvRangeLimit1,tvRangeLimit2;
    private TextView TvFrom;
    private TextView TvTo;
    private TextView saveBtn;

    private CrystalRangeSeekbar participantRangeBar;
    private ImageView ivClose;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);
        context = CreateNewListActivity.this;

        initElementsWithIds();
        initElementsWithListeners();
        changeTime();
        updateDisplay();
    }

    private void changeTime() {
            //code to receive data and change clock
        String Hours, Mins;

    }

    private void initElementsWithListeners() {

        participantRangeBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvRangeLimit1.setText(String.valueOf(minValue));
                tvRangeLimit2.setText(String.valueOf(maxValue));
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

        participantRangeBar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String EventName;
                EventName = eventName.getText().toString();

                //Code for form Validation
                if(EventName.isEmpty()){
                    Toast.makeText(context, "Please enter a Name for the event",Toast.LENGTH_LONG).show();
                }
                //push to firebase
                else {
                    Toast.makeText(context, EventName,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initElementsWithIds() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        participantRangeBar = (CrystalRangeSeekbar) findViewById(R.id.range_seekbar);
        tvRangeLimit1 = (TextView) findViewById(R.id.textMin1);
        tvRangeLimit2 = (TextView) findViewById(R.id.textMax1);
        location = (EditText) findViewById(R.id.location);
        eventName = (EditText) findViewById(R.id.eventName);
        TvFrom = (TextView) findViewById(R.id.tv_from_date);
        TvTo = (TextView) findViewById(R.id.tv_to_date);
        saveBtn = (TextView) findViewById(R.id.saveDetails);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,DashboardActivity.class);
        startActivity(intent);
        finish();
    }


   public void dateFrom(View v) {

        // code for opening calender
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "Date Picker");



    }

    private void updateDisplay() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Toast.makeText(this, year+""+month+""+day,Toast.LENGTH_LONG).show();


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

// class myOnDateChangedListener implements DatePicker.OnDateChangedListener{
//
////     EditText TvFrom = (EditText) findViewById(R.id.tv_from_date);
//
//    @Override
//    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
////        TvFrom.setText(dayOfMonth+(monthOfYear+1)+year);
////        TvTo.setText(dayOfMonth+(monthOfYear+1)+year);
//    }
//}