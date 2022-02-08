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

		startLabel.setFont(startfont);
		startLabel.setTextFill(Color.WHITE);

		restartBtn.setFont(primaryBtnfont);
		restartBtn.setPadding(new Insets(15));
		restartBtn.setFocusTraversable(false);
		
		startBtn.setFont(primaryBtnfont);
		startBtn.setPadding(new Insets(15));
		startBtn.setFocusTraversable(false);

		highScoreBtn.setFont(secondaryBtnfont);
		highScoreBtn.setFocusTraversable(false);
		startScreenQuitBtn.setFont(secondaryBtnfont);
		startScreenQuitBtn.setFocusTraversable(false);
		gameOverScreenQuitBtn.setFont(secondaryBtnfont);
		gameOverScreenQuitBtn.setFocusTraversable(false);
		
		ScaleTransition scaleAnimation = new ScaleTransition(Duration.millis(1500), gameOverLabel);
		scaleAnimation.setByX(0.1);
		scaleAnimation.setByY(0.1);
		scaleAnimation.setAutoReverse(true); 
		scaleAnimation.setCycleCount(Animation.INDEFINITE);
		scaleAnimation.setInterpolator(Interpolator.EASE_BOTH);
		
		GamePanel gamepanel = new GamePanel(gameCanvas);		
		gamepanel.renderPlayerLives(livesBox);
		
		AnimationTimer gameloop = new AnimationTimer() {
			public void handle(long nanotime) {
				// process user input
				if (keyPressedList.contains("LEFT"))
					gamepanel.player.rotation -= 4;
				if (keyPressedList.contains("RIGHT"))
					gamepanel.player.rotation += 4;
				if (keyPressedList.contains("UP")) {
					gamepanel.player.boost();
				}				
				gamepanel.player.getVelocity().multiply(0.98); // add friction to user movement
				gamepanel.player.wrap(gameCanvas); // check if the gamepanel.player has exited the canvas space and wrap if they did
				
				// Create new laser when player clicks space
				if (keyJustPressedList.contains("SPACE")) 
					gamepanel.spawnLaser();
				keyJustPressedList.clear();
				
				gamepanel.update();

				gamepanel.handleLaserDeletion();
				gamepanel.handleAsteroidsDeletion();
				
				gamepanel.spawnAsteroids();
				
				gamepanel.detectLaserAsteroidsCollision();
				
				if (gamepanel.detectPlayerAsteroidsCollision(gameOverScreen, scaleAnimation))
					this.stop();
				gamepanel.render();
			}
		};
		
		restartBtn.setOnMouseClicked(e -> {
			gamepanel.restartGame();
			gameOverScreen.setStyle("visibility: hidden");
			gameloop.start();
		});
		
		startBtn.setOnMouseClicked(e -> {
			gameloop.start();
			startScreen.setStyle("visibility: hidden;");
		});
	}
	
}
