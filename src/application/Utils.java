package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Utils {

	private static Utils INSTANCE = null;
	
	private Utils() {};
	
	public static Utils getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Utils();
		}
		return INSTANCE;
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
	
	public void styleLabel(Label label, String fontFamily, Color color, int fontSize, double alpha) {
		Font font = null;
		try {
			font = Font.loadFont(new FileInputStream(new File("src\\resources\\fonts\\" + fontFamily + ".TTF")), fontSize);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		label.setFont(font);
		label.setTextFill(color);
		label.setStyle("-fx-effect: dropshadow(gaussian, rgba(" +
				color.getRed()*255 +"," +
				color.getGreen()*255 + "," +
				color.getBlue()*255 + ", "+
				alpha + "), 10, 0.6, 0.0, 0.0);");
	}
	public void styleButton(Button btn, String fontFamily, Color textColor, int fontSize) {
		Font font = null;
		try {
			font = Font.loadFont(new FileInputStream(new File("src\\resources\\fonts\\" + fontFamily + ".TTF")), fontSize);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		btn.setFont(font);
		btn.setFocusTraversable(false);
	}
	public void styleButton(Button btn, String fontFamily, Color textColor, int fontSize, int insets) {
		this.styleButton(btn, fontFamily, textColor, fontSize);
		btn.setPadding(new Insets(insets));
	}
	
	public double[] calculateCentroid(double[] xCoords, double[] yCoords, int nbOfVertices) {
	    double x = 0.;
	    double y = 0.;
	    for (int i = 0; i < nbOfVertices; i++){
	        x += xCoords[i];
	        y += yCoords[i];
	    }
	    x = x/nbOfVertices;
	    y = y/nbOfVertices;
	    return new double[] {x, y};
	}
	
	public double[] getRect(double[] xCoords, double[] yCoords) {
		double xMax = Utils.getInstance().arrayMax(xCoords);
		double yMax = Utils.getInstance().arrayMax(yCoords);
		double xMin = Utils.getInstance().arrayMin(xCoords);
		double yMin = Utils.getInstance().arrayMin(yCoords);
		double width = xMax - xMin;
		double height = yMax - yMin;
		return new double[] {xMin, yMin, width, height};
    }
	
}
