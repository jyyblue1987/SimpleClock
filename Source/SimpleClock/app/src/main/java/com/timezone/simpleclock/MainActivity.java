package com.timezone.simpleclock;

import android.content.Context;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import common.list.adapter.ItemCallBack;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;

public class MainActivity extends AppCompatActivity {
    ListView m_listItems	= null;
    MyListAdapter m_adapterList = null;
    ArrayList<String> m_countryList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initData();
    }

    protected void findViews()
    {
        m_listItems = (ListView) findViewById(R.id.list_timezone);

    }

    private void initData()
    {
        getCountryList();
        getTimeZone();
    }

    private void getCountryList()
    {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);

        m_countryList = new ArrayList<String>();
        for (String country : countries) {
            System.out.println(country);
            m_countryList.add(country);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, m_countryList);
        //스피너 속성
        Spinner sp = (Spinner) this.findViewById(R.id.spinner_country);
        sp.setPrompt("Select Country"); //
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, m_countryList.get(i), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getTimeZone()
    {
        Date date = new Date();

        List<JSONObject> list = new ArrayList<JSONObject>();
        String[] ids= TimeZone.getAvailableIDs();

        for(int i=0;i<ids.length;i++)
        {
            TimeZone d= TimeZone.getTimeZone(ids[i]);

            /////////////////////////////////////////////////////
            if (!ids[i].matches(".*/.*")) {
                continue;
            }

            String region = ids[i].replaceAll(".*/", "").replaceAll("_", " ");
            int hours = Math.abs(d.getRawOffset()) / 3600000;
            int minutes = Math.abs(d.getRawOffset() / 60000) % 60;
            String sign = d.getRawOffset() >= 0 ? "+" : "-";

            String timeZonePretty = String.format("(UTC %s %02d:%02d) %s", sign, hours, minutes, region);
            //////////////////////////////////////////////////////////////////

            SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(d);
            String localtime = sdf.format(date);


            JSONObject timezone = new JSONObject();
            try {
                timezone.put("timezone", timeZonePretty);
                timezone.put("localtime", localtime);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            list.add(timezone);
        }

        m_adapterList = new TimezonListAdapter(this, list, R.layout.fragment_timezone_item, null);

        m_listItems.setAdapter(m_adapterList);

    }



    class TimezonListAdapter extends MyListAdapter{

        public TimezonListAdapter(Context context, List<JSONObject> data,
                                  int resource, ItemCallBack callback) {
            super(context, data, resource, callback);
        }

        @Override
        protected void loadItemViews(View rowView, final int position)
        {
            final JSONObject item = getItem(position);

            ((TextView)ViewHolder.get(rowView, R.id.txt_timezone_name)).setText(item.optString("timezone"));
            ((TextView)ViewHolder.get(rowView, R.id.txt_timezone_time)).setText(item.optString("localtime"));
        }
    }
}
