package be.xios.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import be.xios.activities.GpsService.LocalBinder;
import be.xios.model.CustomButton;
import be.xios.model.Place;

public class CompaniesActivity extends Activity {
	private List<CustomButton> listButtons;
	private Intent intent;
	private GpsService gps;
	boolean mBound = false;
	private List<Place> lijst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_buttonlist);

		listButtons = new ArrayList<CustomButton>();
		listButtons.add(new CustomButton("Xios Hogeschool", "You're here",
				MainMenuActivity.class, R.drawable.ic_school, "normal"));
		listButtons.add(new CustomButton("Uhasselt", "500m",
				MainMenuActivity.class, R.drawable.ic_school, "normal"));
		listButtons.add(new CustomButton("KHLim", "800m",
				MainMenuActivity.class, R.drawable.ic_school, "normal"));
		listButtons.add(new CustomButton("Show map", "-",
				MainMenuActivity.class, R.drawable.ic_location, "flashy"));

		CustomAdapter custAd = new CustomAdapter(this, this.listButtons);
		ListView lv_array = (ListView) findViewById(R.id.listViewMainMenu);
		lv_array.setAdapter(custAd);

		lv_array.setOnItemClickListener(new ListViewHandler());

		intent = new Intent(this, GpsService.class);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

	private class ListViewHandler implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (arg1.getTag().getClass() == CustomButton.class) {
				CustomButton cb = (CustomButton) arg1.getTag();
				Class<?> link = cb.getLink();

				Intent peopleIntent = new Intent(getApplicationContext(), link);
				startActivity(peopleIntent);
			}
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			gps = binder.getService();
			mBound = true;
			//gps.placesSearch();
			
			

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;

		}
	};

	
}
