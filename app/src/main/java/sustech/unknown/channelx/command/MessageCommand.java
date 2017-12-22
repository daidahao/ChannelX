package sustech.unknown.channelx.command;

/**
 * Created by dahao on 2017/12/22.
 */

public abstract class MessageCommand implements Command {

    protected String message = "DEFAULT MESSAGE";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message != null && !message.trim().isEmpty()) {
            this.message = message;
        }
    }

}
