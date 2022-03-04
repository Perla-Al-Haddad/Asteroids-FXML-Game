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
	private KineticPlayer player;
	private ArrayList<KineticLaser> laserList;
	private ArrayList<KineticPolygon> asteroidsList;
	private GraphicsContext context;
	
	private KineticBodyFactory playerFactory = new KineticPlayerFactory();
	private KineticLaserFactory laserFactory = new KineticLaserFactory();
	private KineticPolygonFactory polygonFactory = new KineticPolygonFactory();
	
	public GamePanel(Canvas gameCanvas) {
		this.player = (KineticPlayer) playerFactory.getKineticBody();
		this.player.position.setCoords(GameSettings.getInstance().getGameWidth()/2, GameSettings.getInstance().getGameHeight()/2);
		
		this.laserList = new ArrayList<KineticLaser>();	
		this.asteroidsList = new ArrayList<KineticPolygon>();
		this.context = gameCanvas.getGraphicsContext2D();
		this.context.fillRect(0, 0, GameSettings.getInstance().getGameWidth(), GameSettings.getInstance().getGameHeight());

		gameCanvas.widthProperty().addListener(observable -> {
			context.fillRect(0, 0, GameSettings.getInstance().getGameWidth(), gameCanvas.getHeight());
			GameSettings.getInstance().setGameWidth(gameCanvas.getWidth());
		});
		gameCanvas.heightProperty().addListener(observable -> {
			context.fillRect(0, 0, GameSettings.getInstance().getGameWidth(), gameCanvas.getHeight());
			GameSettings.getInstance().setGameHeight(gameCanvas.getHeight());
		});
	}
	
	public void renderPlayerLives(HBox livesBox) {
		for (PlayerLifeIcon l: player.getLifeIcons())
			livesBox.getChildren().add(l.getIcon());
	}
	
	public void render() {
		this.context.fillRect(0, 0, GameSettings.getInstance().getGameWidth(), GameSettings.getInstance().getGameHeight());
		for (KineticLaser laser : this.laserList) 
			laser.render(this.context);
		for (KineticPolygon asteroid : this.asteroidsList) 
			asteroid.render(this.context);
		player.render(this.context);
	}
	
	public void handleLaserDeletion() {
		// Remove laser from array when it exits the screen
		for (int i = 0; i < this.laserList.size(); i++) {
			KineticBody laser = this.laserList.get(i);
			if (laser.position.getX() > GameSettings.getInstance().getGameWidth()) 
				this.laserList.remove(i);
			else if (laser.position.getX() < 0) 
				this.laserList.remove(i);
			if (laser.position.getY() > GameSettings.getInstance().getGameHeight()) 
				this.laserList.remove(i);
			else if (laser.position.getY() < 0) 
				this.laserList.remove(i);
		}	
	}
	public void handleAsteroidsDeletion() {
		// Remove asteroid from array when it exits the screen
		for (int i = 0; i < this.asteroidsList.size(); i++) {
			KineticPolygon asteroid = this.asteroidsList.get(i);
			if (asteroid.position.getX() > GameSettings.getInstance().getGameWidth() + asteroid.boundary.getWidth()) 
				this.asteroidsList.remove(i);
			else if (asteroid.position.getX() < -asteroid.boundary.getWidth()) 
				this.asteroidsList.remove(i);
			if (asteroid.position.getY() > GameSettings.getInstance().getGameHeight() + asteroid.boundary.getHeight()) 
				this.asteroidsList.remove(i);
			else if (asteroid.position.getY() < -asteroid.boundary.getHeight()) 
				this.asteroidsList.remove(i);
		}
	}
	
	public void spawnAsteroids() {
		// If there are less than the maximum amount of asteroids in the array, create a new asteroid
		if (this.asteroidsList.size() < GameSettings.MAX_ASTEROIDS_ON_SCREEN) {
			KineticPolygon asteroid = (KineticPolygon) polygonFactory.getKineticBody();
			this.asteroidsList.add(asteroid);
		}
	}
	public void spawnLaser() {
		if (this.laserList.size() < GameSettings.MAX_LASER_ON_SCREEN) { // only create a new laser if there are less than 4 lasers currently on screen (avoid spamming)
			KineticLaser laser = (KineticLaser) laserFactory.getKineticBody();
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
					if (asteroid.getType() == AsteroidType.PARENT) {
						int spawnNb = (int) (Math.random()*GameSettings.MAX_CHILD_ASTEROIDS_NUMBER);
						for (int i = 0; i <= spawnNb; i++) {
							if (spawnNb == 0)
								break;
							KineticPolygon childasteroid = (KineticPolygon) polygonFactory.getKineticBody(asteroid);
							this.asteroidsList.add(childasteroid);
						}
					}
					scoreLabel.setText(String.valueOf(this.player.getPlayerScore()));
				}
			}
		}	
	}
	
	public boolean detectPlayerAsteroidsCollision(VBox gameOverScreen, ScaleTransition scaleAnimation, Label scoreLabel) {
		for (int i = 0; i < this.asteroidsList.size(); i++) {
			KineticPolygon asteroid = this.asteroidsList.get(i);
			// On collision between the player and asteroid
			if (asteroid.overlaps(this.player)) {
				// If the player still has lives, reset the player coords to the center of the screen
				// And remove all asteroids from asteroid list
				if (this.player.getNbOfLives() > 0) {
					this.context.setFill(Color.WHITE);
					this.player.position.setCoords(GameSettings.getInstance().getGameWidth()/2, GameSettings.getInstance().getGameHeight()/2);
					this.player.setNbOfLives();
					this.player.getLifeIcons().get(this.player.getNbOfLives()).playDeathAnimation();
					this.asteroidsList.clear();
				} else { // if the player has no more live, add the GAME OVER menu, and stop the animation timer
					scaleAnimation.play();
					gameOverScreen.setStyle("visibility: visible;");
					scoreLabel.setText("Score:" + this.player.getPlayerScore());
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
		this.player.position.setCoords(GameSettings.getInstance().getGameWidth()/2, GameSettings.getInstance().getGameHeight()/2);
		this.player.resetLives();
		this.player.getVelocity().setLength(0);
		this.player.rotation = 0;
		this.player.resetLifeIcons();
		this.player.resetScore();
		scoreLabel.setText("000");
	}
	
	public void updatePlayerScore(KineticPolygon asteroid) {
		this.player.incrementPlayerScore(asteroid.getScoreWorth());
	}

	public KineticPlayer getPlayer() {
		return player;
	}
	
	
}
