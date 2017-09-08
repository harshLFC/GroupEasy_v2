package example.com.groupeasy.pojo;

import java.io.Serializable;


public class Groups implements Serializable {

    private String groupName;
    private String image;
    private String lastMsg;


    private int roasterCount;
    private int listCounter;
    private int pollCount;

    private String groupAuthorName;

    public Groups(){

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public int getRoasterCount() {
        return roasterCount;
    }

    public void setRoasterCount(int roasterCount) {
        this.roasterCount = roasterCount;
    }

    public int getListCounter() {
        return listCounter;
    }

    public void setListCounter(int listCounter) {
        this.listCounter = listCounter;
    }

    public int getPollCount() {
        return pollCount;
    }

    public void setPollCount(int pollCount) {
        this.pollCount = pollCount;
    }

    public String getGroupAuthorName() {
        return groupAuthorName;
    }

    public void setGroupAuthorName(String groupAuthorName) {
        this.groupAuthorName = groupAuthorName;
    }
}
