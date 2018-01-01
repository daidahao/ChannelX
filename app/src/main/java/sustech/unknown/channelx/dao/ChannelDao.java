package sustech.unknown.channelx.dao;

import android.net.Uri;
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

import co.intentservice.chatui.models.ChatMessage;
import sustech.unknown.channelx.Configuration;
import sustech.unknown.channelx.command.Command;
import sustech.unknown.channelx.command.MessageCommand;
import sustech.unknown.channelx.command.ReadChannelObjectCommand;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.DatabaseRoot;
import sustech.unknown.channelx.model.Member;

/**
 * Created by dahao on 2017/12/20.
 */

public class ChannelDao {

    private Command onSuccessCommand;
    private Command onFailureCommand;
    private ReadChannelObjectCommand objectCommand;

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

    public ChannelDao(MessageCommand onSuccessCommand, Command onFailureCommand,
                      ReadChannelObjectCommand objectCommand) {
        this.onSuccessCommand = onSuccessCommand;
        this.onFailureCommand = onFailureCommand;
        this.objectCommand = objectCommand;
    }

    private DatabaseReference getChannelRoot() {
        return DatabaseRoot.getRoot().child(Configuration.channelKey);
    }

    private DatabaseReference getChannelChild(String channelId) {
        return getChannelRoot().child(channelId);
    }

    private DatabaseReference getChannelMembersChild(String channelId) {
        return getChannelChild(channelId).child(Configuration.membersKey);
    }


    private DatabaseReference getChannelReference(String key) {
        return getChannelRoot().child(key);
    }

    public void createChannel(final Channel channel,final Uri uri) {
        DatabaseReference channelChild = getChannelRoot().push();
        //set channel uri path = default value unknown
        channel.writeKey(channelChild.getKey());

        channel.checkAndSetLegalName();

        addListenersForTask(channelChild.setValue(channel));
        StorageDao storageDao = new StorageDao();
        if (uri==null) return;
        storageDao.uploadChannelPhoto(uri,channel.readKey());
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

    public void joinChannel(final String channelId, final String userId,
                            final String trueName, final String contactInfo) {
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
                if (channel.isDestroyed()) {
                    sendFailureMessage("The channel has already been destroyed!");
                    return Transaction.success(mutableData);
                }
//                if (channel.getExpiredTime() < System.currentTimeMillis()) {
//                    sendFailureMessage("The channel has expired!");
//                    return Transaction.success(mutableData);
//                }
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
                        member = new Member(
                                channel.getThemeList().get(
                                        String.format("%03d", channel.getMemberCount() + 1)));
                    }
                } else {
                    member = new Member(trueName);
                    member.setInfo(contactInfo);
                }
                Log.d("joinChannel()", "You're added into the member list!");
                Log.d("joinChannel()", contactInfo);
                sendSuccessMessage("You're added into the member list!");
                channel.getMembers().put(userId, member);
                channel.setMemberCount(channel.getMemberCount() + 1);
                mutableData.setValue(channel);

                // Send 1st message to the channel.
                if (channel.getCreatorId().equals(userId)) {
                    addFirstMessage(channelId, member.getNickname(), userId, "created");
                } else {
                    addFirstMessage(channelId, member.getNickname(), userId, "joined");
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if (!b) {
                    sendFailureMessage("Error! Cannot join the channel!");
                    return;
                }
                if (!dataSnapshot.hasChildren()) {
                    Log.d("joinChannel()", "(Confirmed) The channel doesn't exist!");
                    sendFailureMessage("The channel doesn't exist!");
                }
                addFirstMessage();
            }
        });
    }

    /*
    The following two methods might not be very stable.
    Need further examination and testing.
     */
    private MessagesDao messagesDao;
    private ChatMessage chatMessage;

    private void addFirstMessage(String channelId, String nickname, String userId, String keyword) {
        chatMessage = new ChatMessage();
        chatMessage.setMessage(nickname + " has " + keyword + " the channel.");
        chatMessage.setUserid(userId);
        chatMessage.setTimestamp(System.currentTimeMillis());
        Channel channel = new Channel();
        channel.writeKey(channelId);
        messagesDao = new MessagesDao(channel, userId);
    }

    private void addFirstMessage() {
        if (messagesDao != null) {
            messagesDao.addMessage(chatMessage);
        }
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

    private void sendObject(Channel channel) {
        if (objectCommand == null) {
            return;
        }
        objectCommand.setObject(channel);
        objectCommand.execute();
    }

    public void readChannel(String channelId) {
        getChannelChild(channelId).addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Channel channel = dataSnapshot.getValue(Channel.class);
                    channel.writeKey(dataSnapshot.getKey());
                    sendSuccessMessage("Read the channel successfully!");
                    sendObject(channel);
                }
                else {
                    sendFailureMessage("Cannot read the channel!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void leaveChannel(final String channelId, final String userId) {
        getChannelChild(channelId).child(Configuration.membersKey).child(userId)
                .runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Member member = mutableData.getValue(Member.class);
                        if (member != null) {
                            addFirstMessage(channelId, member.getNickname(), userId, "left");
                        }
                        mutableData.setValue(null);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        addFirstMessage();
                        sendSuccessMessage("You has left the channel");
                    }
                });
    }

    // May need to consider multiple circumstances
    public void destoryChannel(final String channelId, final String userId) {
        getChannelChild(channelId).runTransaction(
                new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Channel channel = mutableData.getValue(Channel.class);
                if (channel == null) {
                    return Transaction.success(mutableData);
                }
                if (channel.getCreatorId().equals(userId)) {
                    channel.setDestroyed(true);
                    // channel.setExpiredTime(0);
                    mutableData.setValue(channel);
                    Member member = channel.getMembers().get(userId);
                    addFirstMessage(channelId, member.getNickname(), userId, "destroyed");
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                addFirstMessage();
                sendSuccessMessage("The channel has been destroyed!");
            }
        });
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


