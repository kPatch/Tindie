package com.activity.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activity.R;
import com.activity.custom.CustomActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.LineGraphView;
// TODO: Auto-generated Javadoc
/**
 * The Class Workout is a Fragment that is displayed in the Main activity when
 * the user taps on Workouts tab or when user swipes to third page in ViewPager.
 * You can customize this fragment's contents as per your need.
 */
public class Workout extends Fragment implements OnClickListener
{

	private GraphViewData bstemp[], bsheart[];
	private double atemp, aheart;
	private LayoutInflater inflatee;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		inflatee = inflater;
		View v = inflater.inflate(R.layout.workout, null);
		bstemp = new GraphViewData[10];
		bsheart = new GraphViewData[10];
		atemp = aheart = 0;
		for(int i = 0; i < 10; i++)
		{
			int time = i;
			bstemp[i] = new GraphViewData(time, Math.random() + 98);
			bsheart[i]= new GraphViewData(time, Math.random() * 30 + 50);
			atemp += bstemp[i].getY();
			aheart += bsheart[i].getY();
		}
		atemp /= 10;
		aheart /= 10;
		
		setupView(v);
		return v;
	}

	/**
	 * Setup the view components for this fragment. You can write your code for
	 * initializing the views, setting the adapters, touch and click listeners
	 * etc.
	 * 
	 * @param v
	 *            the base view of fragment
	 */
	private void setupView(View v)
	{
		View b = v.findViewById(R.id.pause);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				
				setGraphHeart(v);
			}
		});

		b = v.findViewById(R.id.finish);
		b.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v)
			{
				
				setGraphTemp(v);
			}
		});
		
		TextView tv = (TextView) v.findViewById(R.id.textViewHeart);
		tv.setText(String.format("%.2f", aheart));
		
		tv = (TextView) v.findViewById(R.id.textViewTemp);
		tv.setText(String.format("%.2f", atemp));
		//setGraphTemp(v);
	}
	
	private void setGraphHeart(View v)
	{
		LinearLayout ll = (LinearLayout) getView().findViewById(R.id.time);
		ll.removeAllViews();
		GraphViewSeries exampleSeries = new GraphViewSeries(bsheart);
		 
		GraphView graphView = new LineGraphView(
		    v.getContext()// context
		    , "Heart View" // heading
		);
		graphView.addSeries(exampleSeries); // data
		graphView.getLayoutParams().height = 250;
		ll.addView(graphView);
	}
	
	private void setGraphTemp(View v)
	{
		
		LinearLayout ll = (LinearLayout)getView().findViewById(R.id.time);
		ll.removeAllViews();
		GraphViewSeries exampleSeries = new GraphViewSeries(bstemp);
		 
		GraphView graphView = new LineGraphView(
		    v.getContext()// context
		    , "Temperature View" // heading
		);
		graphView.addSeries(exampleSeries); // data
		graphView.getLayoutParams().height = 250;
		ll.addView(graphView);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{

	}
}
