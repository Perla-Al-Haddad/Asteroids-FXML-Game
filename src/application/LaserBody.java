package application;

import javafx.scene.canvas.GraphicsContext;

public class LaserBody extends KineticBody {
	public Sprite sprite;

	public LaserBody(String imageFileName) {
		this.sprite = new Sprite();
		this.sprite.setImage(imageFileName);
		this.boundary.setSize(this.sprite.getWidth(), this.sprite.getHeight());
	}
	
	
	public void render(GraphicsContext context) {
		context.save();
//		this.boundary.render(context, this);
		context.translate(this.position.getX(), this.position.getY());
		context.rotate(this.rotation);
		context.translate(-this.sprite.getWidth()/2, -this.sprite.getHeight()/2);
		context.drawImage(this.sprite.image, 0, 0);
		context.restore();
	}
	
}
