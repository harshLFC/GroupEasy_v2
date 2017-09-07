package example.com.groupeasy.pojo;

/**
 * Created by Harsh on 06-09-2017.
 */

public class new_groups {

    private String admin;
    private String image;
    private String last_msg;
    private String name;

    public new_groups(){

    }

    public new_groups(String admin, String image, String last_msg, String name) {
        this.admin = admin;
        this.image = image;
        this.last_msg = last_msg;
        this.name = name;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
