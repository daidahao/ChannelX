package sustech.unknown.channelx;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by dahao on 2017/12/17.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    public static String dateTextKey = "dateTextKey";
    private int year, month, day;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
//        int year = 2018;
//        int month = 0;
//        int day = 1;
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        EditText dateText = getActivity().findViewById(R.id.dateText);

        if ((year < this.year) || (year == this.year && month < this.month) ||
                (year == this.year && month == this.month && dayOfMonth < this.day)) {
            dateText.setText(this.year + "-" + (this.month + 1) + "-" + this.day);
        } else {
            dateText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
        }
    }
}
