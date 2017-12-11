package example.com.groupeasy.pojo;

/**
 * This is the POJO or Plain Old Java Object class
 * for Getting and setting Event details
 */

public class list_details {

    private String details;
    private String fromTime;
    private String toDate;
    private String min;
    private String max;
    private boolean oneDay;
    private String toTime;

    public list_details(){
        //Required Empty Constructor
    }

    public list_details(String details, String min, String  max, boolean oneDay,
                        String fromTime, String toDate, String toTime)
    {
        this.details = details;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.toDate = toDate;
        this.min = min;
        this.max = max;
        this.oneDay = oneDay;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String name) {
        this.details = name;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public boolean isOneDay() {
        return oneDay;
    }

    public void setOneDay(boolean oneDay) {
        this.oneDay = oneDay;
    }

}
