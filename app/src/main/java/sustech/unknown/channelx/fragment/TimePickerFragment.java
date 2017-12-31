package sustech.unknown.channelx.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;


import sustech.unknown.channelx.Configuration;
import sustech.unknown.channelx.CreateChannelActivity2;

/**
 * Created by dahao on 2017/12/31.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int hourOfDay;
    private int minute;
    private boolean open;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        this.open = bundle.getBoolean(Configuration.OPEN);
        this.hourOfDay = bundle.getInt(Configuration.HOUR_OF_DAY);
        this.minute = bundle.getInt(Configuration.MINUTE);

        return new TimePickerDialog(getActivity(), this,
                hourOfDay, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        CreateChannelActivity2 activity2 = (CreateChannelActivity2) getActivity();
        activity2.setTime(open, hourOfDay, minute);

    }
}
