package marmot.tidepredictor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by marmot on 7/15/2017.
 */

public class TideItem {
    private String date;
    private String day;
    private String time;
    private double predCm;
    private String type;

    private SimpleDateFormat dateOutFormat =
            new SimpleDateFormat("EEEE h:mm a (MMM d)");

    private SimpleDateFormat dateInFormat =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    public TideItem(String inDate, String inDay, String inTime, String inPredCm, String typeHL) {
        date = inDate;
        day = inDay;
        time = inTime;
        predCm = Double.valueOf(inPredCm);
        type = typeHL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateFormatted() {
        try {
            Date dateFormat = dateInFormat.parse(date.trim());
            String pubDateFormatted = dateOutFormat.format(date);
            return pubDateFormatted;
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
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

    public double getPredCm() {
        return predCm;
    }

    public void setPredCm(double predCm) {
        this.predCm = predCm;
    }

    public double getVertical() {
        return predCm;
    }

    public void setVertical(double predCm) {
        this.predCm = predCm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
