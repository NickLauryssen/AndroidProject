package be.xios.activities;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import be.xios.model.CustomButton;

public class CustomAdapter extends ArrayAdapter<String> 
{
	private List<CustomButton> values;
	
	public CustomAdapter(Context context, List<CustomButton> values) 
	{
		super(context,R.layout.neutral_button,new String[values.size()]);
		this.values = values;
	}
	
    @Override
	public View getView(int position, View convertView, ViewGroup parent)
    {
    	LayoutInflater infl = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	//Declare Controls
    	View rowView = infl.inflate(R.layout.neutral_button, parent, false);
    	ImageView iv_icon = (ImageView) rowView.findViewById(R.id.button_icon);
    	TextView tv_title = (TextView) rowView.findViewById(R.id.button_title);
    	TextView tv_description = (TextView) rowView.findViewById(R.id.button_description);
    	RelativeLayout rl_button = (RelativeLayout) rowView.findViewById(R.id.button);

    	//Set Fonts
    	Typeface allerDisplay = Typeface.createFromAsset(getContext().getAssets(), "fonts/AllerDisplay.ttf");
    	Typeface allerLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Aller_Lt.ttf");
    	tv_title.setTypeface(allerDisplay);
    	tv_description.setTypeface(allerLight);
    	
    	//Set Contents
    	String title = this.values.get(position).getTitle();
    	String description = this.values.get(position).getDescription();
    	String background = this.values.get(position).getType();
    	int icon = this.values.get(position).getIcon();
    	
    	if(!background.equals("normal") && background.equals("flashy"))
    	{
    		rl_button.setBackgroundResource(R.drawable.btn_green);
    		tv_title.setTextColor(Color.WHITE);
    		tv_description.setTextColor(Color.WHITE);
    	}
    	
    	iv_icon.setImageResource(icon);
    	tv_title.setText(title);
    	tv_description.setText(description);
    	
    	rowView.setTag(this.values.get(position));
    	
    	return rowView;
    }
}
