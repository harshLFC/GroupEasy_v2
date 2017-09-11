package example.com.groupeasy.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import example.com.groupeasy.R;
import example.com.groupeasy.fragments.DatePickerFragment;
import example.com.groupeasy.fragments.TimePickerFragment;
import example.com.groupeasy.pojo.new_list;

/** Activity which creates new list */
@RequiresApi(api = Build.VERSION_CODES.N)
public class CreateNewListActivity extends AppCompatActivity {

    public static final String TAG = CreateNewListActivity.class.getSimpleName();
    private Context context;

    private EditText eventName;
    private EditText location;
    private TextView tvRangeLimit1,tvRangeLimit2;
    private TextView TvFrom, TvTo;
    private TextView timeFrom, timeTo;
    private TextView saveBtn;
    private TextView whatThisMeans;
    private ImageView ivClose;

    private CheckBox oneDayEvent, globalEvent;

    private CrystalRangeSeekbar participantRangeBar;

    final Calendar c = Calendar.getInstance();

    //database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_list);
        context = CreateNewListActivity.this;

        initElementsWithIds();
        initElementsWithListeners();
        updateDisplay();

    }

    private void initElementsWithListeners() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intitlize data
                String EventName, Location = "", minLimit = "", maxLimit = "", fromDATE = "", fromTIME="", toDATE="", toTIME="";

                // Aquire and convert data to string to prepare it for the push
                EventName = eventName.getText().toString();

                if(!location.toString().isEmpty()){
                    Location = location.getText().toString();
                }

                minLimit = tvRangeLimit1.getText().toString();
                maxLimit = tvRangeLimit2.getText().toString();

                //is enabled returns true if pressed
                oneDayEvent = (CheckBox) findViewById(R.id.one_day_event);
                globalEvent = (CheckBox) findViewById(R.id.global_event);

                fromDATE = TvFrom.getText().toString();
                fromTIME = timeFrom.getText().toString();
                toDATE = TvTo.getText().toString();
                toTIME = timeTo.getText().toString();

                //Code for form Validation
                if(EventName.isEmpty()){
                    Toast.makeText(context, "Please enter a Name for the event",Toast.LENGTH_LONG).show();
                }
                //push to firebase
                else {
                    mStorageRef = FirebaseStorage.getInstance().getReference();
                    final DatabaseReference groupRef = myRef.child("Events").child("lists").child("");

//                    new_list newList = new new_list("new poll","Dublin",10,20,false,"1238","92371","192837","92873",true);
                    new_list newList = new new_list(EventName,Location,minLimit,maxLimit,(oneDayEvent.isPressed()),fromDATE,fromTIME,toDATE,toTIME,(globalEvent.isPressed()));
                    groupRef.push().setValue(newList);


                }
            }
        });

        //Close Button
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Range bar
        participantRangeBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvRangeLimit1.setText(String.valueOf(minValue));
                tvRangeLimit2.setText(String.valueOf(maxValue));
            }
        });

        //What this means
        whatThisMeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNewListActivity.this);
                builder
                        .setMessage(R.string.Lorem_Ipsum_large)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
        whatThisMeans = (TextView) findViewById(R.id.see_what_this_means);
        timeFrom = (TextView) findViewById(R.id.tv_from_time);
        timeTo = (TextView) findViewById(R.id.tv_to_time);



    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,DashboardActivity.class);
        startActivity(intent);
        finish();
    }

//Initial update the TextFields from calender instance
    private void updateDisplay() {

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        TvFrom.setText(day+"/"+month+"/"+year);
        TvTo.setText(day+"/"+month+"/"+year);
    }

    // code for opening calender
   public void dateFrom(View v) {

    //old code
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getFragmentManager(), "Date Picker");

       new DatePickerDialog(this,d, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
   }

    //Code to update TimeFromm field by selected data
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                c.set(Calendar.YEAR, year);
                c.set((Calendar.MONTH)+1, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

             TvFrom.setText(dayOfMonth+"/"+(month+1)+"/"+year);
        }
    };

    // code for opening calender
    public void dateTo(View view) {

        new DatePickerDialog(this,e, c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
    }

    //Code to update TimeTO field by selected data
    DatePickerDialog.OnDateSetListener e = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            c.set(Calendar.YEAR, year);
            c.set((Calendar.MONTH)+1 , month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TvTo.setText(dayOfMonth+"/"+(month+1)+"/"+year);

        }
    };

    // code for opening clock
    public void timeFrom(View view) {

//this code did not work -       newFragment.show(getSupportFragmentManager(), "timePicker");
//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getFragmentManager(),"Time Picker");

        new TimePickerDialog(this,t, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true).show();

    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            timeFrom.setText(hourOfDay+":"+minute+"");

        }
    };

    // code for opening clock
    public void timeTo(View view) {

        new TimePickerDialog(this,u, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true).show();

    }

    TimePickerDialog.OnTimeSetListener u = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            timeTo.setText(hourOfDay+":"+minute + "");

        }
    };

}
