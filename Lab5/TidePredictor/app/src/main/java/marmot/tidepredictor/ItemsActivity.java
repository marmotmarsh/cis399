package marmot.tidepredictor;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ItemsActivity extends Activity implements AdapterView.OnItemClickListener {
    private final String FILENAME = "src/main/res/xml/florence.xml";

    private ArrayList<TideItem> tideItems;

    private TextView titleTextView;
    private ListView itemsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        itemsListView = (ListView) findViewById(R.id.itemsListView);

        tideItems = new ArrayList<>();

        itemsListView.setOnItemClickListener(this);

        readXMLFile();
    }


    public void readXMLFile() {
        Log.d("TIDES", "Trying to read xml file");

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new URL(FILENAME));
            doc.getDocumentElement().normalize();

            Log.d("TIDES", "Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("pr");
            Log.d("TIDES", "----------------------------");

            String t, v, type;

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    t = eElement.getAttribute("t");
                    v = eElement.getAttribute("v");
                    type = eElement.getAttribute("type");

                    Log.d("TIDES", t + v + type);

                    tideItems.add(new TideItem(t, v, type));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateDisplay();
    }

    public void updateDisplay() {
        if (tideItems == null) {
            titleTextView.setText("Unable to get Tides");
            return;
        }

        // create a List of Map<String, ?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        for (TideItem item : tideItems) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("date", item.getDateFormatted());
            map.put("type", item.getType());
            map.put("vertical", String.valueOf(item.getVertical()));
            data.add(map);
        }

        // create the resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"date", "type", "vertical"};
        int[] to = {R.id.dateTextView, R.id.tideTypeTextView, R.id.verticalTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);

        Log.d("News reader", "Feed displayed: " + new Date());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v,
                            int position, long id) {

        // get the item at the specified position
        TideItem item = tideItems.get(position);

        Toast.makeText(this, String.valueOf(item.getVertical()), Toast.LENGTH_SHORT).show();
    }
}