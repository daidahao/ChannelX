package sustech.unknown.channelx;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.github.xudaojie.qrcodelib.CaptureActivity;
import sustech.unknown.channelx.command.JoinChannelOnFailureMessageCommand;
import sustech.unknown.channelx.command.JoinChannelOnSuccessMessageCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.util.ToastUtil;

public class JoinChannelActivity extends AppCompatActivity {

    private static final int REQUEST_QR_CODE = 1;
    private EditText editText;
    private String channelId;
    private SurfaceView mySurfaceView;
    //private QREader qrEader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_channel);



        initializeToolbar();
        initializeEditText();
        // Setup SurfaceView
        // -----------------


        // Init QREader
        // ------------

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
//        Intent intent = new Intent(this, DecoderActivity.class);
//        startActivityForResult(intent, 999);

        checkChannelExists(editText.getText().toString());
    }

    public void onJoinChannelByScan(View view) {
        Intent i = new Intent(this, CaptureActivity.class);
       // i.putExtra("channelID", "");
        this.startActivityForResult(i, REQUEST_QR_CODE);

        //checkChannelExists(editText.getText().toString());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK
                && requestCode == REQUEST_QR_CODE
                && data != null) {
            String result = data.getStringExtra("result");
            editText.setText(result);
            checkChannelExists(editText.getText().toString());
            //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();

        }
    }

    private void checkChannelExists(String channelId) {
        JoinChannelOnSuccessMessageCommand onSuccessMessageCommand =
                new JoinChannelOnSuccessMessageCommand(this);
        JoinChannelOnFailureMessageCommand onFailureMessageCommand =
                new JoinChannelOnFailureMessageCommand(this);
        ChannelDao channelDao =
                new ChannelDao(onSuccessMessageCommand, onFailureMessageCommand);

        if (!channelId.trim().isEmpty()){
            this.channelId = channelId.trim();
            // channelDao.joinChannel(this.channelId);
            String contactInfo = null;
            if (CurrentUser.getUser().getEmail() != null) {
                contactInfo = CurrentUser.getUser().getEmail();
            }
            if (CurrentUser.getUser().getPhoneNumber() != null) {
                contactInfo = CurrentUser.getUser().getPhoneNumber();
            }
            channelDao.joinChannel(this.channelId,
                    CurrentUser.getUser().getUid(),
                    CurrentUser.getUser().getDisplayName(),
                    contactInfo);
        } else {
            ToastUtil.makeToast(this, "Channel ID shouldn't be empty");
        }
    }

    public void onSuccess(String message) {
        ToastUtil.makeToast(this, message);
        Intent intent = new Intent();
        intent.putExtra(Configuration.CHANNEL_KEY_MESSAGE, this.channelId);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onFailure(String message) {
        ToastUtil.makeToast(this, message);
        setResult(RESULT_CANCELED);
        finish();

    }

}
