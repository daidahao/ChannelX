package sustech.unknown.channelx.command;

import android.app.Activity;

import sustech.unknown.channelx.ChannelsActivity;

/**
 * Created by Shen on 2017/12/27.
 */

public class PhotoUploadFailureMessageCommand implements Command{

    ChannelsActivity channelsActivity;

    public PhotoUploadFailureMessageCommand(ChannelsActivity channelsActivity) {
        this.channelsActivity = channelsActivity;
    }

    @Override
    public void execute() {
        channelsActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                channelsActivity.onFailure();
            }
        });
    }
}
