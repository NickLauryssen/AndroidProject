package be.xios.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import be.xios.model.CustomButton;

public class MainMenuActivity extends Activity 
{
	private List<CustomButton> listButtons;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buttonlist);
        
        listButtons = new ArrayList<CustomButton>();
        listButtons.add(new CustomButton("Find new people", "Search for contacts that are nearby", PeopleActivity.class, R.drawable.ic_people, "normal"));
        listButtons.add(new CustomButton("Find new companies", "Search for companies that are nearby", CompaniesActivity.class, R.drawable.ic_factory, "normal"));
        listButtons.add(new CustomButton("Recently visited", "Find places you recently visited", VisitedActivity.class, R.drawable.ic_location, "normal"));
        listButtons.add(new CustomButton("My contacts", "Your saved contacts", ContactsActivity.class, R.drawable.ic_contacts, "normal"));
        listButtons.add(new CustomButton("My business card", "Edit your own business card", EditCardActivity.class, R.drawable.ic_business_card, "normal"));
        listButtons.add(new CustomButton("Preferences", "Change your settings", SettingsActivity.class, R.drawable.ic_settings, "flashy"));
                
        CustomAdapter custAd = new CustomAdapter(this, this.listButtons);
        ListView lv_array = (ListView) findViewById(R.id.listViewMainMenu);
        lv_array.setAdapter(custAd);
        
        lv_array.setOnItemClickListener(new ListViewHandler());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
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
}
