package application;
	
import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
	        Parent parent = FXMLLoader.load(getClass().getResource("myFXML.fxml"));
	        
			Scene scene = new Scene(parent);
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        System.out.println("W:"+screenSize.getWidth()+" H:"+screenSize.getHeight());
			
			scene.getRoot().requestFocus();
			primaryStage.setTitle("ReMouse");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
