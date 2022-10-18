package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

public class ShowRoute extends EasyGraphics {

	private static int MARGIN = 50;
	private static int MAPXSIZE = 800;
	private static int MAPYSIZE = 800;

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;

	public ShowRoute() {

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run() {

		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(+MAPYSIZE);

		showStatistics();
	}

	public double xstep() {

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		double xstep = MAPXSIZE / (Math.abs(maxlon - minlon));
		return xstep;
	}

	public double ystep() {

		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));
		double ystep = MAPYSIZE / (Math.abs(maxlat - minlat));
		return ystep;
	}

	static int RADIUS = 3;

	public void showRouteMap(int ybase) {
		double xstart = MARGIN + gpspoints[0].getLongitude();
		double x = xstart;
		double y = 2 * MARGIN + gpspoints[0].getLatitude();

		if (gpspoints.length > 110 && gpspoints.length < 190) {
			y += 3 * MARGIN;
		}
		if (gpspoints.length > 420) {
			x += 3 * MARGIN;
			y += 4 * MARGIN;
		}

		setColor(0, 255, 0);
		fillCircle((int) x, (int) y, RADIUS);
		for (int i = 1; i < gpspoints.length; i++) {
			double x1 = x;
			double y1 = y;
			double x2 = (gpspoints[i].getLongitude() - gpspoints[i - 1].getLongitude()) * xstep();
			double y2 = (gpspoints[i - 1].getLatitude() - gpspoints[i].getLatitude()) * ystep();
			x += x2;
			y += y2;

			setColor(0, 255, 0);
			fillCircle((int) x, (int) y, RADIUS);

			drawLine((int) x1, (int) y1, (int) x, (int) y);
			x1 = x;
			y1 = y;

		}

		x = xstart;
		y = 2 * MARGIN + gpspoints[0].getLatitude();
		if (gpspoints.length > 110 && gpspoints.length < 190) {
			y += 3 * MARGIN;
		}
		if (gpspoints.length > 420) {
			x += 3 * MARGIN;
			y += 4 * MARGIN;
		}
		setColor(0, 0, 255);
		setSpeed(1);
		int blåSirkel = fillCircle((int) x, (int) y, RADIUS * 2);

		for (int i = 1; i < gpspoints.length; i++) {
			double x2 = (gpspoints[i].getLongitude() - gpspoints[i - 1].getLongitude()) * xstep();
			double y2 = (gpspoints[i - 1].getLatitude() - gpspoints[i].getLatitude()) * ystep();
			x += x2;
			y += y2;
			moveCircle(blåSirkel, (int) x, (int) y);
		}
	}

	public void showStatistics() {

		int margin = MARGIN;

		int TEXTDISTANCE = 20;

		if (gpspoints.length > 420) {
			margin = 600;
		}

		String totaltime = "Total Time" + "     : " + GPSUtils.formatTime(gpscomputer.totalTime());
		String totalelevation = "Total Elevation:" + GPSUtils.formatDouble(gpscomputer.totalElevation()) + "0 m";
		String maxspeed = "Max speed" + "      : " + GPSUtils.formatDouble(gpscomputer.maxSpeed()) + " km/t";
		String average = "Average speed" + "  : " + GPSUtils.formatDouble(gpscomputer.averageSpeed()) + " km/t";
		String energy = "Energy" + "         : " + GPSUtils.formatDouble(gpscomputer.totalKcal(80)) + " kcal";

		setColor(0, 0, 0);
		setFont("Courier", 11);
		drawString(totaltime, margin, TEXTDISTANCE);
		TEXTDISTANCE += 20;
		drawString(totalelevation, margin, TEXTDISTANCE);
		TEXTDISTANCE += 20;
		drawString(maxspeed, margin, TEXTDISTANCE);
		TEXTDISTANCE += 20;
		drawString(average, margin, TEXTDISTANCE);
		TEXTDISTANCE += 20;
		drawString(energy, margin, TEXTDISTANCE);

	}

}
