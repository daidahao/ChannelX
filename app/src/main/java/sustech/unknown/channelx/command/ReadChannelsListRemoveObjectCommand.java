package sustech.unknown.channelx.command;

import sustech.unknown.channelx.ChannelsActivity;
import sustech.unknown.channelx.model.Channel;

/**
 * Created by dahao on 2017/12/27.
 */

public class ReadChannelsListRemoveObjectCommand extends ObjectCommand<Channel> {

    private ChannelsActivity activity;

    public ReadChannelsListRemoveObjectCommand(ChannelsActivity activity) {
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
