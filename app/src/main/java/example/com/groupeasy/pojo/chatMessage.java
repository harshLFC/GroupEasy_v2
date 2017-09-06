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

    public chatMessage(String content, String messageUserId, String group) {
        this.content = content;
        this.group = group;
        time = new Date().getTime();
        this.messageUserId = messageUserId;
    }

    public chatMessage(){

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