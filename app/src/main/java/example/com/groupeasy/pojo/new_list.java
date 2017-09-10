package example.com.groupeasy.pojo;

/**
 * Created by Harsh on 09-09-2017.
 */

public class new_list {

    private String name;
    private String location;
    private String fromDate;
    private String fromTime;

    public new_list(){

    }

    public new_list(String name, String location, int min, int max, boolean oneDay, String fromDate,
                    String fromTime, String toDate, String toTime, boolean global)
    {
        this.name = name;
        this.location = location;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.toDate = toDate;
        this.min = min;
        this.max = max;
        this.oneDay = oneDay;
        this.global = global;
    }

    private String toTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isOneDay() {
        return oneDay;
    }

    public void setOneDay(boolean oneDay) {
        this.oneDay = oneDay;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    private String toDate;

    private int min;
    private int max;

    private boolean oneDay;
    private boolean global;
}
