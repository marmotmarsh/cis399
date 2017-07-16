package marmot.tidepredictor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Holden on 7/15/2017.
 */

public class TideItem {
    private String date;
    private String day;
    private String time;
    private int predCm;
    private String type;

    private SimpleDateFormat dateOutFormat = new SimpleDateFormat("MMMM dd, yyyy");
    private SimpleDateFormat dateInFormat = new SimpleDateFormat("yyyy/MM/dd");

    public TideItem(String inDate, String inDay, String inTime, String inPredCm, String typeHL) {
        date = inDate;
        day = inDay;
        time = inTime;
        predCm = Integer.valueOf(inPredCm);
        type = typeHL;
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

    public int getPredCm() {
        return predCm;
    }

    public void setPredCm(int predCm) {
        this.predCm = predCm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
