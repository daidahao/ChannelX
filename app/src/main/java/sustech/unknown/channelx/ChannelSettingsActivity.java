package sustech.unknown.channelx;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import sustech.unknown.channelx.command.LeaveChannelOnSuccessCommand;
import sustech.unknown.channelx.command.ReadChannelInterface;
import sustech.unknown.channelx.command.ReadChannelObjectCommand;
import sustech.unknown.channelx.command.ReadChannelOnFailureMessageCommand;
import sustech.unknown.channelx.command.ReadChannelOnSuccessMessageCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.dao.StorageDao;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.util.DateFormater;
import sustech.unknown.channelx.util.ToastUtil;

public class ChannelSettingsActivity extends AppCompatActivity implements ReadChannelInterface {


    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int QR_REQUEST = 0;
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
    private ImageView qrImage;
    private ImageView channelIcon;
    private TextView scheduleLabel;

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
        initializeQRImage();
        initializeChannelIcon();
        initializeSchedule();
    }

    private void initializeSchedule() {
        scheduleLabel = findViewById(R.id.schedule_text);
    }

    private void initializeChannelIcon() {
        channelIcon = findViewById(R.id.channelImageView);
        channelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri =data.getData();
                channelIcon.setImageURI(uri );
                StorageDao dao = new StorageDao();
                dao.uploadChannelPhoto(uri, getIntent().getStringExtra(Configuration.CHANNEL_KEY_MESSAGE));
                //dao.uplo

            }
        }
    }

    public void downloadIcon() throws IOException {
        //icon = findViewById(R.id.icon_image);
//        if(CurrentUser.isLogin()){
           StorageDao storageDao = new StorageDao();
           String a = getIntent().getStringExtra(Configuration.CHANNEL_KEY_MESSAGE);
            StorageReference storageReference = storageDao.downloadChannelImageByKey(getIntent().getStringExtra(Configuration.CHANNEL_KEY_MESSAGE));
//            // tempIcon = File.createTempFile("userTempIcon","jpg");
            final long FIVE_MEGABYTE = 5* 1024 * 1024;
            storageReference.getBytes(FIVE_MEGABYTE);
          //  storageReference.nu
         //   uri = Uri.fromFile(tempIcon);
            Glide.with(this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(channelIcon);
        }


    public  void  gallery() {

        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
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
    private void initializeQRImage() {
        qrImage = findViewById(R.id.QR_image);
        qrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ChannelSettingsActivity.this, QRActivity.class);
//                intent.putExtra("channelID",)
//                startActivityForResult(intent, QR_REQUEST);
                startQRActivity();
            }
        });
    }

    private void startQRActivity() {
        if (channel == null) {
            return;
        }
        Intent intent = new Intent(this, QRActivity.class);
        intent.putExtra(Configuration.CHANNEL_KEY_MESSAGE, channel.readKey());

        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        readChannelFromIntent(getIntent());
        try {
            downloadIcon();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        scheduleLabel.setText(
                DateFormater.minuteOfDayToString(channel.getOpenTimeInMinute()) +
                        " - " +
                        DateFormater.minuteOfDayToString(channel.getClosedTimeInMinute())
        );
    }

    private void startMoreMembersActivity() {
        if (channel == null) {
            return;
        }
        Intent intent = new Intent(this, MembersActivity.class);
        intent.putExtra(Configuration.CHANNEL_KEY_MESSAGE, channel.readKey());
        startActivity(intent);
    }
}
