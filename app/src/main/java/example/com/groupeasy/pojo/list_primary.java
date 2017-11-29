package example.com.groupeasy.pojo;

/**
 * This is the POJO or Plain Old Java Object class
 * for Getting and setting Event details
 */

public class list_primary {


    String name;
    String admin;
    String location;
    String id;
    String fromDate;
    public list_primary(String name, String admin, String location, String id, String fromDate) {
//    public list_primary(String name, String admin, String location, String id, members_In in, Out out, Maybe maybe) {
        this.name = name;
        this.admin = admin;
        this.location = location;
        this.id = id;
        this.fromDate = fromDate;
//        this.out = out;
//        this.maybe = maybe;
    }

    //Failed code
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
        //Required Empty Constructor
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

}
