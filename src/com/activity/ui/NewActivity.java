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
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.R;

// TODO: Auto-generated Javadoc
/**
 * The Class NewActivity is a Fragment that is displayed in the Main activity
 * when the user taps on New Activity tab or when user swipes to First page in
 * ViewPager. You can customize this fragment's contents as per your need.
 */
public class NewActivity extends Fragment {

	protected static final int SOCKET_CONNECTED = 0;
	protected static final int VALUES = 0;
	private static final int MESSAGE_READ = 0;
	private int tedarr[] = { R.drawable.teddy_up, R.drawable.teddy_right,
			R.drawable.teddy_down, R.drawable.teddy_left };
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
	
	/*
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
		  switch (msg.what) {
		    case SOCKET_CONNECTED: {
		      mBluetoothConnection = (ConnectionThread) msg.obj;
		      if (!mServerMode)
		        mBluetoothConnection.write("this is a message".getBytes());
		      break;
		    }
		    case DATA_RECEIVED: {
		      data = (String) msg.obj;
		      tv.setText(data);
		      if (mServerMode)
		       mBluetoothConnection.write(data.getBytes());
		      break;
		     }
		     case MESSAGE_READ:
		      // your code goes here	
		  }
		}
	};*/
	
	//private final Handler mHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// context = this;
		// Context context = NewActivity.context;
		// Context context = getView().getContext();

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
	private void setupView(View v) {
		TeddyView = (ImageView) v.findViewById(R.id.imageView1);
		myLabel = (TextView) v.findViewById(R.id.textView2_1);
		TeddyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				TeddyView.setImageResource(tedarr[tedctn++]);
				tedctn %= 4;
			}
		});

		final View sw = v.findViewById(R.id.vSwitch);
		sw.setOnClickListener(new OnClickListener() {

			// TextView myLabel = (TextView)sw.findViewById(R.id.textView2_1);
			// myTextbox = (EditText)findViewById(R.id.entry);
			@Override
			public void onClick(View v) {
				ConnectThread ct = null;
				if (sw.getTag() == null) {
					sw.setBackgroundResource(R.drawable.swith_up_right);
					/*
					try {
						findBT();
						openBT();
					} catch (IOException ex) {
					}
					*/
					findBT();
					ct = new ConnectThread(mmDevice);
					ct.run();

				} else {
					sw.setBackgroundResource(R.drawable.swith_up_left);
					sw.setTag(null);
					sw.setTag("gps");
					ct.cancel();
					/*
					try {
						//closeBT();
						
					} catch (IOException ex) {
					}
					*/
				}
				
				/*
				try {
					sendData();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				beginListenForData();
				*/
			}
		});
	}
	
	//https://www.assembla.com/code/miu_gwanic/subversion/nodes/60/game/RemoteControllerBT/src/org/gredam/gwanic/BluetoothControllerService.java
	//mHandle: https://www.assembla.com/code/miu_gwanic/subversion/nodes/60/game/RemoteControllerBT/src/org/gredam/gwanic/Game.java
	//
	// Use this Handler to write the Bluetooth data to the UI TextView
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	
        	/*
            switch (msg.what) {
            case MESSAGE_READ:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                Toast.makeText(getActivity(), writeMessage, Toast.LENGTH_SHORT).show();

                //Toast.makeText(getActivity(), msg.getData().getString(TOAST),
                //        Toast.LENGTH_SHORT).show();
                break;
            }
            */
            
            byte[] writeBuf = (byte[]) msg.obj;
            // construct a string from the buffer
            String data = new String(writeBuf);
            
            int roll = 0;
			myLabel.setText(data);
			if (data.split("\\s+").length == 3) {
				roll = Integer.parseInt(data
						.split("\\s+")[2]);
			}
			if ((roll >= 65 && roll <= 90)
					|| (roll <= -70 && roll >= -90)) {
				// down
				TeddyView
						.setImageResource(tedarr[2]);

				if (!mp.isPlaying()) {
					mp.start();
					builder.setMessage("FLIP THE BABY, NOW!!!!!!");

					builder.setPositiveButton(
							"Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog,
										int id) {
									mp.stop();
								}
							});
					AlertDialog alertDialog = builder
							.create();
					alertDialog.show();
				}
			} else if ((roll >= 0 && roll <= 10)
					|| (roll <= 0 && roll >= -10)) {
				// up
				TeddyView
						.setImageResource(tedarr[0]);
				if (mp.isPlaying()) {
					mp.stop();
				}
			} else if (roll > 10 && roll < 65) {
				// right
				TeddyView
						.setImageResource(tedarr[1]);
				if (mp.isPlaying()) {
					mp.stop();
				}
			} else if (roll <= -10
					&& roll > -50) {
				// left
				TeddyView
						.setImageResource(tedarr[3]);
				if (mp.isPlaying()) {
					mp.stop();
				}
			}

        }
    };	
	
	void findBT() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			myLabel.setText("No bluetooth adapter available");
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBluetooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, 0);
		}

		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				myLabel.setText("Search");
				if (device.getName().equals("RNBT-38B5")) {
					myLabel.setText("YAY");
					mmDevice = device;
					break;
				}
			}
		}
		myLabel.setText("Bluetooth Device Found");
	}

    private void manageConnectedSocket(BluetoothSocket mmSocket) {
    	
    	ConnectedThread connectedThread = new ConnectedThread(mmSocket);
    	connectedThread.write(new byte[]{'d'}); // Send 'd' message to ArduBoard
    	/*
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = mmSocket.getInputStream();
            tmpOut = mmSocket.getOutputStream();
        } catch (IOException e) { }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
		*/

    }

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
		private final UUID uuid = UUID.fromString(MY_UUID); // Standard
															// SerialPortService
															// ID

		public ConnectThread(BluetoothDevice device) {
			BluetoothSocket tmp = null;
			mmDevice = device;

			try {
				tmp = device.createRfcommSocketToServiceRecord(uuid);
			} catch (IOException e) {

			}
			mmSocket = tmp;
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDiscovery();

			try {
				mmSocket.connect();
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) {
				}
				return;
			}

			// Do work to manage the connection (in a separate thread)
			manageConnectedSocket(mmSocket);
		}

		// Will cancel an in-progress connection, and close the socket
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {

			}
		}
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		
		//private final Handler mHandler = new Handler();

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams using temp objects
			// because member streams are final;
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];
			int bytes; // bytes returned from read;

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					// Send the obtained bytes to the UI Activity
					
					mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
							.sendToTarget();
					
					
					/*
					mHandler.post(new Runnable() {
						int roll = 0;

						public void run() {
							myLabel.setText(data);
							if (data.split("\\s+").length == 3) {
								roll = Integer.parseInt(data
										.split("\\s+")[2]);
							}
							if ((roll >= 65 && roll <= 90)
									|| (roll <= -70 && roll >= -90)) {
								// down
								TeddyView
										.setImageResource(tedarr[2]);

								if (!mp.isPlaying()) {
									mp.start();
									builder.setMessage("FLIP THE BABY, NOW!!!!!!");

									builder.setPositiveButton(
											"Yes",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													mp.stop();
												}
											});
									AlertDialog alertDialog = builder
											.create();
									alertDialog.show();
								}
							} else if ((roll >= 0 && roll <= 10)
									|| (roll <= 0 && roll >= -10)) {
								// up
								TeddyView
										.setImageResource(tedarr[0]);
								if (mp.isPlaying()) {
									mp.stop();
								}
							} else if (roll > 10 && roll < 65) {
								// right
								TeddyView
										.setImageResource(tedarr[1]);
								if (mp.isPlaying()) {
									mp.stop();
								}
							} else if (roll <= -10
									&& roll > -50) {
								// left
								TeddyView
										.setImageResource(tedarr[3]);
								if (mp.isPlaying()) {
									mp.stop();
								}
							}
						}
					});
					*/
					
					
					
				} catch (IOException e) {
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) {
			}
		}
	}

	/* Call this from the main activity to shutdown the connection */
	public void cancel() {
		try {
			mmSocket.close();
		} catch (IOException e) {
		}
	}
	

	void openBT() throws IOException {
		myLabel.setText("OpenBT");
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard
																				// SerialPortService
																				// ID
		mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
		mmSocket.connect();
		myLabel.setText("Connect");
		mmOutputStream = mmSocket.getOutputStream();
		mmInputStream = mmSocket.getInputStream();

		beginListenForData();

		myLabel.setText("Bluetooth Opened");
		try {
			sendData();
		} catch (IOException ex) {
		}
	}

	void beginListenForData() {
		final Handler handler = new Handler();
		final byte delimiter = 10; // This is the ASCII code for a newline
									// character
		myLabel.setText("Begin Listening");
		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];
		workerThread = new Thread(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted() && !stopWorker) {
					try {
						int bytesAvailable = mmInputStream.available();
						if (bytesAvailable > 0) {
							byte[] packetBytes = new byte[bytesAvailable];
							mmInputStream.read(packetBytes);
							for (int i = 0; i < bytesAvailable; i++) {
								byte b = packetBytes[i];
								if (b == delimiter) {
									byte[] encodedBytes = new byte[readBufferPosition];
									System.arraycopy(readBuffer, 0,
											encodedBytes, 0,
											encodedBytes.length);
									final String data = new String(
											encodedBytes, "US-ASCII");
									readBufferPosition = 0;

									handler.post(new Runnable() {
										int roll = 0;

										public void run() {
											myLabel.setText(data);
											if (data.split("\\s+").length == 3) {
												roll = Integer.parseInt(data
														.split("\\s+")[2]);
											}
											if ((roll >= 65 && roll <= 90)
													|| (roll <= -70 && roll >= -90)) {
												// down
												TeddyView
														.setImageResource(tedarr[2]);

												if (!mp.isPlaying()) {
													mp.start();
													// AlertDialog.Builder
													// builder = new
													// AlertDialog.Builder(context);
													builder.setMessage("FLIP THE BABY, NOW!!!!!!");

													builder.setPositiveButton(
															"Yes",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {
																	// if this
																	// button is
																	// clicked,
																	// close
																	// current
																	// activity
																	mp.stop();
																}
															});
													AlertDialog alertDialog = builder
															.create();
													alertDialog.show();
												}
											} else if ((roll >= 0 && roll <= 10)
													|| (roll <= 0 && roll >= -10)) {
												// up
												TeddyView
														.setImageResource(tedarr[0]);
												if (mp.isPlaying()) {
													mp.stop();
												}
											} else if (roll > 10 && roll < 65) {
												// right
												TeddyView
														.setImageResource(tedarr[1]);
												if (mp.isPlaying()) {
													mp.stop();
												}
											} else if (roll <= -10
													&& roll > -50) {
												// left
												TeddyView
														.setImageResource(tedarr[3]);
												if (mp.isPlaying()) {
													mp.stop();
												}
											}
										}
									});
								} else {
									readBuffer[readBufferPosition++] = b;
								}
							}
						}
					} catch (IOException ex) {
						stopWorker = true;
					}
				}
			}
		});

		workerThread.start();
	}

	void sendData() throws IOException {
		// String msg = myTextbox.getText().toString();
		String msg = "d";
		msg += "\n";
		mmOutputStream.write(msg.getBytes());
		myLabel.setText("Data Sent");
	}

	void closeBT() throws IOException {
		stopWorker = true;
		mmOutputStream.close();
		mmInputStream.close();
		mmSocket.close();
		myLabel.setText("Bluetooth Closed");
	}

}
