package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class KineticLaser extends KineticBody {
	public Image sprite;

	public KineticLaser(String imageFileName) {
		this.sprite = new Image(imageFileName);
		this.boundary.setSize(this.sprite.getWidth(), this.sprite.getHeight());
	}
	
	public void render(GraphicsContext context) {
		context.save();
//		this.boundary.render(context, this);
		context.translate(this.position.getX(), this.position.getY());
		context.rotate(this.rotation);
		context.translate(-this.sprite.getWidth()/2, -this.sprite.getHeight()/2);
		context.drawImage(this.sprite, 0, 0);
		context.restore();
	}
	
}
