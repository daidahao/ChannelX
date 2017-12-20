package sustech.unknown.channelx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import sustech.unknown.channelx.util.DateFormater;

/**
 * Created by dahao on 2017/12/17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private int year, month, day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        Bundle bundle = getArguments();
        year = bundle.getInt("YEAR");
        month = bundle.getInt("MONTH");
        day = bundle.getInt("DAY");

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText dateText = getActivity().findViewById(R.id.dateText);

        if ((year < this.year) || (year == this.year && month < this.month) ||
                (year == this.year && month == this.month && dayOfMonth < this.day)) {
            dateText.setText(DateFormater.calendarToString());
        } else {
            dateText.setText(DateFormater.dateToString(year, month, dayOfMonth));
            // Calendar c = Calendar.getInstance();
            // c.set(year, month, dayOfMonth, 0, 0, 0);
            ((CreateChannelActivity2)getActivity())
                    .setDate(year, month, dayOfMonth);
        }
    }
}
