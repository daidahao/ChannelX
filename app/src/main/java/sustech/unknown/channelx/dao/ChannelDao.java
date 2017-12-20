package sustech.unknown.channelx.dao;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.DatabaseRoot;

/**
 * Created by dahao on 2017/12/20.
 */

public class ChannelDao {

    private final String channelKey = "channel";

    private DatabaseReference getChannelRoot() {
        return DatabaseRoot.getRoot().child(channelKey);
    }

    public void createChannel(Channel channel) {
        DatabaseReference channelChild = getChannelRoot().push();
        channelChild.setValue(channel).addOnSuccessListener(
                new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}


