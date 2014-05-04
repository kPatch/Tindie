package com.activity.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
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
     //int roll = Integer.parseInt(data.split("\\s+")[2]);
     readBufferPosition = 0;

                                handler.post(new Runnable()
                                {
                                    public void run()
                                    {
                                        myLabel.setText(data);
                                        //if(roll )
                                        //TeddyView.setImageResource(tedarr[0]);
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