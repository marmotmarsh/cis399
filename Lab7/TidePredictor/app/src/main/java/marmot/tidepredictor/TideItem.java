package marmot.tidepredictor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Holden on 7/15/2017.
 */

public class TideItem {
    private int tideId;
    private int listId;
    private String date;
    private String day;
    private String time;
    private String predCm;
    private String type;

    private SimpleDateFormat dateOutFormat = new SimpleDateFormat("MMMM dd, yyyy");
    private SimpleDateFormat dateInFormat = new SimpleDateFormat("yyyy/MM/dd");

    public static final String TRUE = "1";
    public static final String FALSE = "0";

    public TideItem() {
        this.date = "";
        this.day = "";
        this.time = "";
        this.predCm = "";
        this.type = "";
    }

    public TideItem(int listId, String inDate, String inDay, String inTime, String inPredCm, String typeHL) {
        this.listId = listId;
        this.date = inDate;
        this.day = inDay;
        this.time = inTime;
        this.predCm = inPredCm;
        this.type = typeHL;
    }

    public TideItem(int taskId, int listId, String inDate, String inDay, String inTime, String inPredCm, String typeHL) {
        this.tideId = taskId;
        this.listId = listId;
        this.date = inDate;
        this.day = inDay;
        this.time = inTime;
        this.predCm = inPredCm;
        this.type = typeHL;
    }

    public int getTideId() {
        return tideId;
    }

    public void setTideId(int tideId) {
        this.tideId = tideId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getDateFormatted() {
        try {
            Date dateFormat = dateInFormat.parse(date.trim());
            String dateFormatted = dateOutFormat.format(dateFormat);
            return dateFormatted;
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPredCm() {
        return predCm;
    }

    public void setPredCm(String predCm) {
        this.predCm = predCm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
