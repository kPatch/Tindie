package com.activity.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.activity.MainActivity;
import com.activity.R;

// TODO: Auto-generated Javadoc
/**
 * The Class History is a Fragment that is displayed in the Main activity when
 * the user taps on History tab or when user swipes to Fourth page in ViewPager.
 * You can customize this fragment's contents as per your need.
 */
public class History extends Fragment
{


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.listview, null);
		setupView(v);
		return v;
	}

	/**
	 * Setup the view components for this fragment. You write your code for
	 * initializing the views, setting the adapters, touch and click listeners
	 * etc.
	 * 
	 * @param v
	 *            the base view of fragment
	 */
	private void setupView(View v)
	{
		ListView list = (ListView) v;
		list.setAdapter(new HistoryAdapter());
	}

	/**
	 * The Class HistoryAdapter is the adapter for list view used in this
	 * fragment. You must provide valid values for adapter count and must write
	 * your code for binding the data to each item in adapter as per your need.
	 */
	private class HistoryAdapter extends BaseAdapter
	{

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return 10;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int arg0)
		{
			return null;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int arg0, View v, ViewGroup arg2)
		{
			if (v == null){

				v = getLayoutInflater(null)
						.inflate(R.layout.history_item, null);
			}
			return v;
		}

	}
}
