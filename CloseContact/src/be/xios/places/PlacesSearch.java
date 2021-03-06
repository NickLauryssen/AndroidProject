package be.xios.places;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import be.xios.model.Place;

public class PlacesSearch extends AsyncTask<String, Void, List<Place>> {
	private Location l;
	private List<Place> plaatsLijst;
	
	InputStream is = null;
	String result = "";
	JSONObject jsonObject = null;

	public PlacesSearch(Location l, Context c) {
		this.l = l;
		this.plaatsLijst = new ArrayList<Place>();
	}

	@Override
	protected List<Place> doInBackground(String... params) {

		String url = "https://maps.googleapis.com/maps/api/place/search/json?location="
				+ l.getLatitude() + "," + l.getLongitude() + "&radius=300"
				+ "&sensor=true&key=AIzaSyA0rszT72tKjJOdguQOfTEpMgl8vR1bVOA";
		// HTTP
		try {
			HttpClient httpclient = new DefaultHttpClient(); // for port 80
																// requests!
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Read response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			
			JSONObject jObject = new JSONObject(result);
			JSONArray jArray = jObject.getJSONArray("results");

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject oneObject = jArray.getJSONObject(i);

				String name = oneObject.getString("name");
				String id = oneObject.getString("id");
				String vicinity = oneObject.getString("vicinity");
				JSONArray types = oneObject.getJSONArray("types");
				String type = types.getString(0);
				JSONObject loc = oneObject.optJSONObject("geometry")
						.optJSONObject("location");

				String lat = loc.getString("lat");

				String lng = loc.getString("lng");
				int dist = distFrom(Double.parseDouble(lat), Double.parseDouble(lng), l.getLatitude(), l.getLongitude());
				
				Place place = new Place(id, name, lat, lng, vicinity, type,dist);
				plaatsLijst.add(place);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return plaatsLijst;
	}

	//afstand berekenen
	private static int distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    int dist = (int) ( (earthRadius * c) * 1000);
	    
	    return dist;
	    
	}
	

	@Override
	protected void onPostExecute(List<Place> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

	}

}
