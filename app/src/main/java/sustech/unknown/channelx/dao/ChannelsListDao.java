package sustech.unknown.channelx.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import sustech.unknown.channelx.Configuration;
import sustech.unknown.channelx.command.ObjectCommand;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.DatabaseRoot;

/**
 * Created by dahao on 2017/12/24.
 */

public class ChannelsListDao {

    private ObjectCommand<Channel> addObjectCommand;
    private ObjectCommand<Channel> removeObjectCommand;
    private String userId;
    private ChildEventListener childEventListener;

    public ChannelsListDao(ObjectCommand<Channel> addObjectCommand,
                           ObjectCommand<Channel> removeObjectCommand,
                           String userId) {
        this.addObjectCommand = addObjectCommand;
        this.removeObjectCommand = removeObjectCommand;
        this.userId = userId;
    }

    private boolean isChannelEmpty(DataSnapshot dataSnapshot, Channel channel) {
        if (!dataSnapshot.hasChildren() || channel == null) {
            return true;
        }
        if (channel.getMembers() == null) {
            return true;
        }
        return false;
    }

    public void removeChildEventListeners() {
        DatabaseRoot.getRoot().child(Configuration.channelKey)
                .removeEventListener(childEventListener);
    }

    public void readAllChannels() {
        childEventListener = new ReadAllChannelsChildEventListener();
        DatabaseRoot.getRoot().child("channel")
                .addChildEventListener(childEventListener);
    }

    private void sendChannel(Channel channel, ObjectCommand<Channel> objectCommand) {
        if (objectCommand != null) {
            objectCommand.setObject(channel);
            objectCommand.execute();
        }
    }

    class ReadAllChannelsChildEventListener implements ChildEventListener  {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Channel channel = dataSnapshot.getValue(Channel.class);
            if (isChannelEmpty(dataSnapshot, channel)) {
                return;
            }
            if (channel.getMembers().containsKey(userId)) {
                Log.d("loadAllChannels()", dataSnapshot.getKey() + "/" + channel.getName());
                channel.writeKey(dataSnapshot.getKey());
                sendChannel(channel, addObjectCommand);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Channel channel = dataSnapshot.getValue(Channel.class);
            if (isChannelEmpty(dataSnapshot, channel)) {
                return;
            }
            channel.writeKey(dataSnapshot.getKey());
            if (channel.getMembers().containsKey(userId)) {
                // channel.writeKey(dataSnapshot.getKey());
                if (channel.isDestroyed()) {
                    sendChannel(channel, removeObjectCommand);
                    sendChannel(channel, addObjectCommand);
                } else {
                    sendChannel(channel, addObjectCommand);
                }
            } else {
                sendChannel(channel, removeObjectCommand);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Channel channel = dataSnapshot.getValue(Channel.class);
            if (isChannelEmpty(dataSnapshot, channel)) {
                return;
            }
            channel.writeKey(dataSnapshot.getKey());
            sendChannel(channel, removeObjectCommand);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
