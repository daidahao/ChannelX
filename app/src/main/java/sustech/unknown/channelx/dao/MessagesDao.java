package sustech.unknown.channelx.dao;

import com.google.firebase.database.DatabaseReference;

import co.intentservice.chatui.models.ChatMessage;
import sustech.unknown.channelx.model.Channel;
import sustech.unknown.channelx.model.DatabaseRoot;

/**
 * Created by dahao on 2017/12/22.
 */

public class MessagesDao {

    public DatabaseReference getMessagesNode(Channel channel) {
        return DatabaseRoot.getRoot()
                .child(ChannelDao.channelKey)
                .child(channel.readKey())
                .child(ChannelDao.messagesKey);

    }

    public void addMessages(Channel channel, ChatMessage chatMessage) {
        DatabaseReference messagesNode = getMessagesNode(channel);

    }

}
