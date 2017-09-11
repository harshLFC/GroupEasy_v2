package example.com.groupeasy.pojo;

/**
 * Created by Harsh on 11-09-2017.
 */

public class list_main {

    String name;
    String admin;

    public list_main(){

    }

    public list_main(String name, String admin) {
        this.name = name;
        this.admin = admin;
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


}
