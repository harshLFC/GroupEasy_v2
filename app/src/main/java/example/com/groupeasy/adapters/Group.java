package example.com.groupeasy.adapters;

/**
 * A Pojo class is required to communicate with firebase
 */

public class Group {

    private String groupName;

    private Integer rect;


    public Group() {

    }

    public Group(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getRect() {
        return rect;
    }

    public void setRect(Integer rect) {
        this.rect = rect;
    }
}

