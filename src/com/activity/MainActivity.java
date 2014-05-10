package com.activity;

// TEST: Irvin Cardenas
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.custom.CustomActivity;
import com.activity.ui.History;
import com.activity.ui.NewActivity;
import com.activity.ui.Routes;
import com.activity.ui.Workout;

// TODO: Auto-generated Javadoc
/**
 * The Activity MainActivity is the Main screen of the app and it holds all the
 * views and Fragments used in the app.
 * 
 */
public class MainActivity extends CustomActivity {

	private static final int REQUEST_ENABLE_BT = 1;

	/** The view pager for swipe views. */
	private ViewPager pager;

	/** The current selected tab. */
	private View currentTab;
	
	/** The Bluetooth Broadcast Receiver*/
	private final BroadcastReceiver mReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
				final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				Toast toast;
				switch(state) {
				case BluetoothAdapter.STATE_OFF:
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					break;
				case BluetoothAdapter.STATE_TURNING_OFF:
					toast =Toast.makeText(getApplicationContext(), "Tindie requires Bluetooth", Toast.LENGTH_LONG);
					toast.show();
					break;
				case BluetoothAdapter.STATE_ON:
					toast =Toast.makeText(getApplicationContext(), "Press On to connect to your Tindie device", Toast.LENGTH_LONG);
					//toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					break;
				}
			}
		}
	};	
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.newsfeeder.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println(" Before setupActionBar");
		setupActionBar();
		initTabs();
		initPager();//textView2_1
		System.out.println(" Before myLabel");
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null) {
			CharSequence text = "Oops! Your device does ont support bluetooth.";
			Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
			toast.show();
		}
		
		if(!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		
		// Register a receiver for Bluetooth Adapter state change
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		this.registerReceiver(mReceiver, filter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mReceiver);
	}

	/**
	 * This method will setup the top title bar (Action bar) content and display
	 * values. It will also setup the custom background theme for ActionBar. You
	 * can override this method to change the behavior of ActionBar for
	 * particular Activity
	 */
	protected void setupActionBar() {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setLogo(R.drawable.icon);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
	}

	/**
	 * Initialize the tabs. You can write your code related to Tabs.
	 */
	private void initTabs() {
		findViewById(R.id.tab1).setOnClickListener(this);
		//findViewById(R.id.tab2).setOnClickListener(this);
		findViewById(R.id.tab3).setOnClickListener(this);
		findViewById(R.id.tab4).setOnClickListener(this);
		setCurrentTab(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.activity.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.tab1)
			pager.setCurrentItem(0, true);
		//else if (v.getId() == R.id.tab2)
			//pager.setCurrentItem(1, true);
		else if (v.getId() == R.id.tab3)
			pager.setCurrentItem(2, true);
		else if (v.getId() == R.id.tab4)
			pager.setCurrentItem(3, true);
	}

	/**
	 * Sets the current selected tab. Called whenever a Tab is selected either
	 * by clicking on Tab button or by swiping the ViewPager. You can write your
	 * code related to tab selection actions.
	 * 
	 * @param page
	 *            the current page of ViewPager
	 */
	private void setCurrentTab(int page) {
		if (currentTab != null)
			currentTab.setEnabled(true);
		if (page == 0)
			currentTab = findViewById(R.id.tab1);
		//else if (page == 1)
			//currentTab = findViewById(R.id.tab2);
		else if (page == 2)
			currentTab = findViewById(R.id.tab3);
		else
			currentTab = findViewById(R.id.tab4);
		currentTab.setEnabled(false);
		getActionBar().setTitle(((Button) currentTab).getText().toString());
	}

	/**
	 * Initialize the ViewPager. You can customize this method for writing the
	 * code related to view pager actions.
	 */
	private void initPager() {
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int page) {
				setCurrentTab(page);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		pager.setAdapter(new DummyPageAdapter(getSupportFragmentManager()));
	}

	/**
	 * The Class DummyPageAdapter is a dummy pager adapter for ViewPager. You
	 * can customize this adapter as per your needs.
	 */
	private class DummyPageAdapter extends FragmentPagerAdapter {

		/**
		 * Instantiates a new dummy page adapter.
		 * 
		 * @param fm
		 *            the FragmentManager
		 */
		public DummyPageAdapter(FragmentManager fm) {
			super(fm);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int pos) {
			if (pos == 0)
				return new NewActivity();
			//if (pos == 1)
				//return new Routes();
			if (pos == 1)
				return new Workout();
				return new History();
				
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return 4;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.newsfeeder.custom.CustomActivity#onCreateOptionsMenu(android.view
	 * .Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.activity.custom.CustomActivity#onOptionsItemSelected(android.view
	 * .MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_setting) {
			startActivity(new Intent(this, Setting.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
