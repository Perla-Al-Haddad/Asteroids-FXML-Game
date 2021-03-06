package application;
	

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("GameMain.fxml"));
			Pane root = (Pane) loader.load();
			root.requestFocus();
			
			Scene scene = new Scene(root, GameSettings.getInstance().getGameWidth(), GameSettings.getInstance().getGameHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			GameController controller = loader.getController();
			scene.setOnKeyPressed((KeyEvent event) -> { controller.keyPressed(event); });
			scene.setOnKeyReleased((KeyEvent event) -> { controller.keyReleased(event); });
			
			// bind pane width and height to canvas width and height
			controller.gameCanvas.widthProperty().bind(root.widthProperty());
			controller.gameCanvas.heightProperty().bind(root.heightProperty());					
		
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
