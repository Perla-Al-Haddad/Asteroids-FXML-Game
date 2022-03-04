package application;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class KineticPlayer extends KineticBody {
	private Vector force;
	private ArrayList<PlayerLifeIcon> lifeIcons = new ArrayList<PlayerLifeIcon>();
	private int nbOfLives = GameSettings.MAX_NB_OF_LIVES;
	private int playerScore = 0;
	private Image sprite; 

	public KineticPlayer(String imageFileName) {
		super();
		this.force = new Vector();
		this.sprite = new Image(imageFileName);
		this.boundary.setSize(this.sprite.getWidth()-GameSettings.PLAYER_BOUNDARY_REDUCTION, this.sprite.getHeight()-GameSettings.PLAYER_BOUNDARY_REDUCTION);
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
		context.drawImage(this.sprite, 0, 0);
		context.restore();
//		this.boundary.render(context, this);
	}
	
	public void resetLifeIcons() {
		for (PlayerLifeIcon i : this.lifeIcons)
			i.resetDeathAnimaton();
	}
	public void resetLives() { 
		this.nbOfLives = GameSettings.MAX_NB_OF_LIVES;
	}
	public void resetScore() {
		this.playerScore = 0;
	}

	public ArrayList<PlayerLifeIcon> getLifeIcons() {
		return lifeIcons;
	}
	
	public int getNbOfLives() {
		return nbOfLives;
	}
	public void setNbOfLives() {
		this.nbOfLives--;
	}

	public int getPlayerScore() {
		return playerScore;
	}
	public void incrementPlayerScore(int score) {
		this.playerScore += score;
	}
	
}
