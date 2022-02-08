package application;

import java.util.ArrayList;

import javafx.animation.ScaleTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class GamePanel {
	public PlayerBody player;
	public ArrayList<LaserBody> laserList;
	public ArrayList<KineticPolygon> asteroidsList;
	public GraphicsContext context;
	public Canvas gameCanvas = new Canvas();
	private int asteroidsCount = 12;
	
	public GamePanel(Canvas gameCanvas) {
		this.gameCanvas = gameCanvas;

		this.player = new PlayerBody("\\images\\player.png");
		this.player.position.setCoords(this.gameCanvas.getWidth()/2, this.gameCanvas.getHeight()/2);
		
		this.laserList = new ArrayList<LaserBody>();	
		this.asteroidsList = new ArrayList<KineticPolygon>();
		this.context = this.gameCanvas.getGraphicsContext2D();
		this.context.fillRect(0, 0, this.gameCanvas.getWidth(), this.gameCanvas.getHeight());

		this.gameCanvas.widthProperty().addListener(observable -> 
			context.fillRect(0, 0, this.gameCanvas.getWidth(), gameCanvas.getHeight())
		);
		this.gameCanvas.heightProperty().addListener(observable -> 
			context.fillRect(0, 0, this.gameCanvas.getWidth(), gameCanvas.getHeight())
		);
	}
	
	public void renderPlayerLives(HBox livesBox) {
		for (PlayerLifeIcon l: player.lifeIcons)
			livesBox.getChildren().add(l.iconImageView);
	}
	
	public void render() {
		this.context.fillRect(0, 0, this.gameCanvas.getWidth(), this.gameCanvas.getHeight());
		for (LaserBody laser : this.laserList) 
			laser.render(this.context);
		for (KineticPolygon asteroid : this.asteroidsList) 
			asteroid.render(this.context);
		player.render(this.context);
	}
	
	public void handleLaserDeletion() {
		// Remove laser from array when it exits the screen
		for (int i = 0; i < this.laserList.size(); i++) {
			KineticBody laser = this.laserList.get(i);
			if (laser.position.getX() > gameCanvas.getWidth()) 
				this.laserList.remove(i);
			else if (laser.position.getX() < 0) 
				this.laserList.remove(i);
			if (laser.position.getY() > gameCanvas.getHeight()) 
				this.laserList.remove(i);
			else if (laser.position.getY() < 0) 
				this.laserList.remove(i);
		}	
	}
	public void handleAsteroidsDeletion() {
		// Remove asteroid from array when it exits the screen
		for (int i = 0; i < this.asteroidsList.size(); i++) {
			KineticPolygon asteroid = this.asteroidsList.get(i);
			if (asteroid.position.getX() > gameCanvas.getWidth() + asteroid.boundary.getWidth()) 
				this.asteroidsList.remove(i);
			else if (asteroid.position.getX() < -asteroid.boundary.getWidth()) 
				this.asteroidsList.remove(i);
			if (asteroid.position.getY() > gameCanvas.getHeight() + asteroid.boundary.getHeight()) 
				this.asteroidsList.remove(i);
			else if (asteroid.position.getY() < -asteroid.boundary.getHeight()) 
				this.asteroidsList.remove(i);
		}
	}
	
	public void spawnAsteroids() {
		// If there are less than the maximum amount of asteroids in the array, create a new asteroid
		if (this.asteroidsList.size() < this.asteroidsCount) {
			KineticPolygon asteroid = new KineticPolygon(gameCanvas.getWidth(), gameCanvas.getHeight());
			this.asteroidsList.add(asteroid);
		}
	}
	public void spawnLaser() {
		if (this.laserList.size() < 4) { // only create a new laser if there are less than 4 lasers currently on screen (avoid spamming)
			LaserBody laser = new LaserBody("\\images\\laser.png");
			laser.position.setCoords(this.player.position.getX(), this.player.position.getY());
			laser.getVelocity().setLength(800);
			laser.getVelocity().setAngle(this.player.rotation);
			this.laserList.add(laser);
		}
	}
	
	public void detectLaserAsteroidsCollision(Label scoreLabel) {
		// Detect collision between lasers and asteroids
		for (int laserNum = 0; laserNum < this.laserList.size(); laserNum++) {
			KineticBody laser = this.laserList.get(laserNum);
			for (int asteroidNum = 0; asteroidNum < this.asteroidsList.size(); asteroidNum++) {
				KineticPolygon asteroid = this.asteroidsList.get(asteroidNum);
				// On collision remove laser and asteroid from the lists
				if (asteroid.overlaps(laser)) {
					if (this.laserList.size() != 0) 
						this.laserList.remove(laserNum);
					this.asteroidsList.remove(asteroidNum);
					this.updatePlayerScore(asteroid);
					scoreLabel.setText(String.valueOf(this.player.playerScore));
				}
			}
		}	
	}
	
	public boolean detectPlayerAsteroidsCollision(VBox gameOverScreen, ScaleTransition scaleAnimation) {
		for (int i = 0; i < this.asteroidsList.size(); i++) {
			KineticPolygon asteroid = this.asteroidsList.get(i);
			// On collision between the player and asteroid
			if (asteroid.overlaps(this.player)) {
				// If the player still has lives, reset the player coords to the center of the screen
				// And remove all asteroids from asteroid list
				if (this.player.nbOfLives > 0) {
					this.context.setFill(Color.WHITE);
					this.player.position.setCoords(gameCanvas.getWidth()/2, gameCanvas.getHeight()/2);
					this.player.lifeIcons.get(--this.player.nbOfLives).playDeathAnimation();
					this.asteroidsList.clear();
				} else { // if the player has no more live, add the GAME OVER menu, and stop the animation timer
					scaleAnimation.play();
					gameOverScreen.setStyle("visibility: visible;");
					return true;
				}
			}
		}	
		return false;
	}
	
	public void update() {
		this.player.update(1/60.0); 
		for (int i = 0; i < laserList.size(); i++) {
			KineticBody laser = laserList.get(i);
			laser.update(1/60.0);
		}
		for (int i = 0; i < asteroidsList.size(); i++) {
			KineticPolygon asteroid = asteroidsList.get(i);
			asteroid.update(1/60.0);
		}
	}
	
	public void restartGame(Label scoreLabel) {
		this.laserList.clear();
		this.asteroidsList.clear();
		this.player.position.setCoords(gameCanvas.getWidth()/2, gameCanvas.getHeight()/2);
		this.player.resetLives();
		this.player.getVelocity().setLength(0);
		this.player.rotation = 0;
		this.player.resetLifeIcons();
		this.player.resetScore();
		scoreLabel.setText("000");
	}
	
	public void updatePlayerScore(KineticPolygon asteroid) {
		this.player.playerScore += asteroid.scoreWorth;
	}
}
