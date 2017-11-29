package example.com.groupeasy.pojo;

/**
 * This is the POJO or Plain Old Java Object class
 * for Getting and setting Member participants in an Event
 */

public class members_In {
    String name;
    String value;
    public members_In(){
        //Required Empty Constructor
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
