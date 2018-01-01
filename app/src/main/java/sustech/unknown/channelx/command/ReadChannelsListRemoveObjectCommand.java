package sustech.unknown.channelx.command;

import sustech.unknown.channelx.MainActivity;
import sustech.unknown.channelx.model.Channel;

/**
 * Created by dahao on 2017/12/27.
 */

public class ReadChannelsListRemoveObjectCommand extends ObjectCommand<Channel> {

    private MainActivity activity;

    public ReadChannelsListRemoveObjectCommand(MainActivity activity) {
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
                activity.removeChannel(object);
            }
        });
    }
}
