package com.activity.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.activity.R;

// TODO: Auto-generated Javadoc
/**
 * The Class NewActivity is a Fragment that is displayed in the Main activity
 * when the user taps on New Activity tab or when user swipes to First page in
 * ViewPager. You can customize this fragment's contents as per your need.
 */
public class NewActivity extends Fragment
{

	
	private int tedarr[] = {R.drawable.teddy_up,R.drawable.teddy_right, 
			R.drawable.teddy_down, R.drawable.teddy_left};
	private int tedctn = 1;
	
	
	
	/**** */
	private static Context context;
	
	EditText myTextbox;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice;
	OutputStream mmOutputStream;
	InputStream mmInputStream;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
	TextView myLabel;
	
	ImageView TeddyView;
	MediaPlayer mp;
	AlertDialog.Builder builder;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		//context = this;
		//Context context = NewActivity.context;
		//Context context = getView().getContext();
		
		View v = inflater.inflate(R.layout.new_activity, null);
	
		setupView(v);
		
		
		mp = MediaPlayer.create(getActivity(), R.raw.alarm);
		builder = new AlertDialog.Builder(getActivity());
		
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
		TeddyView = (ImageView) v.findViewById(R.id.imageView1);
		myLabel = (TextView)v.findViewById(R.id.textView2_1);
		TeddyView.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v)
			{

				TeddyView.setImageResource(tedarr[tedctn++]);
				tedctn %= 4;
			}
		});
		
		
		final View sw = v.findViewById(R.id.vSwitch);
		sw.setOnClickListener(new OnClickListener() {
			
			
			//TextView myLabel = (TextView)sw.findViewById(R.id.textView2_1);
		    //myTextbox = (EditText)findViewById(R.id.entry);
			@Override
			public void onClick(View v)
			{
				if (sw.getTag() == null)
				{
					sw.setBackgroundResource(R.drawable.swith_up_right);
		            try 
		            {
		                findBT();
		                openBT();
		            }
		            catch (IOException ex) { }

				}
				else
				{
					sw.setBackgroundResource(R.drawable.swith_up_left);
					sw.setTag(null);
					sw.setTag("gps");
					try 
		            {
		            	closeBT();
		            }
		            catch (IOException ex) { }
				}	
			}
		});
	}
	

void findBT()
{
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if(mBluetoothAdapter == null)
    {
        myLabel.setText("No bluetooth adapter available");
    }

    if(!mBluetoothAdapter.isEnabled())
    {
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetooth, 0);
    }

    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    if(pairedDevices.size() > 0)
    {
        for(BluetoothDevice device : pairedDevices)
        {
        	myLabel.setText("Search");
            if(device.getName().equals("RNBT-38B5")) 
            {
            	myLabel.setText("YAY");
                mmDevice = device;
                break;
            }
        }
    }
    myLabel.setText("Bluetooth Device Found");
}

void openBT() throws IOException
{
	myLabel.setText("OpenBT");
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
    mmSocket.connect();
    myLabel.setText("Connect");
    mmOutputStream = mmSocket.getOutputStream();
    mmInputStream = mmSocket.getInputStream();

    beginListenForData();

    myLabel.setText("Bluetooth Opened");
    try 
    {
        sendData();
    }
    catch (IOException ex) { }
}

void beginListenForData()
{
    final Handler handler = new Handler(); 
    final byte delimiter = 10; //This is the ASCII code for a newline character
    myLabel.setText("Begin Listening");
    stopWorker = false;
    readBufferPosition = 0;
    readBuffer = new byte[1024];
    workerThread = new Thread(new Runnable()
    {
        public void run()
        {                
           while(!Thread.currentThread().isInterrupted() && !stopWorker)
           {
                try 
                {
                    int bytesAvailable = mmInputStream.available();                        
                    if(bytesAvailable > 0)
                    {
                        byte[] packetBytes = new byte[bytesAvailable];
                        mmInputStream.read(packetBytes);
                        for(int i=0;i<bytesAvailable;i++)
                        {
                            byte b = packetBytes[i];
                            if(b == delimiter)
                            {
     byte[] encodedBytes = new byte[readBufferPosition];
     System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
     final String data = new String(encodedBytes, "US-ASCII");
     readBufferPosition = 0;

                                handler.post(new Runnable()
                                {
                                	int roll = 0;
                                    public void run()
                                    {
                                        myLabel.setText(data);
                                        if(data.split("\\s+").length == 3 ){
                                        	roll = Integer.parseInt(data.split("\\s+")[2]);
                                        }
                                        if( (roll >= 65 && roll <= 90) || (roll <= -70 && roll >= -90) ){
                                        	// down
                                        	TeddyView.setImageResource(tedarr[2]);
                                        	
                                        	if(!mp.isPlaying()) {
                                        		mp.start();
	                            				//AlertDialog.Builder builder = new AlertDialog.Builder(context);
	                            				builder.setMessage("FLIP THE BABY, NOW!!!!!!");
	                            				
	                            				builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
	                            					public void onClick(DialogInterface dialog,int id) {
	                            						// if this button is clicked, close
	                            						// current activity
	                            						mp.stop();
	                            					}
	                            				  });
	                            				AlertDialog alertDialog = builder.create();
	                            				alertDialog.show();
                                        	}
                                        }
                                        else if( (roll >= 0 && roll <= 10) || (roll <= 0 && roll >= -10)  ){
                                        	// up
                                        	TeddyView.setImageResource(tedarr[0]);
                                        	if(mp.isPlaying()) {
                                        		mp.stop();
                                        	}
                                        }
                                        else if( roll > 10 && roll < 65  ) {
                                        	// right
                                        	TeddyView.setImageResource(tedarr[1]);
                                        	if(mp.isPlaying()) {
                                        		mp.stop();
                                        	}
                                        }
                                        else if( roll <= -10 && roll > -50  ){
                                        	// left
                                        	TeddyView.setImageResource(tedarr[3]);
                                        	if(mp.isPlaying()) {
                                        		mp.stop();
                                        	}
                                        }
                                    }
                                });
                            }
                            else
                            {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } 
                catch (IOException ex) 
                {
                    stopWorker = true;
                }
           }
        }
    });

    workerThread.start();
}

void sendData() throws IOException
{
    //String msg = myTextbox.getText().toString();
	String msg = "d";
    msg += "\n";
    mmOutputStream.write(msg.getBytes());
    myLabel.setText("Data Sent");
}

void closeBT() throws IOException
{
    stopWorker = true;
    mmOutputStream.close();
    mmInputStream.close();
    mmSocket.close();
    myLabel.setText("Bluetooth Closed");
}	
}