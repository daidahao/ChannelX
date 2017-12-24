package sustech.unknown.channelx.command;

import co.intentservice.chatui.ChatView;
import sustech.unknown.channelx.util.ToastUtil;

/**
 * Created by dahao on 2017/12/23.
 */

public class SendMessageOnFailureCommand implements Command {

    private ChatView chatView;

    public SendMessageOnFailureCommand(ChatView chatView) {
        this.chatView = chatView;
    }

    @Override
    public void execute() {
        if (chatView != null) {
            ToastUtil.makeToast(chatView.getContext(), "Cannot send the message!");
            chatView.clearInput();
        }
    }
}
