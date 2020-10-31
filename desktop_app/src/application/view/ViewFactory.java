package application.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class ViewFactory {
	public Scene getMainScene() throws Exception
	{
		Pane root;
		root = FXMLLoader.load(getClass().getResource("main.fxml"));
		Scene scene = new Scene(root,800,500);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		return scene;
	}

}
