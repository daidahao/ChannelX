package sustech.unknown.channelx.command;

import sustech.unknown.channelx.MainActivity;

/**
 * Created by dahao on 2017/12/22.
 */

public class JoinChannelOnFailureMessageCommand extends MessageCommand {

    private MainActivity activity;

    public JoinChannelOnFailureMessageCommand(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onFailure(message);
            }
        });
    }
}
