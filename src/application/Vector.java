package application;

public class Vector {
	private double x;
	private double y;
	
	public Vector() {
		this.setCoords(0, 0);
	}
	public Vector(double x, double y) {
		this.setCoords(x, y);
	}
	
	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + " ]";
	}

	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public void setCoords(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(double dx, double dy) {
		this.x += dx;
		this.y += dy;
	}
	public void multiply(double m) {
		this.x *= m;
		this.y *= m;
	}
	
	public double getLength() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	public void setLength(double Length) {
		double currentLength = this.getLength();
		if (currentLength == 0) {
			this.setCoords(Length, 0);
		} else {
			this.multiply(1/currentLength);
			this.multiply(Length);
		}
	}
	
	public double getAngle() {
		return Math.atan2(this.y, this.x);
	}
	public void setAngle(double angleDegrees) {
		double L = this.getLength();
		double angleRadians = Math.toRadians(angleDegrees);
		this.x = L * Math.cos(angleRadians);
		this.y = L * Math.sin(angleRadians);	
	}
}
