package example.com.groupeasy.pojo;

import java.util.Date;

/**
 * This is the POJO or Plain Old Java Object class
 * for Getting and setting ChatMessages objects
 */

public class chatMessage {

    String grpid;
    private String content;
    private String msgFrom;
    private String messageUserId;
    private long time;

    public chatMessage(String content, String messageUserId, String msgFrom, String grpid) {
        this.content = content;
        this.msgFrom = msgFrom;
        time = new Date().getTime();
        this.messageUserId = messageUserId;
        this.grpid = grpid;
    }

    public chatMessage(){
        //Required Empty Constructor
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

    public String getMsgFrom() {
        return msgFrom;
    }

    public void setMsgFrom(String group) {
        this.msgFrom = group;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}