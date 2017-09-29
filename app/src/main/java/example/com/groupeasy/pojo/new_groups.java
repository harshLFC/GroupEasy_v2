package example.com.groupeasy.pojo;

import java.io.Serializable;

/**
 * Created by Harsh on 06-09-2017.
 */

public class    new_groups implements Serializable{

    private String admin;
    private String image;
    private String last_msg;
    private String name;
    private String group_id;
    private String thumb_image;

    public new_groups(){

    }

    public new_groups(String admin, String image, String last_msg, String name, String group_id, String thumb_image) {
        this.admin = admin;
        this.image = image;
        this.last_msg = last_msg;
        this.name = name;
        this.group_id = group_id;
        this.thumb_image = thumb_image;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
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

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}
