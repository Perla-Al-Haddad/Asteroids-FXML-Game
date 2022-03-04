package application;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public abstract class KineticBody {
	private Vector velocity;
	public CollisionMask boundary;
	public Vector position;
	public double rotation;
	
	public KineticBody() {
		this.position = new Vector();
		this.boundary = new CollisionMask();
		this.velocity = new Vector();
		this.rotation = 0;
	}
	
	public abstract void render(GraphicsContext context);
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public boolean overlaps(KineticBody other) {
		return this.boundary.overlaps(other.boundary);
	}
	
	public void setBoundary() {
		this.boundary.setPosition(
				this.position.getX()-this.boundary.getWidth()/2, 
				this.position.getY()-this.boundary.getHeight()/2
				);
	}
	
	public void update(double deltaTime) {
		this.position.add(this.velocity.getX() * deltaTime, this.velocity.getY() * deltaTime);	
		this.setBoundary();
	}
	
	public void wrap(Canvas canvas) {
		double halfWidth = this.boundary.getWidth()/2;
		double halfHeight = this.boundary.getHeight()/2;
		if (this.position.getX() > canvas.getWidth() + halfWidth) 
			this.position.setCoords(-halfWidth, this.position.getY());
	    else if (this.position.getX() + halfWidth < 0) 
	    	this.position.setCoords(canvas.getWidth() + halfWidth, this.position.getY());
		if (this.position.getY() > canvas.getHeight() + halfHeight) 
			this.position.setCoords(this.position.getX(), -halfHeight);
	    else if (this.position.getY() + halfHeight < 0) 
	    	this.position.setCoords(this.position.getX(), canvas.getHeight() + halfHeight);
	}
	
}
