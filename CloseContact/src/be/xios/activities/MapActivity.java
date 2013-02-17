package be.xios.activities;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import be.xios.activities.GpsService.LocalBinder;
import be.xios.model.Place;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class MapActivity extends Activity {

	private GpsService gps;
	boolean mBound = false;
	private GoogleMap mMap;
	private List<Place> lijst;

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

	private void drawMarkers() {
		if (gps.getGpsGotSignal()) {
			lijst = gps.getLijst();
			LatLng loc = null;
			for (int i = 0; i < lijst.size(); i++) {
				loc = new LatLng(lijst.get(i).getLatitude(), lijst.get(i)
						.getLongitude());

				mMap.addMarker(new MarkerOptions().position(loc)
						.title(lijst.get(i).getName())
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
						.snippet("Show card"));

			}
			mMap.setOnInfoWindowClickListener(new ClickHandler());

		}

	}
	
	
	private class ClickHandler implements OnInfoWindowClickListener{

		@Override
		public void onInfoWindowClick(Marker m) {
			Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
			
			
		}
		
		
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			gps = binder.getService();
			mBound = true;
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			if (gps.getLatlngLoc() != null) {
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
						gps.getLatlngLoc(), 15));
				drawMarkers();
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;

		}
	};
}
