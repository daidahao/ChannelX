package co.intentservice.chatui.models;

import android.text.format.DateFormat;

import java.util.concurrent.TimeUnit;


/**
 *
 * Chat Message model used when ChatMessages are required, either to be sent or received,
 * all messages that are to be shown in the chat-ui must be contained in this model.
 *
 */
public class ChatMessage {
    private String message;
    private long timestamp;
    private Type type;
    private String userid;
    private String nickname;

    public ChatMessage() {

    }

    public ChatMessage(String message, long timestamp, String userid) {
        this.message = message;
        this.timestamp = timestamp;
        this.userid = userid;
    }

    public ChatMessage(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessage(String message, long timestamp, Type type){
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public String findFormattedTime(){

        long oneDayInMillis = TimeUnit.DAYS.toMillis(1); // 24 * 60 * 60 * 1000;

        long timeDifference = System.currentTimeMillis() - timestamp;

        return timeDifference < oneDayInMillis
                ? DateFormat.format("hh:mm a", timestamp).toString()
                : DateFormat.format("dd MMM - hh:mm a", timestamp).toString();
    }

//    public String findUsername() {
//        if (type == Type.SENT) {
//            return "Me";
//        }
//        return "UNKNOWN";
//    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public enum Type {
        SENT, RECEIVED
    }
}
