package be.xios.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.xios.model.Place;
import be.xios.model.Visited;
import be.xios.places.PlacesSearch;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.text.method.DateTimeKeyListener;
import android.util.Log;

public class GpsService extends Service implements LocationListener {

	private final IBinder mBinder = new LocalBinder();
	private LocationManager locMan;
	private LocationProvider prov;
	private Criteria crit;
	private Location loc = null;
	private List<Place> lijst;
	
	private List<Visited> visitedList;
	private Boolean gpsOn = false;
	private Boolean gpsGotSignal = false;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // meters

	// The minimum time between updates in seconds
	private static final long MIN_TIME_BW_UPDATES = 20;

	private static final int MIN_DISTANCE_FOR_VISIT = 20;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		locMan = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);

		locMan.addGpsStatusListener(gpsListener);
		gpsOn = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

	public void makeConnection() {

		if (gpsOn) {

			String best = locMan.getBestProvider(crit, true);
			prov = locMan.getProvider(best);

			if (prov != null) {
				locMan.requestLocationUpdates(prov.getName(),
						MIN_DISTANCE_CHANGE_FOR_UPDATES, MIN_TIME_BW_UPDATES,
						this);

			} else {
				// NO provider available
			}

			loc = locMan.getLastKnownLocation(best);
		}

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
		placesSearch();
		checkIfVisited();
		

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

	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			if (event == GpsStatus.GPS_EVENT_STARTED) {
				gpsOn = true;
			} else if (event == GpsStatus.GPS_EVENT_STOPPED) {
				gpsOn = false;
			} else if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
				gpsGotSignal = true;
				GpsStatus status = locMan.getGpsStatus(null);
				Iterator<GpsSatellite> sats = status.getSatellites().iterator();
				int i = 0;
				while (sats.hasNext()) {
					sats.next();
					i++;
				}
			//	Log.d("test", "got signal " + i + " satelites");
			}
		}
	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	private void placesSearch() {
		if (this.getLoc() != null) {
			PlacesSearch search = new PlacesSearch(this.getLoc(),
					getApplicationContext());
			setLijst(new ArrayList<Place>());
			try {
				setLijst(search.execute("").get());

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Log.d("test", "No connection");
			setLijst(null);
		}

	}

	public void sendLocationToServer() {

		// huidige locatie doorsturen

	}

	public void findNewPeople() {

		// mensen zoeken in omgeving
	}
	
	
	private void checkIfVisited(){
		//Voorlopige versie: timer moet er nog in.
		if (visitedList == null){
		visitedList = new ArrayList<Visited>();
		}
		
		for(int i = 0; i < this.getLijst().size(); i++){
			if(lijst.get(i).getDistance() < MIN_DISTANCE_FOR_VISIT){
				Visited visit = new Visited(lijst.get(i));
				visitedList.add(visit);
				
			}
			
		}

	}

	public List<Place> getLijst() {
		return lijst;
	}

	public void setLijst(List<Place> lijst) {
		this.lijst = lijst;
	}

	
	public List<Visited> getVisitedList() {
		return visitedList;
	}
	public void setVisitedList(List<Visited> visitedList) {
		this.visitedList = visitedList;
	}
	
	public class LocalBinder extends Binder {
		GpsService getService() {
			return GpsService.this;
		}
	}

	public Location getLoc() {
		return loc;
	}

	public void setLoc(Location loc) {
		this.loc = loc;
	}

	public Boolean getGpsOn() {
		return gpsOn;
	}

	public void setGpsOn(Boolean gpsOn) {
		this.gpsOn = gpsOn;
	}

	public Boolean getGpsGotSignal() {
		return gpsGotSignal;
	}

	public void setGpsGotSignal(Boolean gpsGotSignal) {
		this.gpsGotSignal = gpsGotSignal;
	}

}
