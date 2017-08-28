package example.com.groupeasy.adapters;

/**
 * A Pojo class is required to communicate with firebase
 */

public class Group {

    private String groupName;


    public Group() {

    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}

