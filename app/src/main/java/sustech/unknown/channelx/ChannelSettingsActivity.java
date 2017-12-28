package sustech.unknown.channelx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import sustech.unknown.channelx.command.LeaveChannelOnSuccessCommand;
import sustech.unknown.channelx.command.ReadChannelInterface;
import sustech.unknown.channelx.command.ReadChannelObjectCommand;
import sustech.unknown.channelx.command.ReadChannelOnFailureMessageCommand;
import sustech.unknown.channelx.command.ReadChannelOnSuccessMessageCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.util.DateFormater;
import sustech.unknown.channelx.util.ToastUtil;

public class ChannelSettingsActivity extends AppCompatActivity implements ReadChannelInterface{

    private Toolbar toolbar;
    // private TextView toolbarTitle;
    private Channel channel;
    private TextView channelNameLabel;
    private TextView expiredDateLabel;
    private Switch anonymousSwitch;
    private TextView themeLabel;
    private TextView nicknameLabel;
    private TextView moreLabel;
    private Button leaveButton;
    private TextView creatorLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_settings);

        initializeToolbar();
        initializeChannelName();
        initializeExpiredDate();
        initializeCreator();
        initializeAnonymousSwitch();
        initializeTheme();
        initializeNickname();
        initializeMore();
        initializeButton();
    }

    private void initializeCreator() {
        creatorLabel = findViewById(R.id.creator_text);
    }

    public void onLeaveButton(View view) {
        // ToastUtil.makeToast(this, "You clicked the button!");
        LeaveChannelOnSuccessCommand onSuccessCommand =
                new LeaveChannelOnSuccessCommand(this);
        ChannelDao channelDao = new ChannelDao(onSuccessCommand, null);
        if (channel.getCreatorId().equals(CurrentUser.getUser().getUid())) {
            channelDao.destoryChannel(channel.readKey(), CurrentUser.getUser().getUid());
        } else {
            channelDao.leaveChannel(channel.readKey(), CurrentUser.getUser().getUid());
        }
    }

    public void onLeaveChannelSuccess(String message) {
        ToastUtil.makeToast(this, message);
        setResult(Configuration.RESULT_DESTROYED);
        finish();
    }

    private void initializeButton() {
        leaveButton = findViewById(R.id.leave_button);
        leaveButton.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

        readChannelFromIntent(getIntent());
    }

    private void initializeMore() {
        moreLabel = findViewById(R.id.more_text);
    }

    private void initializeNickname() {
        nicknameLabel = findViewById(R.id.nickname_text);
    }

    private void initializeTheme() {
        themeLabel = findViewById(R.id.theme_text);
    }

    private void initializeAnonymousSwitch() {
        anonymousSwitch = findViewById(R.id.anonymous_switch);
        anonymousSwitch.setEnabled(false);
    }

    private void initializeExpiredDate() {
        expiredDateLabel = findViewById(R.id.expired_date_text);
    }

    private void initializeChannelName() {
        channelNameLabel = findViewById(R.id.channel_name_label);
    }

    private void readChannelFromIntent(Intent intent) {
        ReadChannelOnSuccessMessageCommand onSuccessMessageCommand =
                new ReadChannelOnSuccessMessageCommand(this);
        ReadChannelOnFailureMessageCommand onFailureMessageCommand =
                new ReadChannelOnFailureMessageCommand(this);
        ReadChannelObjectCommand objectCommand =
                new ReadChannelObjectCommand(this);
        ChannelDao channelDao =
                new ChannelDao(onSuccessMessageCommand,
                        onFailureMessageCommand, objectCommand);
        if (intent.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE) == null) {
            onReadChannelFailure("CHANNEL ID cannot be empty");
            return;
        }
        channelDao.readChannel(intent.getStringExtra(Configuration.CHANNEL_KEY_MESSAGE));
    }

    private void initializeToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // toolbarTitle = findViewById(R.id.toolbar_title);
    }

    @Override
    public void onReadChannelSuccess(String message) {
        ToastUtil.makeToast(this, message);
    }

    @Override
    public void onReadChannelFailure(String message) {
        ToastUtil.makeToast(this, message);
        finish();
    }

    @Override
    public void onReadChannelObject(Channel channel) {
        this.channel = channel;
        if (channel == null) {
            return;
        }
        initializeChannel();
    }

    private void initializeChannel() {
        FirebaseUser currentUser = CurrentUser.getUser();
        channelNameLabel.setText(channel.getName());
        expiredDateLabel.setText(DateFormater.longToString(channel.getExpiredTime()));
        anonymousSwitch.setChecked(channel.isAnonymous());
        if (channel.getTheme() != null) {
            themeLabel.setText(channel.getTheme());
        }
        nicknameLabel.setText(channel.getMembers().get(currentUser.getUid()).getNickname());
        moreLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMoreMembersActivity();
            }
        });
        creatorLabel.setText(channel.getMembers().get(channel.getCreatorId()).getNickname());
        if (currentUser.getUid().equals(channel.getCreatorId())) {
            leaveButton.setText(getApplicationContext().getString(R.string.destroy_channel_text));
            creatorLabel.setText(getApplicationContext().getString(R.string.me));
        }
        leaveButton.setEnabled(!channel.isDestroyed());
    }

    private void startMoreMembersActivity() {
        ToastUtil.makeToast(this, "You clicked more!");
    }
}
