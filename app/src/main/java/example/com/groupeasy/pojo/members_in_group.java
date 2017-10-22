package example.com.groupeasy.pojo;

/**
 * Created by Harsh on 29-09-2017.
 */

public class members_in_group {

    String name;
//    String status;
    String image;
    String thumb_image;
    String last_seen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

    public members_in_group(String name, String image, String thumb_image, String last_seen, String id) {
        this.name = name;
        this.image = image;
        this.thumb_image = thumb_image;
        this.last_seen = last_seen;
        this.id = id;
    }



}
