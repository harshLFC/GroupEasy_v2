package example.com.groupeasy.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.DatePicker;


public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    int yearFinal, monthFinal, dayFinal;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Do something with the date chosen by the user
        yearFinal = year;
        monthFinal = month +1;
        dayFinal = dayOfMonth;

        updateDisplay();
    }

    private void updateDisplay() {

    }

    class myOnDateChangedListener implements DatePicker.OnDateChangedListener{

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        DatePicker myDatePicker = (DatePicker) findViewById(R.id.mydatepicker);
//        String selectedDate = DateFormat.getDateInstance().format(myDatePicker.getCalendarView().getDate());
        }
    }

}
