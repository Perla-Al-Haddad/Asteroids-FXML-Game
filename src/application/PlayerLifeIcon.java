package application;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class PlayerLifeIcon {
	   
	public Sprite icon;
	public ImageView iconImageView;
	public RotateTransition rotateAnimation;
	public ScaleTransition scaleAnimation;
	
	public PlayerLifeIcon(String imageFileName) {
		this.icon = new Sprite(imageFileName);
		this.iconImageView = new ImageView(icon.image);
		this.iconImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.5), 10, 0.6, 0.0, 0.0);");
		this.movePivot(this.iconImageView, -10, 0);
		this.iconImageView.setRotate(this.iconImageView.getRotate() + 45);
		
		this.rotateAnimation = new RotateTransition(Duration.millis(700), this.iconImageView);
		this.rotateAnimation.setByAngle(360);
		this.rotateAnimation.setCycleCount(Animation.INDEFINITE);
		
		this.scaleAnimation = new ScaleTransition(Duration.millis(700), this.iconImageView);
		this.scaleAnimation.setToX(0);
		this.scaleAnimation.setToY(0);
		this.scaleAnimation.setAutoReverse(false);
	}
	
	private void movePivot(Node node, double x, double y){
        node.getTransforms().add(new Translate(-x,-y));
        node.setTranslateX(x); node.setTranslateY(y);
    }
	
	public void playDeathAnimation() {
		this.rotateAnimation.play();
		this.scaleAnimation.play();
	}
	
	public void resetDeathAnimaton() {
		this.rotateAnimation.jumpTo(Duration.ZERO);
	    this.rotateAnimation.stop();
		this.scaleAnimation.setToY(1);
		this.scaleAnimation.setToX(1);
		this.scaleAnimation.play();
		this.scaleAnimation.setToY(0);
		this.scaleAnimation.setToX(0);
	}
}
