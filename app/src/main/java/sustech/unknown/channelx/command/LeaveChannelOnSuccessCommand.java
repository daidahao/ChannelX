package sustech.unknown.channelx.command;

import sustech.unknown.channelx.ChannelSettingsActivity;

/**
 * Created by dahao on 2017/12/28.
 */

public class LeaveChannelOnSuccessCommand extends MessageCommand {

    private ChannelSettingsActivity activity;


    public LeaveChannelOnSuccessCommand(ChannelSettingsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void execute() {
        if (activity == null) {
            return;
        }
        activity.onLeaveChannelSuccess(message);
    }
}
