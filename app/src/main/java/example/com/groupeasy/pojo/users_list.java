package example.com.groupeasy.pojo;

/**
 * Created by Harsh on 17-09-2017.
 */

public class users_list {

    String name;
    String status;
    String image;
    String rosters;
    String polls;
    String lists;
    String favs;
    String thumb_image;
    String last_seen;

    public users_list(){

    }

    public users_list(String name, String status, String image, String rosters, String polls,
                      String lists, String favs, String thumb_image, String last_seen) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.rosters = rosters;
        this.polls = polls;
        this.lists = lists;
        this.favs = favs;
        this.thumb_image = thumb_image;
        this.last_seen = last_seen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRosters() {
        return rosters;
    }

    public void setRosters(String rosters) {
        this.rosters = rosters;
    }

    public String getPolls() {
        return polls;
    }

    public void setPolls(String polls) {
        this.polls = polls;
    }

    public String getLists() {
        return lists;
    }

    public void setLists(String lists) {
        this.lists = lists;
    }

    public String getFavs() {
        return favs;
    }

    public void setFavs(String favs) {
        this.favs = favs;
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

}
