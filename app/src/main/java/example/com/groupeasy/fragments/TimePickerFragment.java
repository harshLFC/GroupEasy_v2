package example.com.groupeasy.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import example.com.groupeasy.activities.CreateNewListActivity;


public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Intent intent = new Intent(getActivity(), CreateNewListActivity.class);
        int hour,min;
        final Calendar c = Calendar.getInstance();
        min = c.get(Calendar.MINUTE);
        hour = c.get(Calendar.HOUR_OF_DAY);

   //     intent.putExtra(hour,min);
        startActivity(intent);

    }

    public void onTimeChanged (TimePicker view, int hourOfDay, int minute){

    }



}


class myOnDateChangedListener implements DatePicker.OnDateChangedListener{

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        DatePicker myDatePicker = (DatePicker) findViewById(R.id.mydatepicker);
//        String selectedDate = DateFormat.getDateInstance().format(myDatePicker.getCalendarView().getDate());
    }
}