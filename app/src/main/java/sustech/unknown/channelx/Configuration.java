package sustech.unknown.channelx;

/**
 * Created by dahao on 2017/12/22.
 */

public final class Configuration {

    public static final String channelKey = "channel";
    public static final String membersKey = "members";
    public static final String messagesKey = "messages";
    public static final String themeKey = "theme";

    public static final String OPEN = "OPEN";
    public static final String HOUR_OF_DAY = "HOUR_OF_DAY";
    public static final String MINUTE = "MINUTE_HOUR_DAY";
    public static final String DEFAULT_CHANNEL_NAME = "DEFAULT CHANNEL NAME";

    static final int CREATE_CHANNEL_2_REQUEST = 666;
    static final int ENTER_CHANNEL_REQUEST = 111;
    static final int CREATE_CHANNEL_1_REQUEST = 888;
    static final int JOIN_CHANNEL_REQUEST = 999;
    static final int RC_SIGN_IN = 123;
    static final int CHANNEL_SETTINGS_REQUEST = 777;
    static final int RESULT_DESTROYED = 700;


    static final String CHANNEL_KEY_MESSAGE =
            "sustech.unknown.channelx.CHANNEL_KEY";

    static String ANONYMOUS_EXTRA =
            "sustech.unknown.channelx.ANONYMOUS_EXTRA";
}
