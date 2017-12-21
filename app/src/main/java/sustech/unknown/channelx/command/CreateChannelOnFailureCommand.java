package sustech.unknown.channelx.command;

import android.support.v7.app.AppCompatActivity;

import sustech.unknown.channelx.CreateChannelActivity2;

/**
 * Created by dahao on 2017/12/21.
 */

public class CreateChannelOnFailureCommand implements Command {

    private CreateChannelActivity2 activity2;

    public CreateChannelOnFailureCommand(CreateChannelActivity2 activity2) {
        this.activity2 = activity2;
    }

    @Override
    public void execute() {
        activity2.onFailure();
    }
}
