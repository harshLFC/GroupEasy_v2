package example.com.groupeasy.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Harsh on 11-09-2017.
 */

//TODO add extra children db

public class list_primary {

    public list_primary(String name, String admin, String location, String id) {
//    public list_primary(String name, String admin, String location, String id, members_In in, Out out, Maybe maybe) {
        this.name = name;
        this.admin = admin;
        this.location = location;
        this.id = id;
//        this.out = out;
//        this.maybe = maybe;
    }

    String name;
    String admin;
    String location;
    String id;

//    public members_In getIn() {
//        return in;
//    }
//
//    public void setIn(members_In in) {
//        this.in = in;
//    }

//    public Out getOut() {
//        return out;
//    }
//
//    public void setOut(Out out) {
//        this.out = out;
//    }
//
//    public Maybe getMaybe() {
//        return maybe;
//    }
//
//    public void setMaybe(Maybe maybe) {
//        this.maybe = maybe;
//    }

//    members_In in;
//    Out out;
//    Maybe maybe;

//    Participants participants;

    public list_primary(){

    }



//    public Participants getParticipants() {
//        return participants;
//    }
//
//    public void setParticipants(Participants participants) {
//        this.participants = participants;
//    }

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

//    public static class members_In{
//        String in;
//
//        public members_In(){
//
//        }
//
//        public String getIn() {
//            return in;
//        }
//
//        public void setIn(String in) {
//            this.in = in;
//        }
//
//        public members_In(String in){
//        this.in = in;
//
//    }
//    }


//   public static class Participants{
//
//        String name;
//        String value;
//
//        public Participants(){
//
//        }
//
//        public Participants(String name, String value) {
//            this.name = name;
//            this.value = value;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//    }
}
