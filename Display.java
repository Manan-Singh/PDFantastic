package PDF_Maker;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Display extends Application {
	
	BorderPane root;
	private ObservableList<Label> options;
	private ListView<Label> optionsList;
	private StackPane homeScreen;
	public static String buttonStyle = "-fx-background-color: #E32740; -fx-text-fill: white; -fx-padding: 15px;"
			+ "-fx-background-radius: 10px; -fx-font-weight: bold;";
	
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("PDFantastic");
		primaryStage.setResizable(false);
		root = new BorderPane();
		root.setStyle("-fx-background-color: #F29C2C");
		options = FXCollections.observableArrayList(new Label("Make Text Document"), 
				new Label("Make Picture Document"), new Label("Merge/Split Document"));
		optionsList = new ListView<Label>(options);
		options.get(0).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				setToMakeTextDoc();
			}});
		options.get(1).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				setToMakePicDoc();
			}});
		options.get(2).addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				setToMergeSplitBox();
			}});
		root.setLeft(optionsList);
		homeScreen = new StackPane();
		Text introText = new Text("Select an option");
		introText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 50));
		introText.setFill(Color.BLUE);
		homeScreen.getChildren().add(introText);
		root.setCenter(homeScreen);
		
		Scene scene = new Scene(root, 800, 135);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void setToMakeTextDoc(){
		TextMakerBox tmb = new TextMakerBox();
		root.setCenter(tmb);
	}
	
	private void setToMakePicDoc(){
		PictureMakerBox pmb = new PictureMakerBox();
		root.setCenter(pmb);
	}
	
	private void setToMergeSplitBox(){
		MergeSplitBox msb = new MergeSplitBox();
		root.setCenter(msb);
	}
	
}
