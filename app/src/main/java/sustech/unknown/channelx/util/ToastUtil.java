package sustech.unknown.channelx.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by dahao on 2017/12/20.
 */

public class ToastUtil {
    public static void makeToast(Context context,
                                 String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
