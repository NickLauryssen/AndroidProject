package be.xios.activities;

import be.xios.activities.GpsService.LocalBinder;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class MapActivity extends Activity {

	private GpsService gps;
	boolean mBound = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		
		Intent intent = new Intent(this, GpsService.class);
		this.getApplicationContext();
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			gps = binder.getService();
			mBound = true;
		

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;

		}
	};
}
