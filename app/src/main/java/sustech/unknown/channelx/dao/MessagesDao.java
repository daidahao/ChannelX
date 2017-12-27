package sustech.unknown.channelx.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import co.intentservice.chatui.models.ChatMessage;
import sustech.unknown.channelx.Configuration;
import sustech.unknown.channelx.command.Command;
import sustech.unknown.channelx.command.NoCommand;
import sustech.unknown.channelx.command.ObjectCommand;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.DatabaseRoot;

/**
 * Created by dahao on 2017/12/22.
 */

public class MessagesDao {

    private Channel channel;
    private String userId;
    private ObjectCommand<ChatMessage> chatMessageObjectCommand;
    private Command onSuccessCommand = new NoCommand();
    private Command onFailureCommand = new NoCommand();


    public MessagesDao(Channel channel, String userId) {
        this.channel = channel;
        this.userId = userId;
    }

    DatabaseReference getMessagesNode() {
        return DatabaseRoot.getRoot()
                .child(Configuration.channelKey)
                .child(channel.readKey())
                .child(Configuration.messagesKey);

    }

    public void addMessage(ChatMessage chatMessage) {
        DatabaseReference messagesNode = getMessagesNode();
        chatMessage.setUserid(userId);
        // chatMessage.setNickname(channel.getMembers().get(userId).getNickname());
        DatabaseReference childNode = messagesNode.push();
        childNode.setValue(chatMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if (onSuccessCommand != null) {
                    onSuccessCommand.execute();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (onFailureCommand != null) {
                    onFailureCommand.execute();
                }
            }
        });
    }

    public ObjectCommand<ChatMessage> getChatMessageObjectCommand() {
        return chatMessageObjectCommand;
    }

    public void setChatMessageObjectCommand(ObjectCommand<ChatMessage> chatMessageObjectCommand) {
        this.chatMessageObjectCommand = chatMessageObjectCommand;
    }

    private void sendChatMessageObject(ChatMessage chatMessage) {
        if (chatMessageObjectCommand != null) {
            chatMessageObjectCommand.setObject(chatMessage);
            chatMessageObjectCommand.execute();
        }
    }

    public void addListenerForChatMessage() {
        getMessagesNode().addChildEventListener(new MessagesChildEventListener());
    }

    public Command getOnSuccessCommand() {
        return onSuccessCommand;
    }

    public void setOnSuccessCommand(Command onSuccessCommand) {
        this.onSuccessCommand = onSuccessCommand;
    }

    public Command getOnFailureCommand() {
        return onFailureCommand;
    }

    public void setOnFailureCommand(Command onFailureCommand) {
        this.onFailureCommand = onFailureCommand;
    }

    class MessagesChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
            if (chatMessage == null) {
                return;
            }
            Log.d("chatMessage", chatMessage.getMessage());
            chatMessage.setNickname(
                    channel.getMembers().get(
                            chatMessage.getUserid()
                    ).getNickname()
            );
            if (chatMessage.getUserid().equals(userId)){
                chatMessage.setType(ChatMessage.Type.SENT);
            }
            else {
                chatMessage.setType(ChatMessage.Type.RECEIVED);
            }
            sendChatMessageObject(chatMessage);
        }
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {}
        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        @Override
        public void onCancelled(DatabaseError databaseError) {}
    }
}


