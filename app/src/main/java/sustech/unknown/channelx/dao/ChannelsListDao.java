package sustech.unknown.channelx.dao;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import sustech.unknown.channelx.command.ObjectCommand;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.model.DatabaseRoot;

/**
 * Created by dahao on 2017/12/24.
 */

public class ChannelsListDao {

    private ObjectCommand<Channel> objectCommand;
    private String userId;

    public ChannelsListDao(ObjectCommand<Channel> objectCommand,
                           String userId) {
        this.objectCommand = objectCommand;
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

    public void readAllChannels() {
        DatabaseReference root = DatabaseRoot.getRoot();
        root.child("channel").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Channel channel = dataSnapshot.getValue(Channel.class);
                if (isChannelEmpty(dataSnapshot, channel)) {
                    return;
                }
                if (channel.getMembers().containsKey(userId)) {
                    Log.d("loadAllChannels()", dataSnapshot.getKey() + "/" + channel.getName());
                    channel.writeKey(dataSnapshot.getKey());
                    objectCommand.setObject(channel);
                    objectCommand.execute();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Channel channel = dataSnapshot.getValue(Channel.class);
                if (isChannelEmpty(dataSnapshot, channel)) {
                    return;
                }
                if (channel.getMembers().containsKey(userId)) {
                    channel.writeKey(dataSnapshot.getKey());
                    objectCommand.setObject(channel);
                    objectCommand.execute();
                } else {
                    // REMOVE THE CHANNEL
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
