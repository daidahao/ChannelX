package sustech.unknown.channelx.command;

import sustech.unknown.channelx.ChatActivity;

/**
 * Created by dahao on 2017/12/22.
 */

public class ReadChannelOnSuccessMessageCommand extends MessageCommand {

    private ChatActivity activity;

    public ReadChannelOnSuccessMessageCommand(ChatActivity activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.onReadChannelSuccess(message);
            }
        });
    }
}
