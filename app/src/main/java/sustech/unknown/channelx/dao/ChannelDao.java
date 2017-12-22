package sustech.unknown.channelx.dao;

import android.provider.Settings;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import sustech.unknown.channelx.command.Command;
import sustech.unknown.channelx.command.MessageCommand;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.model.DatabaseRoot;
import sustech.unknown.channelx.model.Member;

/**
 * Created by dahao on 2017/12/20.
 */

public class ChannelDao {

    private final String channelKey = "channel";
    private final String membersKey = "members";
    private final String themeKey = "theme";
    private Command onSuccessCommand;
    private Command onFailureCommand;

    public ChannelDao() {

    }

    public ChannelDao(Command onSuccessCommand, Command onFailureCommand) {
        this.onSuccessCommand = onSuccessCommand;
        this.onFailureCommand = onFailureCommand;
    }

    public ChannelDao(MessageCommand onSuccessCommand, Command onFailureCommand) {
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

    public void joinChannel(String channelId, final String userId, final String trueName) {
        // getChannelChild("123").runTransaction();
        getChannelChild(channelId).runTransaction(
                new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Channel channel = mutableData.getValue(Channel.class);
                Member member = null;
                if (channel == null) {
                    Log.d("joinChannel()", "(Possibly) The channel doesn't exist!");
                    // sendFailureMessage("The channel doesn't exist!");
                    return Transaction.success(mutableData);
                }
                if (channel.getMembers().containsKey(userId)) {
                    Log.d("joinChannel()", "You are already in the channel!");
                    sendSuccessMessage("You are already in the channel!");
                    return Transaction.success(mutableData);
                } else if (channel.isAnonymous()) {
                    if (channel.getThemeList().size() <= channel.getMemberCount()) {
                        Log.d("joinChannel()", "The channel is already full!");
                        sendFailureMessage("The channel is already full!");
                        return Transaction.success(mutableData);
                    } else {
                        // Log.d("ChannelDao", channel.getThemeList().get("001"));
                        member = new Member(
                                channel.getThemeList().get(
                                        String.format("%03d", channel.getMemberCount() + 1)));
                    }
                } else {
                    member = new Member(trueName);
                }
                Log.d("joinChannel()", "You're added into the member list!");
                sendSuccessMessage("You're added into the member list!");
                channel.getMembers().put(userId, member);
                channel.setMemberCount(channel.getMemberCount() + 1);
                mutableData.setValue(channel);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (!b) {
                    sendFailureMessage("Error! Cannot join the channel!");
                }
                if (!dataSnapshot.hasChildren()) {
                    Log.d("joinChannel()", "(Confirmed) The channel doesn't exist!");
                    sendFailureMessage("The channel doesn't exist!");
                }
            }
        });
    }

    private void sendFailureMessage(String message) {
        if (onFailureCommand == null) {
            return;
        }
        ((MessageCommand)onFailureCommand).setMessage(message);
        onFailureCommand.execute();
    }

    private void sendSuccessMessage(String message) {
        if (onSuccessCommand == null) {
            return;
        }
        ((MessageCommand)onSuccessCommand).setMessage(message);
        onSuccessCommand.execute();
    }

//    public void joinChannel(final String channelId) {
//        getChannelRoot().addListenerForSingleValueEvent(
//                new CheckExistsListener(channelId, onSuccessCommand, onFailureCommand)
//        );
//    }
//
//    public void checkInChannel(final String channelId, final String userId) {
//        getChannelMembersChild(channelId).addListenerForSingleValueEvent(
//                new CheckExistsListener(userId, onSuccessCommand, onFailureCommand)
//        );
//    }

//    public void addMember(String channelId, String userId) {
//        // Get value for the channel
//        getChannelChild(channelId).addValueEventListener(
//                new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


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


