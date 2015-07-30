package PDF_Maker;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class CustomButton extends Button {

	private String normalStyle = "-fx-background-color: #E32740; -fx-text-fill: white; -fx-padding: 15px;"
			+ "-fx-background-radius: 10px; -fx-font-weight: bold;";
	
	
	public CustomButton(String message){
		super(message);
		this.setStyle(normalStyle);
		this.setOnMouseEntered(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {
				changeStyle();
			}});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent e) {
				revertStyle();
			}});
	}
	
	private void changeStyle(){
		this.setStyle("-fx-background-color: #F22C46; -fx-text-fill: white; -fx-padding: 15px;"
			+ "-fx-background-radius: 10px; -fx-font-weight: bold;");
	}
	
	private void revertStyle(){
		this.setStyle(normalStyle);
	}
}
