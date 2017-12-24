package sustech.unknown.channelx.command;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

/**
 * Created by dahao on 2017/12/23.
 */

public class ChatMessageObjectCommand extends ObjectCommand<ChatMessage> {

    private ChatView chatView;

    public ChatMessageObjectCommand(ChatView chatView) {
        this.chatView = chatView;
    }

    @Override
    public void execute() {
        if (chatView != null) {
            chatView.addMessage(object);
        }
    }
}
