package sustech.unknown.channelx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import sustech.unknown.channelx.command.CheckChannelExistsOnFailureCommand;
import sustech.unknown.channelx.command.CheckChannelExistsOnSuccessCommand;
import sustech.unknown.channelx.command.CheckIfInChannelOnFailureCommand;
import sustech.unknown.channelx.command.CheckIfInChannelOnSuccessCommand;
import sustech.unknown.channelx.command.Command;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.util.ToastUtil;

public class JoinChannelActivity extends AppCompatActivity {

    private EditText editText;
    private String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_channel);

        initializeToolbar();
        initializeEditText();
    }

    private void initializeEditText() {
        editText = findViewById(R.id.editText);
    }

    private void initializeToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onJoinChannel(View view) {
       checkChannelExists(editText.getText().toString());
    }

    private void checkChannelExists(String channelId) {
        Command onSuccessCommand =
                new CheckChannelExistsOnSuccessCommand(this);
        Command onFailureCommand =
                new CheckChannelExistsOnFailureCommand(this);
        ChannelDao channelDao =
                new ChannelDao(onSuccessCommand, onFailureCommand);

        if (!channelId.trim().isEmpty()){
            this.channelId = channelId.trim();
            channelDao.joinChannel(this.channelId);
        } else {
            ToastUtil.makeToast(this, "Channel ID shouldn't be empty");
        }
    }


    public void channelNotExists() {
        ToastUtil.makeToast(this, "Channel doesn't exist!");
    }

    public void channelExists() {
        // ToastUtil.makeToast(this, "Channel does exist!");
        checkIfInChannel();
    }

    private void checkIfInChannel() {
        Command onSuccessCommand =
                new CheckIfInChannelOnSuccessCommand(this);
        Command onFailureCommand =
                new CheckIfInChannelOnFailureCommand(this);
        ChannelDao channelDao = new ChannelDao(onSuccessCommand, onFailureCommand);
        channelDao.checkInChannel(this.channelId, CurrentUser.getUser().getUid());
    }

    public void isInChannel() {
        ToastUtil.makeToast(this, "You are already in the channel!");
    }

    public void notInChannel() {
        ToastUtil.makeToast(this, "You are not in the channel!");
    }
}
