package application;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class KineticPolygon extends KineticBody {
	private double spin;
	private double[] xCoords;
	private double[] yCoords;
	private int verticesNb;
	private int scoreWorth;

	private AsteroidType type;
	
	public KineticPolygon() {
		super();
		this.construct();
		this.generateCoordsWraper(GameSettings.PARENT_ASTEROID_RANGE[0], GameSettings.PARENT_ASTEROID_RANGE[1]);
		this.scoreWorth = (int) (Math.random() * GameSettings.PARENT_ASTEROID_SCORE_RANGE[0] + GameSettings.PARENT_ASTEROID_SCORE_RANGE[1]);
		this.position.setCoords(this.getCenterX(), this.getCenterY()); // calculate the center of the polygon
		this.type = AsteroidType.PARENT;
	}

	public KineticPolygon(KineticPolygon parent) {
		super();
		this.construct();
		this.generateCoords(parent.getCenterX(), parent.getCenterY(), this.verticesNb, GameSettings.CHILD_ASTEROID_RANGE[0], GameSettings.CHILD_ASTEROID_RANGE[1]);
		this.scoreWorth = (int) (Math.random() * GameSettings.CHILD_ASTEROID_SCORE_RANGE[0] + GameSettings.CHILD_ASTEROID_SCORE_RANGE[1]);
		this.position.setCoords(this.getCenterX(), this.getCenterY()); // calculate the center of the polygon		
		this.getVelocity().setAngle(Math.random()*360);
		this.type = AsteroidType.CHILD;
	}

	public void construct() {
		this.verticesNb = (int) (Math.floor(7 * Math.random()) + 5);
		this.xCoords = new double[this.verticesNb];
		this.yCoords = new double[this.verticesNb];
		this.getVelocity().setLength(2 * Math.random() + 1); // generate random starting velocity
		this.spin = Math.random(); // generate random spin
	}
	
	private void generateCoordsWraper(int minRadius, int maxRadius) {
		double angleRdm = (Math.random()*10 > 5) ? Math.random()*90+135 : 270+Math.random()*90;
		if (angleRdm < 270) {
			this.generateCoords(
					(100 * Math.random() + GameSettings.getInstance().getGameWidth()+50), 
					((GameSettings.getInstance().getGameHeight()) * Math.random()), 
					this.verticesNb, minRadius, maxRadius); // generate random polygon vertices 
		}
		else if (angleRdm > 90)
			this.generateCoords(
					(-100 * Math.random() - 50), 
					((GameSettings.getInstance().getGameHeight()) * Math.random()), 
					this.verticesNb, minRadius, maxRadius); // generate random polygon vertices 	
		this.getVelocity().setAngle(angleRdm); // generate random starting rotation angle
	}
	
	public void update(double deltaTime) {
		for (int i = 0; i < this.xCoords.length; i++) {
			this.xCoords[i] += this.getVelocity().getX();
			this.yCoords[i] += this.getVelocity().getY();
		}
		this.position.setCoords(this.getCenterX(), this.getCenterY());
		this.rotation += this.spin;
		this.setBoundary();
	}
	
	@Override
	public void setBoundary() {
		double rect[] = Utils.getInstance().getRect(xCoords, yCoords);
		this.boundary.setPosition(rect[0]+rect[2]/5/2, rect[1]+rect[3]/5/2);
		this.boundary.setSize(rect[2]-rect[2]/5, rect[3]-rect[3]/5);
	}
	
	public void generateCoords(double px, double py, int numberOfNodes, double minRadius, double maxRadius) {
	    // Split a full circle into numberOfNodes step, this is how much to advance each part
	    double angleStep = Math.PI * 2 / numberOfNodes;
	    Random rnd = new Random();
	    for(int i = 0; i < numberOfNodes; ++i) {
	        double targetAngle = angleStep * i; // This is the angle we want if all parts are equally spaced
	        double angle = targetAngle + (rnd.nextDouble() - 0.5) * angleStep * 0.25; // add a random factor to the angle, which is +- 25% of the angle step
	        double radius = minRadius + rnd.nextDouble() * (maxRadius - minRadius); // make the radius random but within minRadius to maxRadius
	        // calculate x and y positions of the part point
	        double x = px + Math.cos(angle) * radius; 
	        double y = py + Math.sin(angle) * radius;
			this.xCoords[i] = x;
			this.yCoords[i] = y;
	    }
	}
	
	private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
	
	public double getCenterX() {
		return Utils.getInstance().calculateCentroid(xCoords, yCoords, verticesNb)[0];
	}
	public double getCenterY() {
		return Utils.getInstance().calculateCentroid(xCoords, yCoords, verticesNb)[1];
	}
	
	public void render(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
	    gc.save();
	    this.rotate(gc, this.rotation, this.position.getX(), this.position.getY());
		gc.fillPolygon(this.xCoords, this.yCoords, this.verticesNb);
	    gc.setStroke(Color.WHITE);
	    gc.setLineWidth(2);
	    gc.strokePolygon(this.xCoords, this.yCoords, this.verticesNb);
	    gc.restore();
//		this.boundary.render(gc, this);
	}

	public int getScoreWorth() {
		return scoreWorth;
	}
	public AsteroidType getType() {
		return type;
	}
	
}
