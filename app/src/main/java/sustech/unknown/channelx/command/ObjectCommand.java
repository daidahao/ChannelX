package sustech.unknown.channelx.command;

/**
 * Created by dahao on 2017/12/23.
 */

public abstract class ObjectCommand<T> implements Command {

    protected T object;

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
