package com.uniovi.entities;

import javax.persistence.Embeddable;

@Embeddable
public class LatLng {
	
	public double latitude;
	public double longitude;
	
	public LatLng() {}
	
	public LatLng(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Location{Latitude='"+latitude+"',"+
				"Longitude='"+longitude+"'}";
	}
	
}
