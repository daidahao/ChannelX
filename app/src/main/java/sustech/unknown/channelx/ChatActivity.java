package sustech.unknown.channelx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.intentservice.chatui.ChatView;
import sustech.unknown.channelx.command.ReadChannelOnFailureMessageCommand;
import sustech.unknown.channelx.command.ReadChannelOnSuccessMessageCommand;
import sustech.unknown.channelx.dao.ChannelDao;
import sustech.unknown.channelx.listener.MessagesReferenceListener;
import sustech.unknown.channelx.listener.OnSentMessageListenerImpl;
import sustech.unknown.channelx.listener.TypingListenerImpl;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.DatabaseRoot;
import sustech.unknown.channelx.util.ToastUtil;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference messagesReference;

    private ChatView chatView;

    private Channel channel;

    public static final int ENTER_CHANNEL_REQUEST = 111;

    public static final String CHANNEL_KEY_MESSAGE =
            "sustech.unknown.channelx.ChatActivity.CHANNEL_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        initializeChatView();

//        initializeToolbar(intent);
//
//        messagesReference = getMessagesReference(intent);

//
//        // 增加message的监听器，可以在启动时加载ChatView，且在有新聊天消息时更新ChatView
//        messagesReference.addChildEventListener(new MessagesReferenceListener(chatView));
//        // 在发送消息时触发该监听器
//        chatView.setOnSentMessageListener(
//                new OnSentMessageListenerImpl(messagesReference, chatView));

        readChannelFromIntent(getIntent());


    }

    private void initializeChatView() {
        chatView = findViewById(R.id.chat_view);
        chatView.disableInput();
        chatView.setTypingListener(new TypingListenerImpl());
    }

    private void readChannelFromIntent(Intent intent) {
        ReadChannelOnSuccessMessageCommand onSuccessMessageCommand =
                new ReadChannelOnSuccessMessageCommand(this);
        ReadChannelOnFailureMessageCommand onFailureMessageCommand =
                new ReadChannelOnFailureMessageCommand(this);
        ChannelDao channelDao = new ChannelDao(onSuccessMessageCommand, onFailureMessageCommand);
        if (intent.getStringExtra(CHANNEL_KEY_MESSAGE) == null) {
            onReadChannelFailure("CHANNEL ID cannot be empty");
            return;
        }
        channelDao.readChannel(intent.getStringExtra(CHANNEL_KEY_MESSAGE), channel);
    }

    public void onReadChannelSuccess(String message) {
        ToastUtil.makeToast(this, message);
    }

    public void onReadChannelFailure(String message) {
        ToastUtil.makeToast(this, message);
        setResult(RESULT_CANCELED);
        finish();
    }

    private void initializeToolbar(Intent intent) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(intent.getStringExtra(ChannelsActivity.CHANNEL_NAME_MESSAGE));

    }

    private String getChannelKey(Intent intent) {
        return intent.getStringExtra(CHANNEL_KEY_MESSAGE);
    }

    private DatabaseReference getMessagesReference(Intent intent) {
        return DatabaseRoot.getRoot()
                .child("channel")
                .child(getChannelKey(intent))
                .child("messages");
    }

}
