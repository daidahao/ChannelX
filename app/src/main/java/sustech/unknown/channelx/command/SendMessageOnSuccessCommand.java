package sustech.unknown.channelx.command;

import co.intentservice.chatui.ChatView;

/**
 * Created by dahao on 2017/12/23.
 */

public class SendMessageOnSuccessCommand implements Command {

    private ChatView chatView;

    public SendMessageOnSuccessCommand(ChatView chatView) {
        this.chatView = chatView;
    }

    @Override
    public void execute() {
        if (chatView != null) {
            chatView.clearInput();
        }
    }
}
