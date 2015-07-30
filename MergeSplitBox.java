package PDF_Maker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.apache.pdfbox.util.Splitter;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;

public class MergeSplitBox extends HBox {

	private CustomButton mergePDF;
	private CustomButton splitPDF;
	
	public MergeSplitBox(){
		super();
		this.setPadding(new Insets(50, 10, 10, 120));
		this.setSpacing(100);
		mergePDF = new CustomButton("Merge PDFs");
		mergePDF.setOnAction(new MergePDF());
		splitPDF = new CustomButton("Split PDFs");
		splitPDF.setOnAction(new SplitPDF());
		this.getChildren().addAll(mergePDF, splitPDF);
	}
	
	private class MergePDF implements EventHandler<ActionEvent>{

		public void handle(ActionEvent e) {
			Stage secStage = new Stage();
			secStage.setResizable(false);
			secStage.setTitle("Add PDFs to merge");
			PDFMergerUtility merger = new PDFMergerUtility();
			BorderPane root = new BorderPane();
			ObservableList<String> pdfFiles = FXCollections.observableArrayList();
			ListView<String> fileView = new ListView<String>(pdfFiles);
			root.setLeft(fileView);
			VBox rightContainer = new VBox();
			rightContainer.setPadding(new Insets(10, 10, 10, 10));
			rightContainer.setSpacing(5);
			root.setRight(rightContainer);
			TextField fileField = new TextField();
			TextField destination = new TextField();
			Button merge = new Button("Merge All");
			Button addToList = new Button("Add to list");
			merge.setMaxWidth(Double.MAX_VALUE);
			addToList.setMaxWidth(Double.MAX_VALUE);
			addToList.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent e) {
					String fileName = fileField.getText();
					fileField.setText("");
					File file = new File(fileName);
					if(file.exists() && file.getName().substring(file.getName().indexOf(".") + 1).equals("pdf")){
						merger.addSource(file);
						pdfFiles.add(fileName);
					}else{
						new ErrorStage("There is a problem with the selected file");
					}
				}});
			
			merge.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent e) {
					String destName = destination.getText();
					if(!destName.equals("")){
						destination.setText("");
						File file = new File(destName);
						if(!file.exists() && (file.getName().substring(file.getName().indexOf(".") + 1).equals("pdf"))){
							try {
								if(pdfFiles.size() <= 1){
									new ErrorStage("You need at least two pdf files in the list");
								}else{
									merger.setDestinationFileName(destName);
									merger.mergeDocuments();
								}
							} catch (COSVisitorException e1) {
								new ErrorStage("Something went wrong while visiting the pdf!");
							} catch (IOException e1) {
								new ErrorStage("An I/O problem ocurred!");
							}
						}else{
							new ErrorStage("The destination file either already exists or did not contain the \".pdf\" extension.");
						}
					}else{
						new ErrorStage("Input destination name");
					}
				}});
			
			fileField.setPromptText("Input pdf file path");
			destination.setPromptText("Create a destination file");
			rightContainer.getChildren().addAll(fileField, destination, addToList, merge);
			Scene scene = new Scene(root, 500, 200);
			secStage.setScene(scene);
			secStage.show();
		}
		
	}
	
	private class SplitPDF implements EventHandler<ActionEvent> {

		private File file;
		
		public void handle(ActionEvent e) {
			file = null;
			Stage secStage = new Stage();
			secStage.setResizable(false);
			BorderPane root = new BorderPane();
			VBox leftContainer = new VBox();
			StackPane centerText = new StackPane();
			Text description = new Text("Input a pdf file");
			centerText.getChildren().add(description);
			leftContainer.setPadding(new Insets(10, 10, 10, 10));
			leftContainer.setSpacing(5);
			TextField fileField = new TextField();
			fileField.setPromptText("Input a pdf file");
			Button confirm = new Button("Confirm");
			VBox rightContainer = new VBox();
			rightContainer.setPadding(new Insets(20, 20, 20, 20));
			rightContainer.setSpacing(10);
			HBox specificSplit = new HBox();
			VBox directoryArea = new VBox();
			Text splitSent1 = new Text("Split at every ");
			TextField pageField = new TextField("1");
			pageField.setPrefColumnCount(2);
			Text splitSent2 = new Text(" page(s)");
			specificSplit.getChildren().addAll(splitSent1, pageField, splitSent2);
			Text info = new Text("Choose a name for the directory\n to make the files in:");
			TextField dirField = new TextField();
			Button splitPages = new Button("Split Pages");
			splitPages.setMaxWidth(Double.MAX_VALUE);
			directoryArea.getChildren().addAll(info, dirField);
			rightContainer.getChildren().addAll(specificSplit, directoryArea, splitPages);
			confirm.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent e) {
					String fileName = fileField.getText();
					if(!fileName.equals("")){
						if(fileName.substring(fileName.indexOf(".") + 1).equals("pdf")){
							File theFile = new File(fileName);
							if(theFile.exists()){
								fileField.setEditable(false);
								root.setRight(rightContainer);
								leftContainer.getChildren().remove(confirm);
								file = theFile;
							}else{
								new ErrorStage("There is no file with that name");
							}
						}else{
							new ErrorStage("Make sure the file contains the \".pdf\" extension!");
						}
					}else{
						new ErrorStage("Input an existing pdf file name!");
					}
				}});
			
			confirm.setMaxWidth(Double.MAX_VALUE);
			splitPages.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent e) {
					PDDocument doc = null;
					try {
						doc = PDDocument.load(file);
					} catch (IOException e1) {
						new ErrorStage("An I/O Exception ocurred!");
					}
					if(doc != null){
						List pages = doc.getDocumentCatalog().getAllPages();
						if(pages.size() > 1){
							int splitAt = -1;
							try{
								splitAt = Integer.parseInt(pageField.getText());
							}catch(NumberFormatException e1){
								new ErrorStage("Please input a valid numer");
							}catch(NullPointerException e2){
								new ErrorStage("Please enter the number at which the pages should be split");
							}
							if(splitAt > 0 && splitAt < pages.size()){
								String dirName = dirField.getText();
								if(!dirName.equals("")){
									File newDir = new File(dirName);
									if(!newDir.exists()){
										newDir.mkdirs();
									}
									if(newDir.isDirectory()){
										Splitter splitter = new Splitter();
										try {
											splitter.setStartPage(1);
											splitter.setSplitAtPage(splitAt);
											List<PDDocument> newPages = splitter.split(doc);
											int counter = 1;
											for(PDDocument x : newPages){
												x.save(dirName + "//" + file.getName().substring(0, file.getName().indexOf(".")) + "page" + counter + ".pdf");
												counter++;
												x.close();
											}
											secStage.close();
										} catch (IOException e1) {
											new ErrorStage("An I/O error ocurred!");
										} catch (COSVisitorException e1) {
											e1.printStackTrace();
										}
									}else{
										new ErrorStage("That file isn't a directory!");
									}
								}else{
									new ErrorStage("Please input a directory name!");
								}
								
							}else{
								new ErrorStage("Please input a valid numer for where the pages should be split");
							}
						}else{
							new ErrorStage("The document must have more than one page, pick a different one!");
						}
					}
				}});
			leftContainer.getChildren().addAll(centerText, fileField, confirm);
			root.setLeft(leftContainer);
			Scene scene = new Scene(root, 500, 200);
			secStage.setScene(scene);
			secStage.show();
		}
		
	}
}
