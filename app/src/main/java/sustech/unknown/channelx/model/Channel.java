package sustech.unknown.channelx.model;

/**
 * Created by dahao on 2017/12/14.
 */

public class Channel {
    private String name;
    private String creatorId;
    private String theme;
    private long startTime, expiredTime;
    private int imageId;
    private boolean anonymous, group;
    private String key;
    private boolean isDestroyed;

    public Channel(){
        this.isDestroyed = false;
    }
    public Channel (String name, int imageId,long time){
        this.name=name;
        this.imageId=imageId;
        this.expiredTime = time;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public int getImageId(){
        return  imageId;
    }
    public int setImageId(int imageId){
       return this.imageId=imageId;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public String readKey() {
        return key;
    }

    public void writeKey(String key) {
        this.key = key;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
