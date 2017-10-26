package example.com.groupeasy.pojo;

/**
 * Created by Harsh on 23-10-2017.
 */

public class members_In {

    String name;
    String value;

    public members_In(){

    }

    public members_In(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
