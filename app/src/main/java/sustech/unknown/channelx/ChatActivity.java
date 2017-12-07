package sustech.unknown.channelx;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mMessageReference;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarTitle.setText(intent.getStringExtra(MainActivity.CHANNEL_MESSAGE));

        // 获取当前登陆的用户
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        // 初始化数据库
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mMessageReference = mDatabase.child("message");

        final ChatView chatView = (ChatView) findViewById(R.id.chat_view);

        // 增加message的监听器，可以在启动时加载ChatView，且在有新聊天消息时更新ChatView
        mMessageReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                if (message.getUserid().equals(mUser.getUid())){
                    message.setType(ChatMessage.Type.SENT);
                }
                else {
                    message.setType(ChatMessage.Type.RECEIVED);
                }
                chatView.addMessage(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        // 在发送消息时触发该监听器
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                // 将消息添加至message中
                chatMessage.setUserid(mUser.getUid());
                DatabaseReference child = mMessageReference.push();
                // 在写入成功时触发监听器，清楚输入框的内容
                child.setValue(chatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        chatView.clearInput();
                    }
                });
                Log.d("chatView", "sending message");
                return true;
            }
        });

        chatView.setTypingListener(new ChatView.TypingListener() {
            @Override
            public void userStartedTyping() {

            }

            @Override
            public void userStoppedTyping() {

            }
        });

    }
}
