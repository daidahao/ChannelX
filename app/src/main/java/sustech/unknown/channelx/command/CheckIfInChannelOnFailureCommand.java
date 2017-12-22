package sustech.unknown.channelx.command;

import sustech.unknown.channelx.JoinChannelActivity;

/**
 * Created by dahao on 2017/12/22.
 */

public class CheckIfInChannelOnFailureCommand implements Command {

    private JoinChannelActivity activity;

    public CheckIfInChannelOnFailureCommand(JoinChannelActivity activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        activity.notInChannel();
    }
}
