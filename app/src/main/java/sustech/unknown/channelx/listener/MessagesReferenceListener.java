package sustech.unknown.channelx.listener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

/**
 * Created by dahao on 2017/12/15.
 */

public class MessagesReferenceListener implements ChildEventListener {

    private FirebaseUser user;
    private ChatView chatView;

    public MessagesReferenceListener() {}

    public MessagesReferenceListener(ChatView chatView){
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.chatView = chatView;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
        if (message.getUserid().equals(user.getUid())){
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
}
