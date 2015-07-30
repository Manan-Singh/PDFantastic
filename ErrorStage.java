package PDF_Maker;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ErrorStage extends Stage {
	
	public ErrorStage(String errorMessage){
		this.setResizable(false);
		this.setTitle("Error");
		StackPane root = new StackPane();
		Scene scene = new Scene(root);
		Label messageLabel = new Label(errorMessage);
		root.getChildren().add(messageLabel);
		root.setPrefSize(messageLabel.getPrefWidth(), messageLabel.getPrefHeight() + 100);
		this.setScene(scene);
		this.show();
	}
}
