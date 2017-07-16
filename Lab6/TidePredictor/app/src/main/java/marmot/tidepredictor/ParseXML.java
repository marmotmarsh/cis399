package marmot.tidepredictor;

import android.app.Activity;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Holden on 7/15/2017.
 */

// Information copied from https://github.com/googlesamples/android-BasicSyncAdapter/blob/master/Application/src/main/java/com/example/android/basicsyncadapter/net/FeedParser.java

public class ParseXML {
    private static final String ns = null;

    public ArrayList<TideItem> parse(Activity main) throws XmlPullParserException, IOException {
        try {
            XmlResourceParser parser = main.getResources().getXml(R.xml.florence);
            parser.next();
            return readFeed(parser);
        } finally {
            //in.close();
        }
    }

    private ArrayList<TideItem> readFeed(XmlResourceParser parser) throws XmlPullParserException, IOException {
        ArrayList<TideItem> entries = new ArrayList<>();

        Log.d("PARSING", "Entering the parsing arena");

        int eventType = parser.getEventType();
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlResourceParser.START_TAG) {
                String name = parser.getName();
                Log.d("PARSING", name);
                // Starts by looking for the entry tag
                if (name.equals("item")) {
                    entries.add(readEntry(parser));
                }
            }
            eventType = parser.next();
        }

        return entries;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private TideItem readEntry(XmlResourceParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlResourceParser.START_TAG, ns, "item");
        String date = null;
        String day = null;
        String time = null;
        String predCm = null;
        String type = null;
        while (parser.next() != XmlResourceParser.END_TAG) {
            if (parser.getEventType() != XmlResourceParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            switch (name) {
                case "date":
                    date = readDate(parser);
                    break;
                case "day":
                    day = readDay(parser);
                    break;
                case "time":
                    time = readTime(parser);
                    break;
                case "pred_in_cm":
                    predCm = readPredCm(parser);
                    break;
                case "highlow":
                    type = readType(parser);
                    break;
                default:
                    skip(parser);
            }
        }
        return new TideItem(date, day, time, predCm, type);
    }

    private String readDate(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlResourceParser.START_TAG, ns, "date");
        String date = readText(parser);
        parser.require(XmlResourceParser.END_TAG, ns, "date");
        return date;
    }

    private String readDay(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlResourceParser.START_TAG, ns, "day");
        String day = readText(parser);
        parser.require(XmlResourceParser.END_TAG, ns, "day");
        return day;
    }

    private String readTime(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlResourceParser.START_TAG, ns, "time");
        String time = readText(parser);
        parser.require(XmlResourceParser.END_TAG, ns, "time");
        return time;
    }

    private String readPredCm(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlResourceParser.START_TAG, ns, "pred_in_cm");
        String predCm = readText(parser);
        parser.require(XmlResourceParser.END_TAG, ns, "pred_in_cm");
        return predCm;
    }

    private String readType(XmlResourceParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlResourceParser.START_TAG, ns, "highlow");
        String type = readText(parser);
        parser.require(XmlResourceParser.END_TAG, ns, "highlow");
        return type;
    }

    private String readText(XmlResourceParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlResourceParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlResourceParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlResourceParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlResourceParser.END_TAG:
                    depth--;
                    break;
                case XmlResourceParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

