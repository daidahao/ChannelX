package sustech.unknown.channelx.model;

/**
 * Created by dahao on 2017/12/22.
 */

public class Member {

    private String userId;
    private String nickname;
    private long joinTime;
    private String info;

    public Member() {

    }

    public Member(String userId,String nickname,String info){
        this.userId=userId;
        this.nickname=nickname;
        this.info=info;
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

    public String getInfo(){
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
