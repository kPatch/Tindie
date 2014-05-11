package com.activity.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Used to handle Bluetooth connection
 * @author irvincardenas
 *
 */
public class BluetoothManager extends Thread{

	protected static final int REQUEST_ENABLE_BT = 1;
	BluetoothAdapter mBluetoothAdapter;
	
	public BluetoothManager(){
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

}
