package com.activity.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.activity.R;

// TODO: Auto-generated Javadoc
/**
 * The Class NewActivity is a Fragment that is displayed in the Main activity
 * when the user taps on New Activity tab or when user swipes to First page in
 * ViewPager. You can customize this fragment's contents as per your need.
 */
public class NewActivity extends Fragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.new_activity, null);

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
		final View sw = v.findViewById(R.id.vSwitch);
		sw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				if (sw.getTag() == null)
				{
					sw.setBackgroundResource(R.drawable.swith_up_right);
					sw.setTag("gps");
				}
				else
				{
					sw.setBackgroundResource(R.drawable.swith_up_left);
					sw.setTag(null);
				}
			}
		});
	}
}
