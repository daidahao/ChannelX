package sustech.unknown.channelx.model;

/**
 * Created by dahao on 2017/12/14.
 */

public class Channel {
    private String name, creatorId;
    private long startTime;
    private  int imageId;
    public Channel(){}
    public Channel (String name, int imageId,long time){
        this.name=name;
        this.imageId=imageId;
        this.startTime=time;
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

}
