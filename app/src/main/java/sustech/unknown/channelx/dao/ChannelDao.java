package sustech.unknown.channelx.dao;

import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import sustech.unknown.channelx.command.Command;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.model.DatabaseRoot;

/**
 * Created by dahao on 2017/12/20.
 */

public class ChannelDao {

    private final String channelKey = "channel";
    private final String membersKey = "members";
    private Command onSuccessCommand;
    private Command onFailureCommand;

    public ChannelDao() {

    }

    public ChannelDao(Command onSuccessCommand, Command onFailureCommand) {
        this.onSuccessCommand = onSuccessCommand;
        this.onFailureCommand = onFailureCommand;
    }


    private DatabaseReference getChannelRoot() {
        return DatabaseRoot.getRoot().child(channelKey);
    }

    private DatabaseReference getChannelChild(String channelId) {
        return getChannelRoot().child(channelId);
    }

    private DatabaseReference getChannelMembersChild(String channelId) {
        return getChannelChild(channelId).child(membersKey);
    }


    private DatabaseReference getChannelReference(String key) {
        return getChannelRoot().child(key);
    }

    public void createChannel(final Channel channel) {
        DatabaseReference channelChild = getChannelRoot().push();
        channel.writeKey(channelChild.getKey());
        addListenersForTask(channelChild.setValue(channel));
    }

    private void addListenersForTask(Task<Void> task) {
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
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

//    public void joinChannel(Channel channel) {
//        if (channel.getCreatorId().equals(
//                CurrentUser.getUser().getUid())){
//        }
//    }

    public void joinChannel(final String channelId) {
        getChannelRoot().addListenerForSingleValueEvent(
                new CheckExistsListener(channelId, onSuccessCommand, onFailureCommand)
        );
    }

    public void checkInChannel(final String channelId, final String userId) {
        getChannelMembersChild(channelId).addListenerForSingleValueEvent(
                new CheckExistsListener(userId, onSuccessCommand, onFailureCommand)
        );
    }


}

class CheckExistsListener implements ValueEventListener {

    private Command onSuccessCommand;
    private Command onFailureCommand;
    private String childKey;

    public CheckExistsListener(String childKey,
                               Command onSuccessCommand, Command onFailureCommand) {
        this.childKey = childKey;
        this.onSuccessCommand = onSuccessCommand;
        this.onFailureCommand = onFailureCommand;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChild(childKey)) {
            if (onSuccessCommand != null) {
                onSuccessCommand.execute();
            }
        } else {
            if (onFailureCommand != null) {
                onFailureCommand.execute();
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}


