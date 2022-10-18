package no.hvl.dat100ptc.oppgave4;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;

public class GPSComputer {

	private GPSPoint[] gpspoints;

	public GPSComputer(String filename) {

		GPSData gpsdata = GPSDataFileReader.readGPSFile(filename);
		gpspoints = gpsdata.getGPSPoints();

	}

	public GPSComputer(GPSPoint[] gpspoints) {
		this.gpspoints = gpspoints;
	}

	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}

	public double totalDistance() {

		double distance = 0;

		for (int i = 1; i < gpspoints.length; i++) {
			distance += GPSUtils.distance(gpspoints[i - 1], gpspoints[i]);
		}
		return distance;

	}

	public double totalElevation() {

		double elevation = 0;

		for (int i = 1; i < gpspoints.length; i++) {
			if (gpspoints[i].getElevation() > gpspoints[i - 1].getElevation()) {
				elevation += gpspoints[i].getElevation() - gpspoints[i - 1].getElevation();
			}
		}

		return elevation;
	}

	public int totalTime() {

		int totalTid = gpspoints[gpspoints.length - 1].getTime() - gpspoints[0].getTime();
		return totalTid;
	}

	public double[] speeds() {

		double[] fartsPunkt = new double[gpspoints.length - 1];

		for (int i = 1; i < gpspoints.length; i++) {
			fartsPunkt[i - 1] = GPSUtils.speed(gpspoints[i - 1], gpspoints[i]);
		}
		return fartsPunkt;
	}

	public double maxSpeed() {
		double[] speeds = speeds();
		double maxspeed = GPSUtils.findMax(speeds);
		return maxspeed;
	}

	public double averageSpeed() {

		double distance = totalDistance();
		double totalTid = totalTime();
		double average = (distance / totalTid) * 3.6;

		return average;

	}

	public static double MS = 2.236936;

	public double kcal(double weight, int secs, double speed) {

		double kcal;
		double hours = (double) secs / 3600;
		double met = 4.0;
		double speedmph = speed * MS;

		if (speedmph >= 10.0) {
			met = 6.0;
		}
		if (speedmph >= 12.0) {
			met = 8.0;
		}
		if (speedmph >= 14.0) {
			met = 10.0;
		}
		if (speedmph >= 16.0) {
			met = 12.0;
		}
		if (speedmph >= 20.0) {
			met = 16.0;
		}

		kcal = met * weight * hours;

		return kcal;

	}

	public double totalKcal(double weight) {

		double totalkcal = 0;
		double[] speeds = speeds();

		int[] times = new int[speeds.length];

		for (int i = 1; i < gpspoints.length; i++) {
			times[i - 1] = gpspoints[i].getTime() - gpspoints[i - 1].getTime();
		}

		for (int i = 0; i < times.length; i++) {
			totalkcal += kcal(weight, times[i], speeds[i]);
		}
		return totalkcal;
	}

	private static double WEIGHT = 80.0;

	public void displayStatistics() {

		String distance = " " + GPSUtils.formatDouble(totalDistance() / 1000);
		String elevation = "" + GPSUtils.formatDouble(totalElevation());
		String maxspeed = " " + GPSUtils.formatDouble(maxSpeed());
		String average = " " + GPSUtils.formatDouble(averageSpeed());
		String energy = " " + GPSUtils.formatDouble(totalKcal(WEIGHT));

		System.out.println("==============================================");
		System.out.println("Total Time" + "     : " + GPSUtils.formatTime(totalTime()));
		System.out.print("Total distance" + " :" + distance);
		System.out.println(" km");
		System.out.print("Total elevation:" + elevation);
		System.out.println("0 m");
		System.out.print("Max speed" + "      :" + maxspeed);
		System.out.println(" km/t");
		System.out.print("Average speed" + "  :" + average);
		System.out.println(" km/t");
		System.out.print("Energy" + "         :" + energy);
		System.out.println(" kcal");
		System.out.println("==============================================");

	}

	public double[] climbs() {
		double[] distanse = new double[gpspoints.length - 1];
		double[] elevations = new double[gpspoints.length - 1];

		for (int i = 1; i < gpspoints.length; i++) {
			distanse[i - 1] = GPSUtils.distance(gpspoints[i - 1], gpspoints[i]);
			elevations[i - 1] = gpspoints[i].getElevation() - gpspoints[i - 1].getElevation();
		}
		double stigningsprosenter[] = new double[distanse.length];
		for (int i = 0; i < distanse.length; i++) {
			stigningsprosenter[i] = (elevations[i] / distanse[i]) * 100;
			System.out.println(stigningsprosenter[i] + " %");
		}
		return stigningsprosenter;
	}

	public double maxClimb() {

		double stigninger[] = climbs();
		double hoyestStigning = GPSUtils.findMax(stigninger);

		return hoyestStigning;
	}
}
