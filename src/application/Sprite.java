package application;

import javafx.scene.image.Image;

public class Sprite {

	public Image image;
	
	public Sprite() {}
	
	public Sprite(String imageFileName) {
		this();
		this.setImage(imageFileName);
	}
	
	public void setImage(String imageFileName) {
		this.image = new Image(imageFileName);
	}
	
	public double getWidth() {
		return this.image.getWidth();
	}
	public double getHeight() {
		return this.image.getHeight();
	}
	
}
