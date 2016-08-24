package common.list.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;



public class MyPagerAdapter extends PagerAdapter {
	private List<JSONObject> lstData = new ArrayList<JSONObject>();
	protected ItemCallBack m_callBack = null;
	protected Context		m_context = null;
	
	protected boolean m_bValidData = false;
	
	
	
	public MyPagerAdapter(Context context, List<JSONObject> data, ItemCallBack callback) {
		super();
		lstData = data;
		m_callBack = callback;
		
		m_context = context;
	}
	
	public List<JSONObject> getData()
	{
		return lstData;
	}
	@Override
	public int getCount() {		
    	int count =  null != lstData ? lstData.size() : 0;
    	if( count < 1 )
    	{
    		m_bValidData = false;
    		return 1;
    	}
    	
    	m_bValidData = true; 
    		
    	return count;
	}

	public JSONObject getItem(int i) {
		if(lstData==null || lstData.size()<=i) {
			return null;
		}
		return lstData.get(i);
	}

	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		if( container == null )
			return null;
		
	
		View subView = loadItemViews(position);
		if( subView == null )
			return null;
		
		container.addView(subView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);	
	
		return subView;
	}
	
	public void addItemList(List<JSONObject> data)
	{
		if( data == null || data.size() < 1 )
			return;
		
		if( lstData == null )
		{
			lstData = data;
		}
		else
		{
			for( int i = 0; i < data.size(); i++ )
				lstData.add(data.get(i));			
		}
		this.notifyDataSetChanged();
	}
	
	public void replaceItemList(List<JSONObject> data, int start)
	{
		if( data == null || data.size() < 1 )
			return;
		
		if( lstData == null )
		{
			lstData = data;
		}
		else
		{
			if( start >= lstData.size() )
				lstData.addAll(data);
			else
			{
				int count = Math.min(lstData.size() - start, data.size());
				for(int i = 0; i < count; i++ )
				{
					lstData.remove(start);
				}
				lstData.addAll(start, data);
			}
			
		}
		this.notifyDataSetChanged();
	}
	protected View loadItemViews(int position)
	{
		TextView view = new TextView(m_context);
		view.setText("There is no item");
		view.setGravity(Gravity.CENTER);
		return view;
	}
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
