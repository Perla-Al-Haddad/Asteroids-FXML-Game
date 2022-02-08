package application;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class KineticPolygon extends KineticBody {
	private double spin;
	public double[] xCoords;
	public double[] yCoords;
	public int verticesNb;
	
	public KineticPolygon(double WINDOW_WIDTH, double WINDOW_HEIGHT) {
		super();
		this.verticesNb = (int) (Math.floor(7*Math.random()) + 5);
		this.xCoords = new double[this.verticesNb];
		this.yCoords = new double[this.verticesNb];
		this.velocity.setLength(2*Math.random() + 1); // generate random starting velocity
		double angleRdm = (Math.random()*10 > 5) ? Math.random()*90+135 : 270+Math.random()*90;
		this.velocity.setAngle(angleRdm); // generate random starting rotation angle
		this.spin = Math.random(); // generate random spin
		if (angleRdm < 270) {
			this.generateCoords(
					(100 * Math.random() + WINDOW_WIDTH+50), 
					((WINDOW_HEIGHT) * Math.random()), 
					this.verticesNb, 30, 70); // generate random polygon vertices 
		}
		else if (angleRdm > 90)
			this.generateCoords(
					(-100 * Math.random() - 50), 
					((WINDOW_HEIGHT) * Math.random()), 
					this.verticesNb, 30, 70); // generate random polygon vertices 
		double center[] = this.calculateCentroid();
		this.position.setCoords(center[0], center[1]); // calculate the center of the polygon
	}
	
	public double arrayMax(double[] arr) {
	    double max = Double.NEGATIVE_INFINITY;
	    for(double cur: arr)
	        max = Math.max(max, cur);
	    return max;
	}	
	public double arrayMin(double[] arr) {
	    double min = Double.POSITIVE_INFINITY;
	    for(double cur: arr)
	        min = Math.min(min, cur);
	    return min;
	}
	public double[] getRect()
    {
		double xMax = arrayMax(this.xCoords);
		double yMax = arrayMax(this.yCoords);
		double xMin = arrayMin(this.xCoords);
		double yMin = arrayMin(this.yCoords);
		double width = xMax - xMin;
		double height = yMax - yMin;
		return new double[] {xMin, yMin, width, height};
    }
	
	public void update(double deltaTime) {
		for (int i = 0; i < this.xCoords.length; i++) {
			this.xCoords[i] += this.velocity.getX();
			this.yCoords[i] += this.velocity.getY();
		}
		double center[] = this.calculateCentroid();
		this.position.setCoords(center[0], center[1]);
		this.rotation += this.spin;
		this.setBoundary();
	}
	
	@Override
	public void setBoundary() {
		double rect[] = this.getRect();
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
	
	public double[] calculateCentroid() {
	    double x = 0.;
	    double y = 0.;
	    for (int i = 0; i < this.verticesNb; i++){
	        x += this.xCoords[i];
	        y += this.yCoords[i];
	    }
	    x = x/this.verticesNb;
	    y = y/this.verticesNb;
	    return new double[] {x, y};
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
	
}
