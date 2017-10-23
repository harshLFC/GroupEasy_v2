package example.com.groupeasy.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Harsh on 11-09-2017.
 */

public class list_primary {

    String name;
    String admin;
    String location;
    String id;

    Participants participants;

    public list_primary(){

    }

    public list_primary(String name, String admin, String location, String id, Participants participants) {
        this.name = name;
        this.admin = admin;
        this.location = location;
        this.id = id;
        this.participants = participants;

    }

    public Participants getParticipants() {
        return participants;
    }

    public void setParticipants(Participants participants) {
        this.participants = participants;
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

   public static class Participants{

        String name;
        String value;

        public Participants(){

        }

        public Participants(String name, String value) {
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
}
