package sustech.unknown.channelx.command;

import android.app.Activity;

import sustech.unknown.channelx.ChannelsActivity;
import sustech.unknown.channelx.JoinChannelActivity;

/**
 * Created by dahao on 2017/12/22.
 */

public class JoinChannelOnSuccessMessageCommand extends MessageCommand {

    private ChannelsActivity activity;

    public JoinChannelOnSuccessMessageCommand(ChannelsActivity activity) {
        this.activity = activity;
    }


    @Override
    public void execute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onSuccess(message);
            }
        });
    }
}
