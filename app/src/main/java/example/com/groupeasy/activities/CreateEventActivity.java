package example.com.groupeasy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;

import example.com.groupeasy.R;
import example.com.groupeasy.pojo.list_primary;
import example.com.groupeasy.pojo.list_details;

/** Activity which creates new list */
@RequiresApi(api = Build.VERSION_CODES.N)
public class CreateEventActivity extends AppCompatActivity {

    public static final String TAG = CreateEventActivity.class.getSimpleName();
    private Context context;

    private TextInputEditText eventName, eventDetails;
    private TextInputEditText location;
    private TextView tvRangeLimit1, tvRangeLimit2;
    private TextView TvFrom, TvTo;
    private TextView timeFrom, timeTo;
    private TextView saveBtn;
    private TextView popUpPublicEvent, popUpOneDayEvent;
    private ImageView ivClose, ImagetimeTo,ImagetimeFrom, ImageDateFrom, ImageDateTo;
    private String groupKey;
    private GoogleApiClient mGoogleApiClient;
    private LinearLayout LayouttimeTo,LayouttimeFrom, LayoutDateFrom, LayoutDateTo;

    private CheckBox oneDayEvent, globalEvent;

    private CrystalRangeSeekbar participantRangeBar;
    private ProgressDialog mRegProcess;

    final Calendar c = Calendar.getInstance();

    DatePickerDialog datePickerDialog;

    //database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_event);
        context = CreateEventActivity.this;

        initElementsWithIds();
        initElementsWithListeners();
        updateDisplay();
    }

    private void initElementsWithListeners() {

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //intitlize data
                 String EventName, EventDetails = null, Location = null, minLimit = null, maxLimit = null, fromDATE = null, fromTIME = null, toDATE = null, toTIME = null;

                // Aquire and convert data to string to prepare it for the push
                EventName = eventName.getText().toString();

                if (eventDetails.getText().toString().trim().length() > 0) {
                    EventDetails = eventDetails.getText().toString();
                }

                if (location.getText().toString().trim().length() > 0) {
                    Location = location.getText().toString();
                }

                if (tvRangeLimit1.getText().toString().trim().length() != 0) {
                    minLimit = tvRangeLimit1.getText().toString();
                }

                if (tvRangeLimit2.getText().toString().trim().length() != 100) {
                    maxLimit = tvRangeLimit2.getText().toString();
                }

                //is enabled returns true if pressed
                oneDayEvent = (CheckBox) findViewById(R.id.one_day_event);
                globalEvent = (CheckBox) findViewById(R.id.global_event);
                boolean one_day_event = oneDayEvent.isChecked();
                boolean global_event = globalEvent.isChecked();

                //the trick used is if textFields do not have default value
                // then the values are added to variable and pushed to server
                // Otherwise the data is null by default and not pushed

                if (!TvFrom.getText().toString().equals("Start date"))  {
                    fromDATE = TvFrom.getText().toString();
                }

                if (!timeFrom.getText().toString().equals("Start time"))  {
                    fromTIME = timeFrom.getText().toString();
                }

                if (!TvTo.getText().toString().equals("End date"))  {
                    toDATE = TvTo.getText().toString();
                }
                if (!timeTo.getText().toString().equals("End time"))  {
                    toTIME = timeTo.getText().toString();
                }

                //Code for form Validation
                if (EventName.isEmpty()) {
                    Toast.makeText(context, "Please enter a Name for the event", Toast.LENGTH_LONG).show();
                }
                //push to firebase
                else {
//                    if(TvFrom.getText().toString().equals("Start date")){
//                        Toast.makeText(CreateEventActivity.this, "You are creating an event without a start date",Toast.LENGTH_SHORT).show();
//
//                    }

                    groupKey = getIntent().getExtras().get("groupKey").toString();
                    final DatabaseReference groupRef = myRef.child("Events").child("lists").child(groupKey).child("");

                    final String finalEventDetails = EventDetails;
                    final String finalMinLimit = minLimit;
                    final String finalMaxLimit = maxLimit;
                    final String finalEventName = EventName;
                    final String finalLocation = Location;
                    final String finalFromDate = fromDATE;
                    final String finalFromTime = fromTIME;
                    final String finalToTime = toTIME;
                    final String finalToDate = toDATE;
                    final boolean finalOneDayEvent = one_day_event;
                    final boolean finalGlobalEvent = global_event;

                    String push_id = groupRef.push().getKey();

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

//                            code here
            /*        myRef.child("members").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String userName = dataSnapshot.getValue().toString();
//                            Log.w(userName, "thisIstheName");


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/

                    //push event created value for counter
                    myRef.child("members").child(uid).child("events_created").child(push_id).setValue(true);



                    list_primary listMain = new list_primary(finalEventName, uid, finalLocation, push_id, finalFromDate);
                    //send primary details
                    groupRef.child(push_id).setValue(listMain);
                    list_details newList = new list_details(finalEventDetails, finalMinLimit, finalMaxLimit, finalOneDayEvent, finalFromTime, finalToDate, finalToTime, finalGlobalEvent);
//                            list_details newList = new list_details(finalEventDetails, finalMinLimit, finalMaxLimit, finalOneDayEvent, finalFromDate, finalFromTime, finalToDate, finalToTime, finalGlobalEvent);

                    HashMap myMap = new HashMap();
                    myMap.put("details", finalEventDetails);
                    myMap.put("details", finalEventDetails);

                    //send extra details
                    groupRef.child(push_id).child("extra").setValue(newList);

                    //send participant details ?
//                            groupRef.child(push_id).child("participants").setValue(newList);

                    Toast.makeText(CreateEventActivity.this, "Event Created! ", Toast.LENGTH_SHORT).show();
                    finish();


           /*         groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long i = dataSnapshot.getChildrenCount();

                            Log.w(String.valueOf(i),"numOfCHildren");

//                            String push_id = String.valueOf(i+1);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });*/

                }
            }

        });

        //Close Button
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        //What this means popup TODO add real instructions
        popUpPublicEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
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

        //What this means popup TODO add real instructions
        popUpOneDayEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
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


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(context,LocationActivity.class);
//                startActivity(intent);
//                finish();

            }
        });

        /**The following codes hides keyboard when touched outside**/
        eventDetails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        eventName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void initElementsWithIds() {
        ivClose = (ImageView) findViewById(R.id.iv_close);
        participantRangeBar = (CrystalRangeSeekbar) findViewById(R.id.range_seekbar);

        tvRangeLimit1 = (TextView) findViewById(R.id.textMin1);
        tvRangeLimit2 = (TextView) findViewById(R.id.textMax1);
        location = (TextInputEditText) findViewById(R.id.location);
        eventName = (TextInputEditText) findViewById(R.id.eventName);
        eventDetails = (TextInputEditText) findViewById(R.id.event_details);
        TvFrom = (TextView) findViewById(R.id.tv_from_date);
        TvTo = (TextView) findViewById(R.id.tv_to_date);
        saveBtn = (TextView) findViewById(R.id.saveDetails);
        popUpPublicEvent = (TextView) findViewById(R.id.pop_up_public_event);
        popUpOneDayEvent = (TextView) findViewById(R.id.popup_one_day_event);
        timeFrom = (TextView) findViewById(R.id.tv_from_time);
        timeTo = (TextView) findViewById(R.id.tv_to_time);

        ImagetimeTo = (ImageView) findViewById(R.id.image_to_time);
        ImagetimeFrom = (ImageView) findViewById(R.id.image_time_from);
        ImageDateFrom = (ImageView) findViewById(R.id.image_date_from);
        ImageDateTo = (ImageView) findViewById(R.id.image_date_to);

        LayouttimeTo = (LinearLayout) findViewById(R.id.layout_to_time);
        LayouttimeFrom = (LinearLayout) findViewById(R.id.layout_from_time);
        LayoutDateFrom = (LinearLayout) findViewById(R.id.layout_from_date);
        LayoutDateTo = (LinearLayout) findViewById(R.id.layout_to_date);




    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //Initial update the TextFields from calender instance if want to
    private void updateDisplay() {

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

//        TvFrom.setText(day+"/"+month+"/"+year);
//        TvTo.setText(day+"/"+month+"/"+year);
    }

    // code for opening calender
    public void dateFrom(View v) {
        datePickerDialog = new DatePickerDialog(this, d, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        //so that user cannot pick previous date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    //Code to update Time From field by selected data
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            //gets the month in text, this is not returning selected month
            String month_name = month_date.format(c.getTime());

            c.set(Calendar.YEAR, year);
            c.set((Calendar.MONTH) + 1, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // Code to get current date and check if its before or after, but this only checks days of current month
            //also another BUG is if present day is selected it still posts the below toast
            Calendar currentDate;
            currentDate = Calendar.getInstance();

            long currentTime = Calendar.getInstance().getTimeInMillis();

            Log.w(String.valueOf(currentTime),"currentTIme");

//            currentDate.setTime(Date(currentTime));
            currentDate.setTime(new Date(currentTime));
            String test = currentDate.getTime().toString();

            //bug in this code
//            if(currentDate.after(c) | (currentDate.equals(c))) {
//                Toast.makeText(CreateEventActivity.this, "WARNING:You have selected date in the past", Toast.LENGTH_SHORT).show();
//            }

            TvFrom.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            TvFrom.setTextColor(Color.WHITE);
            ImageDateFrom.setColorFilter(Color.WHITE);
            LayoutDateFrom.setBackgroundResource(R.drawable.rounded_corner_primary_color);

//            TvFrom.setText(dayOfMonth+" "+month_name+" "+year);
            compareDate();

        }

    };
    private void compareDate() {

        /**THis method for comparing dates and alerting user if To date is more than From**/

        String Date1 = TvFrom.getText().toString();
        String Date2 = TvTo.getText().toString();

        if (!TvTo.getText().toString().equals("End date")) {

            String[] myDate1 = Date1.split("/");

            int day1 = Integer.parseInt(myDate1[0]); // 004
            int month1 = Integer.parseInt(myDate1[1]);
            int year1 = Integer.parseInt(myDate1[2]);

            String[] myDate2 = Date2.split("/");

            int day2 = Integer.parseInt(myDate2[0]); // 004
            int month2 = Integer.parseInt(myDate2[1]);
            int year2 = Integer.parseInt(myDate2[2]);

            if (year1 > year2) {
                Toast.makeText(CreateEventActivity.this, "Hey there time traveller, please select a valid date", Toast.LENGTH_SHORT).show();
                revertTV();

            } else if (year2 == year1) {
                if (month1 > month2) {
                    Toast.makeText(CreateEventActivity.this, "Hey there time traveller, please select a valid date", Toast.LENGTH_SHORT).show();
                    revertTV();
                }
                if (month2 == month1) {
                    if (day1 > day2) {
                        Toast.makeText(CreateEventActivity.this, "Hey there time traveller, please select a valid date", Toast.LENGTH_SHORT).show();
                        revertTV();

                    }
                }
            }
        }
                /*else{
                    revertTV();
                }*/

    }

    private void revertTV() {
        TvFrom.setText("Start date");
        TvFrom.setTextColor(Color.GRAY);
        ImageDateFrom.setColorFilter(Color.GRAY);
        LayoutDateFrom.setBackgroundResource(R.drawable.rounded_corners_shape);
    }


    // code for opening calender
    public void dateTo(View view) {

        DatePickerDialog datePickerTo = new DatePickerDialog(this, e, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        //so that user cannot pick previous date

//        datePickerTo.getDatePicker().setMinDate(datePickerDialog.getDatePicker().getDayOfMonth());
        datePickerTo.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerTo.show();

    }

    //Code to update TimeTO field by selected data
    DatePickerDialog.OnDateSetListener e = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            c.set(Calendar.YEAR, year);
            c.set((Calendar.MONTH) + 1, month);
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TvTo.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            TvTo.setTextColor(Color.WHITE);
            ImageDateTo.setColorFilter(Color.WHITE);
            LayoutDateTo.setBackgroundResource(R.drawable.rounded_corner_secondary_color);


            compareToDate(year,month,dayOfMonth);
        }


        void compareToDate(int year, int month, int dayOfMonth) {
            /**THis method for comparing dates and alerting user if To date is more than From**/

            Log.w(TvFrom.getText().toString(), "DateIssue");
            Log.w(TvTo.getText().toString(), "DateIssue");

            String Date1 = TvFrom.getText().toString();
            String Date2 = TvTo.getText().toString();

            if (TvFrom.getText().toString().equals("Start date")) {

                Toast.makeText(CreateEventActivity.this, "Make sure you select a start date", Toast.LENGTH_SHORT).show();
                revertTV();

            } else {

                String[] myDate1 = Date1.split("/");

                int day1 = Integer.parseInt(myDate1[0]); // 004
                int month1 = Integer.parseInt(myDate1[1]);
                int year1 = Integer.parseInt(myDate1[2]);

                String[] myDate2 = Date2.split("/");

                int day2 = Integer.parseInt(myDate2[0]); // 004
                int month2 = Integer.parseInt(myDate2[1]);
                int year2 = Integer.parseInt(myDate2[2]);

                if (year2 < year1) {
                    Toast.makeText(CreateEventActivity.this, "You have selected a date in the past for end date", Toast.LENGTH_SHORT).show();
                    revertTV();


                } else if (year2 == year1) {

                    if (month2 < month1) {
                        Toast.makeText(CreateEventActivity.this, "You have selected a date in the past for end date", Toast.LENGTH_SHORT).show();
                        revertTV();

                    }

                    if (month2 == month1) {
                        if (day2 < day1) {
                            Toast.makeText(CreateEventActivity.this, "You have selected a date in the past for end date", Toast.LENGTH_SHORT).show();
                            revertTV();

                        }
                    }
                }
                else{
                    revertTV();
                }
            }
        }

        private void revertTV() {
            TvTo.setText("End date");
            TvTo.setTextColor(Color.GRAY);
            ImageDateTo.setColorFilter(Color.GRAY);
            LayoutDateTo.setBackgroundResource(R.drawable.rounded_corners_shape);
        }

    };

    // code for opening clock
    public void timeFrom(View view) {
        new TimePickerDialog(this, t, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
    }

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            timeFrom.setText(convertTo12H(hourOfDay) + ":" + zeroBeforeMin(minute) + " " + isAM(hourOfDay));
            timeFrom.setTextColor(Color.WHITE);
            ImagetimeFrom.setColorFilter(Color.WHITE);
            LayouttimeFrom.setBackgroundResource(R.drawable.rounded_corner_primary_color);
        }
    };

    // code for opening clock
    public void timeTo(View view) {
        new TimePickerDialog(this, u, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
    }

    //code for setting text field with
    TimePickerDialog.OnTimeSetListener u = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            //calling the respective methods and displaying user readable time data
            timeTo.setText(convertTo12H(hourOfDay) + ":" + zeroBeforeMin(minute) + " " + isAM(hourOfDay));

            //change ui
            timeTo.setTextColor(Color.WHITE);
            ImagetimeTo.setColorFilter(Color.argb(255, 255, 255, 255));
            LayouttimeTo.setBackgroundResource(R.drawable.rounded_corner_secondary_color);
        }
    };

    //display am/pm according to time chosen
    private String isAM(int hourOfDay) {
        if (hourOfDay > 12) return "PM";
        else return "AM";
            }

    //if time chosen < 9 add 0 before
    private String zeroBeforeMin(int minute) {
        if (minute < 10) return "0" + minute;
        else
        return String.valueOf(minute);
    }

    //Converts 24H values to 12Hours
    private String convertTo12H(int hour) {
        if (hour > 12) return String.valueOf(hour -= 12);
        else if (hour == 0) return String.valueOf(hour += 12);
        else return String.valueOf(hour);
    }

/*    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }*/
    }
