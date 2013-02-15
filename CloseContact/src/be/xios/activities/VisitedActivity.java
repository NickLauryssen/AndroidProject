package be.xios.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import be.xios.activities.GpsService.LocalBinder;
import be.xios.model.CustomButton;
import be.xios.model.Visited;

public class VisitedActivity extends Activity 
{
	private List<CustomButton> listButtons;
	private GpsService gps;
	boolean mBound = false;
	private Intent intent;
	private List<Visited> visitedList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buttonlist);
        
		intent = new Intent(this, GpsService.class);
		this.getApplicationContext();
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}
	
	private void addVisitedPlaces(){
		
	    listButtons = new ArrayList<CustomButton>();
	    visitedList = gps.getVisitedList();
	    
	    if (visitedList == null || visitedList.size() == 0){
	    	listButtons.add(new CustomButton("No recent visits", "", MainMenuActivity.class, R.drawable.ic_settings, "normal"));
	        
	    } else {
	    	for (int i = 0 ; i <visitedList.size() ; i++){
	    		 listButtons.add(new CustomButton(visitedList.get(i).getPlace().getName(), visitedList.get(i).getTime().toString(), MainMenuActivity.class, R.drawable.ic_store, "normal"));
	             
	    	}
	    }
              
        CustomAdapter custAd = new CustomAdapter(this, this.listButtons);
        ListView lv_array = (ListView) findViewById(R.id.listViewMainMenu);
        lv_array.setAdapter(custAd);
        
        lv_array.setOnItemClickListener(new ListViewHandler());
		
		
	}

    private class ListViewHandler implements OnItemClickListener
    {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
		{			
			if(arg1.getTag().getClass() == CustomButton.class)
			{
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
			addVisitedPlaces();

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;

		}
	};
    
}
