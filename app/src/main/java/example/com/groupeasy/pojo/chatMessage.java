package example.com.groupeasy.pojo;

import java.util.Date;

/**
 * Created by Harsh on 05-09-2017.
 */

public class chatMessage {

    private String content;
    private String group;
    private String messageUserId;
    private long time;
    String grpid;



    public chatMessage(String content, String messageUserId, String group, String grpid) {
        this.content = content;
        this.group = group;
        time = new Date().getTime();
        this.messageUserId = messageUserId;
        this.grpid = grpid;
    }

    public chatMessage(){

    }
    public String getGrpid() {
        return grpid;
    }

    public void setGrpid(String grpid) {
        this.grpid = grpid;
    }
    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}