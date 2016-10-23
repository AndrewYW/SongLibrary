/**
 * Song library
 * Andrew Wang
 * Rumzi Tadros
 */
package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.SongController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/Lib.fxml"));
			AnchorPane rootLayout = (AnchorPane) loader.load();
			
			SongController controller = loader.getController();
			controller.start(primaryStage);
			
			Scene scene = new Scene(rootLayout, 600, 400);
			primaryStage.setTitle("Song library - Rumzi Tadros & Andrew Wang");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			stop();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
