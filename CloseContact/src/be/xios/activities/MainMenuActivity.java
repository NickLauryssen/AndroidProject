package be.xios.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import be.xios.activities.GpsService.LocalBinder;
import be.xios.model.CustomButton;

public class MainMenuActivity extends Activity {
	private List<CustomButton> listButtons;
	private Intent intent;
	GpsService gps;
	boolean mBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_buttonlist);

		intent = new Intent(this, GpsService.class);
		this.getApplicationContext();
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

		listButtons = new ArrayList<CustomButton>();
		listButtons.add(new CustomButton("Find new people",
				"Search for contacts that are nearby", PeopleActivity.class,
				R.drawable.ic_people, "normal"));
		listButtons.add(new CustomButton("Find new companies",
				"Search for companies that are nearby",
				CompaniesActivity.class, R.drawable.ic_factory, "normal"));
		listButtons.add(new CustomButton("Recently visited",
				"Find places you recently visited", VisitedActivity.class,
				R.drawable.ic_location, "normal"));
		listButtons.add(new CustomButton("My contacts", "Your saved contacts",
				ContactsActivity.class, R.drawable.ic_contacts, "normal"));
		listButtons.add(new CustomButton("My business card",
				"Edit your own business card", EditCardActivity.class,
				R.drawable.ic_business_card, "normal"));
		listButtons.add(new CustomButton("Preferences", "Change your settings",
				SettingsActivity.class, R.drawable.ic_settings, "flashy"));

		CustomAdapter custAd = new CustomAdapter(this, this.listButtons);
		ListView lv_array = (ListView) findViewById(R.id.listViewMainMenu);
		lv_array.setAdapter(custAd);

		lv_array.setOnItemClickListener(new ListViewHandler());

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
			enableGps();
			gps.makeConnection();

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

			mBound = false;
		}

	};

	private void enableGps() {
		if (gps.getGpsOn() == false) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("GPS").setIcon(R.drawable.ic_launcher)
					.setMessage("Please enable GPS");

			builder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							finish();
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							finish();
						}
					});

			AlertDialog dialog = builder.create();
			dialog.show();

		}

	}

}
