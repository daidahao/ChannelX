package sustech.unknown.channelx.command;

import sustech.unknown.channelx.ChatActivity;
import sustech.unknown.channelx.model.Channel;

/**
 * Created by dahao on 2017/12/22.
 */

public class ReadChannelObjectCommand extends ObjectCommand<Channel> {

    // private ChatActivity activity;
    private ReadChannelInterface activity;

    public ReadChannelObjectCommand(ReadChannelInterface activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onReadChannelObject(object);
            }
        });
    }
}
