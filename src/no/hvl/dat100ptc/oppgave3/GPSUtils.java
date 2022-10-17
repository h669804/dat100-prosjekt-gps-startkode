package no.hvl.dat100ptc.oppgave3;

import static java.lang.Math.*;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;

public class GPSUtils {

	public static double findMax(double[] da) {

		double max; 
		
		max = da[0];
		
		for (double d : da) {
			if (d > max) {
				max = d;
			}
		}
		return max;
	}

	public static double findMin(double[] da) {

		double min;

		min = da[0];
		
		for(double d : da) {
			if(d < min) {
				min = d;
			}
		}
		return min;
	}

	public static double[] getLatitudes(GPSPoint[] gpspoints) {

		double [] latitudes = new double [gpspoints.length];
		
		for (int i = 0; i < gpspoints.length; i++) {
			latitudes[i] = gpspoints[i].getLatitude();
		}
		return latitudes;
	}

	public static double[] getLongitudes(GPSPoint[] gpspoints) {

		double [] longitudes = new double [gpspoints.length];
		
		for (int i = 0; i < gpspoints.length; i++) {
			longitudes[i] = gpspoints[i].getLongitude();
		}
		return longitudes;
	}

	private static int R = 6371000; // jordens radius

	public static double distance(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		double d;
		double latitude1, longitude1, latitude2, longitude2;
		latitude1 = Math.toRadians(gpspoint1.getLatitude());
		latitude2 = Math.toRadians(gpspoint2.getLatitude());
		longitude1 = Math.toRadians(gpspoint1.getLongitude());
		longitude2 = Math.toRadians(gpspoint2.getLongitude());
		
		double deltaLatitude = latitude2 - latitude1;
		double deltaLongitude= longitude2 - longitude1;
		
		double a = Math.pow((Math.sin(deltaLatitude / 2)), 2) + (Math.cos(latitude1) * (Math.cos(latitude2) * (Math.pow((Math.sin(deltaLongitude / 2)), 2))));
		double c =  2 * Math.atan2((Math.sqrt(a)), Math.sqrt(1 - a));
		
		d = R * c;
		
		return d;
	}

	public static double speed(GPSPoint gpspoint1, GPSPoint gpspoint2) {

		//speed = distance / time
		double distance = distance(gpspoint1, gpspoint2);
		int time = gpspoint2.getTime() - gpspoint1.getTime();
		double speed = distance / time * 3.6; //m/s to km/t
		
		return speed;

	}

	public static String formatTime(int secs) {
		
		int h = secs / 3600;
		int m = (secs / 60) % 60;
		int s = secs % 60;
		
		String hours = "" + h;
		String minutes = "" + m;
		String seconds = "" + s;

		String timestr = null;
		
		if(hours.length() == 1) {
			hours = "0" + hours;
		}
		if(minutes.length() == 1) {
			minutes = "0" + minutes;
		}
		if(seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		timestr = "  " + hours + ":" + minutes + ":" + seconds;

		return timestr;
	}
	private static int TEXTWIDTH = 10;

	public static String formatDouble(double d) {
		
		
		String str =  Double.toString(Math.round(d*100.0) /100.0);
		
		while(str.length() != TEXTWIDTH) {
			str = " " + str;
		}
		return str;
	}
}
