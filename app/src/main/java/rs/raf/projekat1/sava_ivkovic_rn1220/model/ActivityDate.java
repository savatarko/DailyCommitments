package rs.raf.projekat1.sava_ivkovic_rn1220.model;

public class ActivityDate {
    private String day;
    private String month;
    private String year;
    private int priority;

    public ActivityDate() {
    }

    public ActivityDate(String day) {
        this.day = day;
    }

    public ActivityDate(String day, String month, String year, int priority) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.priority = priority;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
