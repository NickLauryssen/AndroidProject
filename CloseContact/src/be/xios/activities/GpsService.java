package be.xios.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.xios.model.Place;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class GpsService extends Service implements LocationListener {

	private final IBinder mBinder = new LocalBinder();
	private LocationManager locMan;
	private LocationProvider prov;
	private Criteria crit;
	private Location loc = null;
	private List<Place> lijst;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 5000;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		locMan = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);

		String best = locMan.getBestProvider(crit, true);
		prov = locMan.getProvider(best);

		if (prov != null) {
			locMan.requestLocationUpdates(prov.getName(),
					MIN_DISTANCE_CHANGE_FOR_UPDATES, MIN_TIME_BW_UPDATES, this);

		} else {
			// NO provider available
		}

		loc = locMan.getLastKnownLocation(best);
		
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		if (locMan != null) {
			locMan.removeUpdates(this);
		}
	}

	@Override
	public IBinder onBind(Intent i) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onLocationChanged(Location l) {
		// this.loc = new LatLng(l.getLatitude(), l.getLongitude());
		this.loc = l;

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public void placesSearch() {
		if (this.getLoc() != null) {
			PlacesSearch search = new PlacesSearch(this.getLoc(),
					getApplicationContext());
			lijst = new ArrayList<Place>();
			try {
				lijst = search.execute("").get();
				Log.d("test",lijst.get(1).getName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Log.d("test","No connection");
		}
	}

	public class LocalBinder extends Binder {
		GpsService getService() {
			return GpsService.this;
		}
	}

}
