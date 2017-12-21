package sustech.unknown.channelx.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import sustech.unknown.channelx.CreateChannelActivity2;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.CurrentUser;
import sustech.unknown.channelx.model.DatabaseRoot;

/**
 * Created by dahao on 2017/12/20.
 */

public class ChannelDao {

    private final String channelKey = "channel";
    private CreateChannelActivity2 activity2;

    public ChannelDao() {

    }

    public  ChannelDao(CreateChannelActivity2 activity2) {
        this.activity2 = activity2;
    }


    private DatabaseReference getChannelRoot() {
        return DatabaseRoot.getRoot().child(channelKey);
    }

    private DatabaseReference getChannelReference(String key) {
        return getChannelRoot().child(key);
    }

    public void createChannel(final Channel channel) {
        DatabaseReference channelChild = getChannelRoot().push();
        channel.writeKey(channelChild.getKey());
        channelChild.setValue(channel).addOnSuccessListener(
                new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                joinChannel(channel);
                if (activity2 != null) {
                    activity2.joinChannel(channel);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void joinChannel(Channel channel) {
        if (channel.getCreatorId().equals(
                CurrentUser.getUser().getUid())){

        }
    }


}


