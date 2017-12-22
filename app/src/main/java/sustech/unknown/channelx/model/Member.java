package sustech.unknown.channelx.model;

/**
 * Created by dahao on 2017/12/22.
 */

public class Member {

    private String userId;
    private String nickname;
    private long joinTime;

    public Member() {

    }

    public Member(String nickname) {
        this.nickname = nickname;
        this.joinTime = System.currentTimeMillis();
    }

    public String readUserId() {
        return userId;
    }

    public void writeUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }
}
