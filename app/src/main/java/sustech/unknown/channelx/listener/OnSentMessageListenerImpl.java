package sustech.unknown.channelx.listener;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

/**
 * Created by dahao on 2017/12/16.
 */

public class OnSentMessageListenerImpl implements ChatView.OnSentMessageListener {

    private DatabaseReference messagesReference;
    private ChatView chatView;

    public OnSentMessageListenerImpl() {

    }

    public OnSentMessageListenerImpl(DatabaseReference messagesReference, ChatView chatView){
        this.messagesReference = messagesReference;
        this.chatView = chatView;
    }

    @Override
    public boolean sendMessage(ChatMessage chatMessage) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // 将消息添加至message中
        chatMessage.setUserid(user.getUid());
        chatMessage.setNickname(user.getDisplayName());
        DatabaseReference child = messagesReference.push();
        // 暂时禁用输入框
        chatView.disableInput();
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
}
