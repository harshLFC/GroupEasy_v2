package example.com.groupeasy.pojo;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * A Pojo class is required to communicate with firebase
 */

public class chatPOJO {

    // inititalize variables
    String name;
    String members;
    String icon;

    Map<String, Object> map = new HashMap<>();

    public chatPOJO() {
// default empty constructor required for calls to DataSnapshot.getValue

    }

    public chatPOJO(String name, String members, String icon) {

        // setters
        this.name = name;
        this.members = members;
        this.icon = icon;
    }

    //getters
    public String getName() {
            return name;
    }

    public String getMembers() {
        return members;
    }

    public String getIcon() {
        return icon;
    }
}
