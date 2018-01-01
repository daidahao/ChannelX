package sustech.unknown.channelx.command;

import sustech.unknown.channelx.MainActivity;
import sustech.unknown.channelx.model.Channel;

/**
 * Created by dahao on 2017/12/24.
 */

public class ReadChannelsListAddObjectCommand extends ObjectCommand<Channel> {

    private MainActivity activity;

    public ReadChannelsListAddObjectCommand(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.addChannel(object);
            }
        });
    }
}
