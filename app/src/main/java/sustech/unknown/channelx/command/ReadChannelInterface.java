package sustech.unknown.channelx.command;

import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.util.ToastUtil;

/**
 * Created by dahao on 2017/12/28.
 */

public interface ReadChannelInterface {

    void onReadChannelSuccess(String message);
    void onReadChannelFailure(String message);
    void onReadChannelObject(Channel channel);
    void runOnUiThread(Runnable runnable);

}
