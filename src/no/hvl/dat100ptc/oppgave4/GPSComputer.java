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
	
	// beregn total distances (i meter)
	public double totalDistance() {

		double distance = 0;
		
		for(int i = 1; i < gpspoints.length; i++) {
			distance += GPSUtils.distance(gpspoints[i-1], gpspoints[i]);
		}

		return distance;

	}

	// beregn totale høydemeter (i meter)
	public double totalElevation() {

		double elevation = 0;
		
		for (int i = 1; i < gpspoints.length; i++) {
			if(gpspoints[i].getElevation() > gpspoints[i-1].getElevation()) {
			elevation += gpspoints[i].getElevation() - gpspoints[i-1].getElevation();
			}
		}
		
		return elevation;
	}

	// beregn total tiden for hele turen (i sekunder)
	public int totalTime() {
		
		int totalTid = gpspoints[gpspoints.length-1].getTime() - gpspoints[0].getTime();
		return totalTid;
	}
		
	// beregn gjennomsnitshastighets mellom hver av gps punktene

	public double[] speeds() {
		
		double[] fartsPunkt = new double[gpspoints.length-1];
		
		for(int i = 1; i < gpspoints.length; i++) {
			fartsPunkt [i-1] = GPSUtils.speed(gpspoints[i-1], gpspoints[i]);
		}
		return fartsPunkt;
	}
	
	public double maxSpeed() {
		
		double maxspeed = 0;
		
		for(int i = 1; i < gpspoints.length; i++) {
			double fartPunkt = GPSUtils.speed(gpspoints[i-1], gpspoints[i]);
			if(maxspeed < fartPunkt) {
				maxspeed = fartPunkt;
			}
		}
		return maxspeed;
	}

	public double averageSpeed() {

		double average = 0;
		double distance = 0;
		double totalTid = totalTime();
		
		for(int i = 1; i < gpspoints.length; i++) {
			distance += GPSUtils.distance(gpspoints[i-1], gpspoints[i]);
		}
		average = (distance / totalTid) *3.6; //m/s til km/t (*3.6)
		
		return average;
		
	}

	/*
	 * bicycling, <10 mph, leisure, to work or for pleasure 4.0 bicycling,
	 * general 8.0 bicycling, 10-11.9 mph, leisure, slow, light effort 6.0
	 * bicycling, 12-13.9 mph, leisure, moderate effort 8.0 bicycling, 14-15.9
	 * mph, racing or leisure, fast, vigorous effort 10.0 bicycling, 16-19 mph,
	 * racing/not drafting or >19 mph drafting, very fast, racing general 12.0
	 * bicycling, >20 mph, racing, not drafting 16.0
	 */

	// conversion factor m/s to miles per hour
	public static double MS = 2.236936;

	// beregn kcal gitt weight og tid der kjøres med en gitt hastighet
	public double kcal(double weight, int secs, double speed) {

		double kcal;
		double hours = Double.valueOf(secs) / 3600;

		// MET: Metabolic equivalent of task angir (kcal x kg-1 x h-1) kcal = MET * kg * h
		double met = 4.0;		
		double speedmph = speed * MS;
		
		if(speedmph >= 10.0) {
			met = 6.0;
		}
		if(speedmph >= 12.0) {
			met = 8.0;
		}
		if(speedmph >= 14.0) {
			met = 10.0;
		}
		if(speedmph >= 16.0) {
			met = 12.0;
		}
		if(speedmph >= 20.0) {
			met = 16.0;
		}
		
		kcal = met * weight * hours;
		
		return kcal;
		
	}

	public double totalKcal(double weight) {

		double totalkcal = 0;
		double[] speeds = speeds();

		int[] times = new int[speeds.length];
		
		for(int i = 1; i < gpspoints.length; i++) {
			times[i-1] = gpspoints[i].getTime() - gpspoints[i-1].getTime();
		}
	
		
		for(int i = 0; i < times.length; i++) {
			totalkcal += kcal(weight, times[i], speeds[i]);
		}
		return totalkcal;
	}
	
	private static double WEIGHT = 80.0;
	
	public void displayStatistics() {
		
		
		String distance = " " + GPSUtils.formatDouble(totalDistance()/1000);
		String elevation = "" + GPSUtils.formatDouble(totalElevation());
		String maxspeed = " " + GPSUtils.formatDouble(maxSpeed());
		String average = " " + GPSUtils.formatDouble(averageSpeed());
		String energy = " " + GPSUtils.formatDouble(totalKcal(WEIGHT));

		System.out.println("==============================================");
		System.out.println("Total Time" + "     : " + GPSUtils.formatTime(totalTime()));
		System.out.print("Total distance" + " :" + distance); System.out.println(" km");
		System.out.print("Total elevation:" + elevation); System.out.println("0 m");
		System.out.print("Max speed" + "      :" + maxspeed); System.out.println(" km/t");
		System.out.print("Average speed" + "  :" + average); System.out.println(" km/t");
		System.out.print("Energy" + "         :" + energy); System.out.println(" kcal");
		System.out.println("==============================================");
		
		
		
	}
	
	//stigningsprosent = høydeforskjell / distanse
	public double [] climbs(){
		double[] distanse = new double [gpspoints.length-1];
		double[] elevations = new double [gpspoints.length-1];
		
		for (int i = 1; i < gpspoints.length; i++) {
			distanse[i-1] = GPSUtils.distance(gpspoints[i-1], gpspoints[i]);
			elevations[i-1] = gpspoints[i].getElevation() - gpspoints[i-1].getElevation();
		}
		double stigningsprosenter[] = new double [distanse.length];
		for (int i = 0; i < distanse.length; i++) {
			stigningsprosenter[i] = (elevations[i] / distanse[i]) * 100;
			System.out.println(stigningsprosenter[i] + " %");
		}
		return stigningsprosenter;
	}
	public double maxClimb() {
	double hoyestStigning = 0;
	double stigninger[] = climbs();
	
	for(int i = 0; i < stigninger.length; i++) {
		if(hoyestStigning < stigninger[i]) {
			hoyestStigning = stigninger[i];
		}
	}
	return hoyestStigning;
}
}
