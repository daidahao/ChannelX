package sustech.unknown.channelx.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dahao on 2017/12/20.
 */

public class DateFormater {

    public static String dateToString(int year, int month, int mday) {
        return (year + "-" + (month + 1) + "-" + mday);
    }

    public static String calendarToString() {
        Calendar calendar = Calendar.getInstance();
        return  dateToString(
                calendar.get(calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(calendar.DAY_OF_MONTH));
    }

}
