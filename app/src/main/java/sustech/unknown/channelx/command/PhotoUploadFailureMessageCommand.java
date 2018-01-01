package sustech.unknown.channelx.command;

import sustech.unknown.channelx.MainActivity;

/**
 * Created by Shen on 2017/12/27.
 */

public class PhotoUploadFailureMessageCommand implements Command{

    MainActivity mainActivity;

    public PhotoUploadFailureMessageCommand(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void execute() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.onFailure();
            }
        });
    }
}
