package sustech.unknown.channelx.command;

import sustech.unknown.channelx.CreateChannelActivity2;

/**
 * Created by dahao on 2017/12/21.
 */

public class CreateChannelOnSuccessCommand implements Command {

    private CreateChannelActivity2 activity2;

    public CreateChannelOnSuccessCommand(CreateChannelActivity2 activity2) {
        this.activity2 = activity2;
    }

    @Override
    public void execute() {
        activity2.onSuccess();
    }
}
