package sustech.unknown.channelx.command;

import sustech.unknown.channelx.JoinChannelActivity;

/**
 * Created by dahao on 2017/12/22.
 */

public class JoinChannelOnSuccessMessageCommand extends MessageCommand {

    private JoinChannelActivity activity;

    public JoinChannelOnSuccessMessageCommand(JoinChannelActivity activity) {
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
