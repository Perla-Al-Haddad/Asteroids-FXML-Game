package application;

import java.io.File;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController implements Initializable {

	private ArrayList<String> keyPressedList = new ArrayList<String>();		
	private ArrayList<String> keyJustPressedList = new ArrayList<String>();

	@FXML public Canvas gameCanvas;


	@FXML private HBox livesBox;
	@FXML private Label playerScore;
	
	@FXML private Label gameOverLabel;
	@FXML private Button restartBtn;
	@FXML private VBox gameOverScreen;
	@FXML private Button highScoreBtn;
	@FXML private Button gameOverScreenQuitBtn;
	@FXML private Label scoreLabel;
	
	@FXML private Label startLabel;
	@FXML private VBox startScreen;
	@FXML private Button startBtn;
	@FXML private Button startScreenQuitBtn;
	
	@FXML private HBox muteButtonBox;
	@FXML private Button muteBtn;
	
//	@FXML private Label highScoreLabel;
//	@FXML private VBox highScoreScreen;
	
	@FXML void handleCloseButtonAction(ActionEvent event) {
		Stage stage = (Stage) gameCanvas.getScene().getWindow();
	    stage.close();
	}
	
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
		muteBtn.getStyleClass().add("icon-button");
		muteBtn.setPadding(new Insets(15));
		muteBtn.setPickOnBounds(true);
		muteBtn.setFocusTraversable(false);	
		
		String path = System.getProperty("user.dir") + "\\src\\resources\\music\\01_Stage_Select.mp3";  
		Media media = new Media(new File(path).toURI().toString());  
		MediaPlayer menuPlayer = new MediaPlayer(media);
		menuPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		menuPlayer.play();
		
		path = System.getProperty("user.dir") + "\\src\\resources\\music\\05_Bomb_Man.mp3";  
		media = new Media(new File(path).toURI().toString());  
		MediaPlayer gamePlayer = new MediaPlayer(media);  
		gamePlayer.setCycleCount(MediaPlayer.INDEFINITE);
	    
		path = System.getProperty("user.dir") + "\\src\\resources\\music\\10_Victory!.mp3";  
		media = new Media(new File(path).toURI().toString());  
		MediaPlayer gameoverPlayer = new MediaPlayer(media);  
		
	    Utils.getInstance().styleLabel(gameOverLabel, "ARCADE_N", Color.rgb(255, 0, 77), 60, 0.3); 
		Utils.getInstance().styleLabel(startLabel, "TRON", Color.rgb(41, 173, 255), 75, 0.3); 
		Utils.getInstance().styleLabel(playerScore, "ARCADE_R", Color.WHITE, 18, 0.3);
		Utils.getInstance().styleLabel(scoreLabel, "ARCADE_N", Color.WHITE, 27, 0.3);
		
		Utils.getInstance().styleButton(restartBtn, "ARCADE_R", Color.BLACK, 18, 15);
		Utils.getInstance().styleButton(startBtn, "ARCADE_R", Color.BLACK, 18, 15);
		Utils.getInstance().styleButton(highScoreBtn, "ARCADE_R", Color.BLACK, 10);
		Utils.getInstance().styleButton(startScreenQuitBtn, "ARCADE_R", Color.BLACK, 10);
		Utils.getInstance().styleButton(gameOverScreenQuitBtn, "ARCADE_R", Color.BLACK, 10);
				
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
					gamepanel.getPlayer().rotation -= GameSettings.PLAYER_ROTATIONAL_SPEED;
				if (keyPressedList.contains("RIGHT"))
					gamepanel.getPlayer().rotation += GameSettings.PLAYER_ROTATIONAL_SPEED;
				if (keyPressedList.contains("UP")) {
					gamepanel.getPlayer().boost();
				}
				gamepanel.getPlayer().getVelocity().multiply(GameSettings.PLAYER_FRICTION_EFFECT); // add friction to user movement
				gamepanel.getPlayer().wrap(gameCanvas); // check if the gamepanel.player has exited the canvas space and wrap if they did
				
				// Create new laser when player clicks space
				if (keyJustPressedList.contains("SPACE")) {
					gamepanel.spawnLaser();
				}
				keyJustPressedList.clear();
				
				gamepanel.update();

				gamepanel.handleLaserDeletion();
				gamepanel.handleAsteroidsDeletion();
				
				gamepanel.spawnAsteroids();
				
				gamepanel.detectLaserAsteroidsCollision(playerScore);
				
				if (gamepanel.detectPlayerAsteroidsCollision(gameOverScreen, scaleAnimation, scoreLabel)) {
					this.stop();
					gamePlayer.stop();	
					gameoverPlayer.setOnEndOfMedia(gameoverPlayer::stop);
					gameoverPlayer.play();
				}
				gamepanel.render();
			}
		};
		
		restartBtn.setOnMouseClicked(e -> {
			gameOverScreen.setStyle("visibility: hidden");
			gameloop.start();
			gameoverPlayer.stop();
			gamePlayer.setOnEndOfMedia(gamePlayer::stop);	
			gamePlayer.play();
			gamepanel.restartGame(playerScore);
		});
		
		startBtn.setOnMouseClicked(e -> {
			startScreen.setStyle("visibility: hidden;");
			menuPlayer.pause();
			gamePlayer.play();
			gameloop.start();
		});
		
		muteBtn.setOnMouseClicked(e -> {
			gamepanel.toggleMuteGame();
			gamePlayer.setMute(!gamePlayer.isMute());
			gameoverPlayer.setMute(!gameoverPlayer.isMute());
			menuPlayer.setMute(!menuPlayer.isMute());
			if (GameSettings.getInstance().isMuted()) {
				muteBtn.getStyleClass().remove("icon-button");
				muteBtn.getStyleClass().add("icon-button-off");
				muteButtonBox.setPadding(new Insets(0, 15, 0, 0	));
			} else {
				muteBtn.getStyleClass().remove("icon-button-off");
				muteBtn.getStyleClass().add("icon-button");
				muteButtonBox.setPadding(new Insets(0, 0, 0, 0));
			}
		});
		
	}
	
}
