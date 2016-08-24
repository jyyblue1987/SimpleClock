package com.timezone.simpleclock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import common.list.adapter.ItemCallBack;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;

public class MainActivity extends AppCompatActivity {
    ListView m_listItems	= null;
    MyListAdapter m_adapterList = null;

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
        getTimeZone();
    }

    private void getTimeZone()
    {
        List<JSONObject> list = new ArrayList<JSONObject>();
        String[] ids= TimeZone.getAvailableIDs();
        for(int i=0;i<ids.length;i++)
        {
            System.out.println("Availalbe ids.................."+ids[i]);
            TimeZone d= TimeZone.getTimeZone(ids[i]);
            System.out.println("time zone."+d.getDisplayName());
            System.out.println("savings."+d.getDSTSavings());
            System.out.println("offset."+d.getRawOffset());

            /////////////////////////////////////////////////////
            if (!ids[i].matches(".*/.*")) {
                continue;
            }

            String region = ids[i].replaceAll(".*/", "").replaceAll("_", " ");
            int hours = Math.abs(d.getRawOffset()) / 3600000;
            int minutes = Math.abs(d.getRawOffset() / 60000) % 60;
            String sign = d.getRawOffset() >= 0 ? "+" : "-";

            String timeZonePretty = String.format("(UTC %s %02d:%02d) %s", sign, hours, minutes, region);
            System.out.println(timeZonePretty);
            //////////////////////////////////////////////////////////////////

            JSONObject timezone = new JSONObject();
            try {
                timezone.put("timezone", timeZonePretty);
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
        }
    }
}
