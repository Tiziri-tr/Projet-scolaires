package meta;

public class Dance {
	private double qualite;
	private double distance;

	public Dance() {
		qualite = 0;
		distance = 0;
	}

	public Dance(double qualite, double distance) {
		super();
		this.qualite = qualite;
		this.distance = distance;
	}

	public double getQualite() {
		return qualite;
	}

	public void setQualite(double qualite) {
		this.qualite = qualite;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	
}
