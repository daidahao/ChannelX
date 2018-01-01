package sustech.unknown.channelx.model;

import java.util.HashMap;
import java.util.Map;

import co.intentservice.chatui.models.ChatMessage;
import sustech.unknown.channelx.Configuration;

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
    private int memberCount;
    private Map<String, Member> members = new HashMap<>();
    private Map<String, String> themeList = new HashMap<>();
    private Map<String, ChatMessage> messages = new HashMap<>();
    private int openTimeInMinute;
    private int closedTimeInMinute;

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            return ((Channel)obj).readKey().equals(readKey());
        }
        return super.equals(obj);
    }

    public Channel(){
        this.isDestroyed = false;
        openTimeInMinute = 0;
        closedTimeInMinute = 1440;
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

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public Map<String, Member> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Member> members) {
        this.members = members;
    }

    public Map<String, String> getThemeList() {
        return themeList;
    }

    public void setThemeList(Map<String, String> themeList) {
        this.themeList = themeList;
    }

    public Map<String, ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, ChatMessage> messages) {
        this.messages = messages;
    }

    public int getOpenTimeInMinute() {
        return openTimeInMinute;
    }

    public void setOpenTimeInMinute(int openTimeInMinute) {
        this.openTimeInMinute = openTimeInMinute;
    }

    public int getClosedTimeInMinute() {
        return closedTimeInMinute;
    }

    public void setClosedTimeInMinute(int closedTimeInMinute) {
        this.closedTimeInMinute = closedTimeInMinute;
    }

    public void checkAndSetLegalName() {
        if (name == null) {
            name = Configuration.DEFAULT_CHANNEL_NAME;
        }
        if (name.length() > 20) {
            name = name.substring(0, 20);
        } else {
            
        }
    }
}
