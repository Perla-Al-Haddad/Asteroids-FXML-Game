package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GameController implements Initializable {

	private ArrayList<String> keyPressedList = new ArrayList<String>();		
	private ArrayList<String> keyJustPressedList = new ArrayList<String>();

	@FXML public Canvas gameCanvas;

	@FXML private HBox livesBox;

	@FXML private Label gameOverLabel;
	@FXML private Button restartBtn;
	@FXML private VBox gameOverScreen;
	@FXML private Button highScoreBtn;
	@FXML private Button gameOverScreenQuitBtn;
	
	@FXML private Label startLabel;
	@FXML private VBox startScreen;
	@FXML private Button startBtn;
	@FXML private Button startScreenQuitBtn;

	public void keyPressed(KeyEvent event) {
		String keyName = event.getCode().toString();
		if (!keyPressedList.contains(keyName))
			keyPressedList.add(keyName);
		if (!keyJustPressedList.contains(keyName))
			keyJustPressedList.add(keyName);
	}
	public void keyReleased(KeyEvent event) {
		String keyName = event.getCode().toString();
		if (keyPressedList.contains(keyName))
			keyPressedList.remove(keyName);
	}
	
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		Font gameOverfont = null;
		Font startfont = null;
		Font primaryBtnfont = null;
		Font secondaryBtnfont = null;
		try {
			startfont = Font.loadFont(new FileInputStream(new File("src\\resources\\fonts\\TRON.TTF")), 75);
			gameOverfont = Font.loadFont(new FileInputStream(new File("src\\resources\\fonts\\ARCADE_N.TTF")), 60);
			primaryBtnfont = Font.loadFont(new FileInputStream(new File("src\\resources\\fonts\\ARCADE_R.TTF")), 18);
			secondaryBtnfont = Font.loadFont(new FileInputStream(new File("src\\resources\\fonts\\ARCADE_R.TTF")), 10);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		gameOverLabel.setFont(gameOverfont);
		gameOverLabel.setTextFill(Color.WHITE);

		restartBtn.setFont(primaryBtnfont);
		restartBtn.setPadding(new Insets(15));
		restartBtn.setFocusTraversable(false);

		startLabel.setFont(startfont);
		startLabel.setTextFill(Color.WHITE);
		
		startBtn.setFont(primaryBtnfont);
		startBtn.setPadding(new Insets(15));
		startBtn.setFocusTraversable(false);

		highScoreBtn.setFont(secondaryBtnfont);
		highScoreBtn.setFocusTraversable(false);
		startScreenQuitBtn.setFont(secondaryBtnfont);
		startScreenQuitBtn.setFocusTraversable(false);
		gameOverScreenQuitBtn.setFont(secondaryBtnfont);
		gameOverScreenQuitBtn.setFocusTraversable(false);
		
		GraphicsContext context = gameCanvas.getGraphicsContext2D();
		context.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
		// refill canvas background color to all black on screen resize and update WINDOW dimensions 
		gameCanvas.widthProperty().addListener(observable -> context.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight()));
		gameCanvas.heightProperty().addListener(observable -> context.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight()));

		PlayerBody player = new PlayerBody("\\images\\player.png");
		player.position.setCoords(gameCanvas.getWidth()/2, gameCanvas.getHeight()/2);

		for (PlayerLifeIcon l: player.lifeIcons)
			livesBox.getChildren().add(l.iconImageView);
		
		ArrayList<LaserBody> laserList = new ArrayList<LaserBody>();
		ArrayList<KineticPolygon> asteroidsList = new ArrayList<KineticPolygon>();
		int asteroidCount = 12; // Set max number of asteroids
		
		AnimationTimer gameloop = new AnimationTimer() {
			public void handle(long nanotime) {
				// process user input
				if (keyPressedList.contains("LEFT"))
					player.rotation -= 4;
				if (keyPressedList.contains("RIGHT"))
					player.rotation += 4;
				if (keyPressedList.contains("UP")) {
					player.boost();
				}
				
				player.getVelocity().multiply(0.98); // add friction to user movement
				player.wrap(gameCanvas); // check if the player has exited the canvas space and wrap if they did
				player.update(1/60.0); 
				
				// Create new laser when player clicks space
				if (keyJustPressedList.contains("SPACE")) 
					if (laserList.size() < 4) { // only create a new laser if there are less than 4 lasers currently on screen (avoid spamming)
						LaserBody laser = new LaserBody("\\images\\laser.png");
						laser.position.setCoords(player.position.getX(), player.position.getY());
						laser.getVelocity().setLength(800);
						laser.getVelocity().setAngle(player.rotation);
						laserList.add(laser);
						keyJustPressedList.clear();
					}
				keyJustPressedList.clear();
				
				// Remove laser from array when it exits the screen
				for (int i = 0; i < laserList.size(); i++) {
					KineticBody laser = laserList.get(i);
					laser.update(1/60.0);
					if (laser.position.getX() > gameCanvas.getWidth()) 
						laserList.remove(i);
					else if (laser.position.getX() < 0) 
						laserList.remove(i);
					if (laser.position.getY() > gameCanvas.getHeight()) 
						laserList.remove(i);
					else if (laser.position.getY() < 0) 
						laserList.remove(i);
				}
				
				// Remove asteroid from array when it exits the screen
				for (int i = 0; i < asteroidsList.size(); i++) {
					KineticPolygon asteroid = asteroidsList.get(i);
					if (asteroid.position.getX() > gameCanvas.getWidth() + asteroid.boundary.getWidth()) 
						asteroidsList.remove(i);
					else if (asteroid.position.getX() < -asteroid.boundary.getWidth()) 
						asteroidsList.remove(i);
					if (asteroid.position.getY() > gameCanvas.getHeight() + asteroid.boundary.getHeight()) 
						asteroidsList.remove(i);
					else if (asteroid.position.getY() < -asteroid.boundary.getHeight()) 
						asteroidsList.remove(i);
				}
				// If there are less than the maximum amount of asteroids in the array, create a new asteroid
				if (asteroidsList.size() < asteroidCount) {
					KineticPolygon asteroid = new KineticPolygon(gameCanvas.getWidth(), gameCanvas.getHeight());
					asteroidsList.add(asteroid);
				}
				// Detect collision between lasers and asteroids
				for (int laserNum = 0; laserNum < laserList.size(); laserNum++) {
					KineticBody laser = laserList.get(laserNum);
					for (int asteroidNum = 0; asteroidNum < asteroidsList.size(); asteroidNum++) {
						KineticPolygon asteroid = asteroidsList.get(asteroidNum);
						// On collision remove laser and asteroid from the lists
						if (asteroid.overlaps(laser)) {
							if (laserList.size() != 0) 
								laserList.remove(laserNum);
							asteroidsList.remove(asteroidNum);
						}
					}
				}
				
				// Detect collision between player and asteroids
				for (int i = 0; i < asteroidsList.size(); i++) {
					KineticPolygon asteroid = asteroidsList.get(i);
					// On collision between the player and asteroid
					if (asteroid.overlaps(player)) {
						// If the player still has lives, reset the player coords to the center of the screen
						// And remove all asteroids from asteroid list
						if (player.nbOfLives > 0) {
							context.setFill(Color.WHITE);
							player.position.setCoords(gameCanvas.getWidth()/2, gameCanvas.getHeight()/2);
							player.lifeIcons.get(--player.nbOfLives).playDeathAnimation();
							asteroidsList.clear();
						} else { // if the player has no more live, add the GAME OVER menu, and stop the animation timer
							ScaleTransition scaleAnimation = new ScaleTransition(Duration.millis(1500), gameOverLabel);
							scaleAnimation.setByX(0.1);
							scaleAnimation.setByY(0.1);
							scaleAnimation.setAutoReverse(true); 
							scaleAnimation.setCycleCount(Animation.INDEFINITE);
							scaleAnimation.setInterpolator(Interpolator.EASE_BOTH);
							scaleAnimation.play();
							gameOverScreen.setStyle("visibility: visible;");

							this.stop();
						}
					}
				}
				
				for (int i = 0; i < asteroidsList.size(); i++) {
					KineticPolygon asteroid = asteroidsList.get(i);
					asteroid.update(1/60.0);
				}

				context.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
				for (LaserBody laser : laserList) 
					laser.render(context);
				for (KineticPolygon asteroid : asteroidsList) 
					asteroid.render(context);
				player.render(context);
			}
		};
//		restartBtn.setOnMouseClicked(e -> {
//			gameloop.start();
//		});
		startBtn.setOnMouseClicked(e -> {
			gameloop.start();
			startScreen.setStyle("visibility: hidden;");
		});
	}
	
}
