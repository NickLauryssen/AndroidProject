package be.xios.model;

import java.util.Date;

public class Visited {

	private Place place;
	private Date time;
	
	public Visited(Place place) {
		this.place = place;
		this.time = new Date();
	}
	
	
	public Place getPlace() {
		return place;
	}
	public void setPlace(Place place) {
		this.place = place;
	}
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
