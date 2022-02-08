package application;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;

public class PlayerBody extends KineticBody {
	
	public Vector force;
	public Sprite sprite;
	public ArrayList<PlayerLifeIcon> lifeIcons = new ArrayList<PlayerLifeIcon>();
	public int nbOfLives = 3;

	public PlayerBody() {
		super();
		this.force = new Vector();
		this.sprite = new Sprite();
	}
	
	public PlayerBody(String imageFileName) {
		this();
		this.sprite.setImage(imageFileName);
		this.boundary.setSize(this.sprite.getWidth()-25, this.sprite.getHeight()-25);
		for (int i = 0; i < nbOfLives; i++)
			this.lifeIcons.add(new PlayerLifeIcon("\\images\\lifeIcon.png"));
	}
	
	public void boost() {
		this.force.setAngle(this.rotation);
		this.force.setLength(0.5);
		this.force.multiply(0.5);
		this.boundary.setPosition(this.position.getX(), this.position.getY());
		this.getVelocity().add(force.getX()*100, force.getY()*100);
	}
	
	public void render(GraphicsContext context) {
		context.save();
		context.translate(this.position.getX(), this.position.getY());
		context.rotate(this.rotation);
		context.translate(-this.sprite.getWidth()/2, -this.sprite.getHeight()/2);
		context.drawImage(this.sprite.image, 0, 0);
		context.restore();
//		this.boundary.render(context, this);
	}
	
	public void resetLifeIcons() {
		for (PlayerLifeIcon i : this.lifeIcons)
			i.resetDeathAnimaton();
	}
	
}
