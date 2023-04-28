package rs.raf.projekat1.sava_ivkovic_rn1220.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity implements Comparable<Activity>{
    private int id;
    private String date;
    private String desc;
    private String startT;
    private String endT;
    private String title;
    private int priority;
    private boolean past;

    public Activity() {
    }

    public Activity(int id, String date, String desc, String startT, String endT, String title, int priority) {
        this.id = id;
        this.date = date;
        this.desc = desc;
        this.startT = startT;
        this.endT = endT;
        this.title = title;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStartT() {
        return startT;
    }

    public void setStartT(String startT) {
        this.startT = startT;
    }

    public String getEndT() {
        return endT;
    }

    public void setEndT(String endT) {
        this.endT = endT;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isPast() {
        return past;
    }

    public void setPast(boolean past) {
        this.past = past;
    }

    @Override
    public int compareTo(Activity activity) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date d1 = sdf.parse(this.getStartT());
            Date d2 = sdf.parse(activity.getStartT());
            return d1.compareTo(d2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
