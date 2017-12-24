package sustech.unknown.channelx.command;

import sustech.unknown.channelx.ChannelsActivity;
import sustech.unknown.channelx.model.Channel;

/**
 * Created by dahao on 2017/12/24.
 */

public class ReadChannelsListObjectCommand extends ObjectCommand<Channel> {

    private ChannelsActivity activity;

    public ReadChannelsListObjectCommand(ChannelsActivity activity) {
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
