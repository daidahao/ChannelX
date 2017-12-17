package sustech.unknown.channelx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.intentservice.chatui.ChatView;
import sustech.unknown.channelx.listener.MessagesReferenceListener;
import sustech.unknown.channelx.listener.OnSentMessageListenerImpl;
import sustech.unknown.channelx.listener.TypingListenerImpl;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference messagesReference;

    private ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        initializeToolbar(intent);

        messagesReference = getMessagesReference(intent);
        chatView = (ChatView) findViewById(R.id.chat_view);

        // 增加message的监听器，可以在启动时加载ChatView，且在有新聊天消息时更新ChatView
        messagesReference.addChildEventListener(new MessagesReferenceListener(chatView));
        // 在发送消息时触发该监听器
        chatView.setOnSentMessageListener(
                new OnSentMessageListenerImpl(messagesReference, chatView));

        chatView.setTypingListener(new TypingListenerImpl());

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
        return intent.getStringExtra(ChannelsActivity.CHANNEL_KEY_MESSAGE);
    }

    private DatabaseReference getMessagesReference(Intent intent) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference()
                .child("channel")
                .child(getChannelKey(intent))
                .child("messages");
    }

}
