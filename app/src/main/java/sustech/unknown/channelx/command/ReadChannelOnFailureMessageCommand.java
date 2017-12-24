package sustech.unknown.channelx.command;

import sustech.unknown.channelx.ChatActivity;

/**
 * Created by dahao on 2017/12/22.
 */

public class ReadChannelOnFailureMessageCommand extends MessageCommand {

    private ChatActivity activity;

    public ReadChannelOnFailureMessageCommand(ChatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onReadChannelFailure(message);
            }
        });
    }
}
