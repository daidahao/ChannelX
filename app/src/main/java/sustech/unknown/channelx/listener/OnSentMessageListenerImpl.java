package sustech.unknown.channelx.listener;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import sustech.unknown.channelx.command.SendMessageOnFailureCommand;
import sustech.unknown.channelx.command.SendMessageOnSuccessCommand;
import sustech.unknown.channelx.dao.MessagesDao;

/**
 * Created by dahao on 2017/12/16.
 */

public class OnSentMessageListenerImpl implements ChatView.OnSentMessageListener {

    private ChatView chatView;
    private MessagesDao messagesDao;

    public OnSentMessageListenerImpl(MessagesDao messagesDao, ChatView chatView) {
        this.chatView = chatView;
        this.messagesDao = messagesDao;
    }

    @Override
    public boolean sendMessage(ChatMessage chatMessage) {

        // 将消息添加至message中
        // chatMessage.setUserid(userId);
        // chatMessage.setNickname(user.getDisplayName());
        // DatabaseReference child = messagesReference.push();

        // 暂时禁用输入框
        chatView.disableInput();
        // 在写入成功时触发监听器，清楚输入框的内容
        messagesDao.setOnSuccessCommand(new SendMessageOnSuccessCommand(chatView));
        messagesDao.setOnFailureCommand(new SendMessageOnFailureCommand(chatView));
        messagesDao.addMessage(chatMessage);
//        child.setValue(chatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                chatView.clearInput();
//            }
//        });
        // Log.d("chatView", "sending message");
        return true;
    }
}
