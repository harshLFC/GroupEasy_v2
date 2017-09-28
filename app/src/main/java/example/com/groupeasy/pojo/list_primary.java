package example.com.groupeasy.pojo;

/**
 * Created by Harsh on 11-09-2017.
 */

public class list_primary {

    String name;
    String admin;
    String location;
    String id;

    public list_primary(){

    }

    public list_primary(String name, String admin, String location, String id) {
        this.name = name;
        this.admin = admin;
        this.location = location;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
