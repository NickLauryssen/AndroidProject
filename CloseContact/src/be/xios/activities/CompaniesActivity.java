package be.xios.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

import android.util.Log;
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
	private ListView lv_array;
	private CustomAdapter custAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_buttonlist);

		intent = new Intent(this, GpsService.class);

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

	private class ListViewHandler implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
			if (v.getTag().getClass() == CustomButton.class) {
				CustomButton cb = (CustomButton) v.getTag();
				Class<?> link = cb.getLink();
				if (cb.getTitle().equals("Refresh")) {
					addPlaces();

				} else if (cb.getTitle().equals("Show map")) {

					Intent mapIntent = new Intent(getApplicationContext(), link);
					startActivity(mapIntent);
				}

			}

		}
	}


	private void addPlaces() {

		listButtons = new ArrayList<CustomButton>();
		if (gps.getGpsGotSignal()) {
			lijst = gps.getLijst();
			for (int i = 0; i < lijst.size(); i++) {

				listButtons.add(new CustomButton(lijst.get(i).getName(), lijst
						.get(i).getDistance() + "m", PeopleActivity.class,
						R.drawable.ic_factory, "normal"));
			}
			listButtons.add(new CustomButton("Show map",
					"View companies on a map", MapActivity.class,
					R.drawable.ic_settings, "flashy"));
		} else {

			listButtons.add(new CustomButton("GPS Signal",
					"No satelites found", MainMenuActivity.class,
					R.drawable.ic_factory, "normal"));
		}
		listButtons.add(new CustomButton("Refresh", "Search for new companies",
				MainMenuActivity.class, R.drawable.ic_settings, "flashy"));

		custAd = new CustomAdapter(this, listButtons);
		lv_array = (ListView) findViewById(R.id.listViewMainMenu);
		lv_array.setAdapter(custAd);

		lv_array.setOnItemClickListener(new ListViewHandler());

	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			gps = binder.getService();
			mBound = true;
			gps.makeConnection();
			addPlaces();

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;

		}
	};

}
