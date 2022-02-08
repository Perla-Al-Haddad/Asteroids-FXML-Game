package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class CollisionMask extends Rectangle {
	
	public CollisionMask() {
		this.setPosition(0, 0);
		this.setSize(1, 1);
	}
	
	public void setPosition(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	
	public void setSize(double width, double height) {
		this.setWidth(width);
		this.setHeight(height);
	}
	
	// Collision
	public boolean overlaps(CollisionMask other) {
		// Collision algorithm
		boolean noOverlap = this.getX() + this.getWidth() < other.getX() ||
				other.getX() + other.getWidth() < this.getX() || 
				this.getY() + this.getHeight() < other.getY() ||
				other.getY() + other.getHeight() < this.getY();
		return !noOverlap;
	}
	
	private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
	
	public void render(GraphicsContext gc, KineticBody kb) {
		gc.save();
		this.rotate(gc, kb.rotation, kb.position.getX(), kb.position.getY());
		gc.setFill(Color.RED);
		gc.fillRect(kb.boundary.getX(), kb.boundary.getY(), kb.boundary.getWidth(), kb.boundary.getHeight());
		gc.setFill(Color.TRANSPARENT);
		gc.restore();
	}
}
