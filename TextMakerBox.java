package PDF_Maker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFTextStripper;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class TextMakerBox extends HBox {
	
	private CustomButton makePDF;
	private CustomButton convertPDF;
	
	public TextMakerBox(){
		super();
		this.setPadding(new Insets(50, 10, 10, 120));
		this.setSpacing(100);
		makePDF = new CustomButton("Make PDF");
		convertPDF = new CustomButton("Txt to PDF");
		this.getChildren().addAll(makePDF, convertPDF);
		makePDF.setOnAction(new CreatePDF());
		convertPDF.setOnAction(new ConvertPDF());
		makePDF.setStyle(Display.buttonStyle);
		convertPDF.setStyle(Display.buttonStyle);
	}
	
	private class CreatePDF implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			Stage secStage = new Stage();
			BorderPane root = new BorderPane();
			StackPane contentArea = new StackPane();
			Button save = new Button("Save");
			TextField fileInputField = new TextField();
			fileInputField.setPromptText("Name of the new file");
			HBox menu = new HBox();
			menu.setPadding(new Insets(5, 5, 5, 5));
			menu.setSpacing(20);
			TextArea ta = new TextArea("Content of the PDF goes here!!!");
			ta.setWrapText(true);
			contentArea.getChildren().add(ta);
			root.setCenter(contentArea);
			root.setTop(menu);
			menu.getChildren().addAll(save, fileInputField);
			save.setOnAction(new EventHandler<ActionEvent>(){
				
				public void handle(ActionEvent e) {
					String fileName = fileInputField.getText();
					File possibleFile = new File(fileName);
					if(!possibleFile.exists()){
						if(fileName.substring(fileName.indexOf(".") + 1).equals("pdf")){
							//String data = ta.getText();
							PDFTextFormatter formatter = new PDFTextFormatter(ta.getParagraphs());
							//Apache API initialization
							PDDocument document = new PDDocument();
							
							PDPage firstPage = new PDPage();
							PDPage page = firstPage;
							document.addPage(page);
							PDFont font = PDType1Font.HELVETICA_BOLD;
							try {
								PDPageContentStream contentStream = new PDPageContentStream(document, page);
								int x = 50, y = 700;
								for(int i = 0; i < formatter.getFormattedParagraphs().size(); i++){
									String[] brokenLines = formatter.getFormattedParagraphs().get(i).split("-");
									for(int j = 0; j < brokenLines.length; j++){
										contentStream.beginText();
										contentStream.setFont(font, 12);
										contentStream.moveTextPositionByAmount(x, y);
										contentStream.drawString(brokenLines[j]);
										contentStream.endText();
										y -= 20;
										if(y < 200){
											page = new PDPage();
											document.addPage(page);
											contentStream.close();
											contentStream = new PDPageContentStream(document, page);
											y = 700;
										}
									}
									
								}
								contentStream.close();
								document.save(fileName);
								document.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							} catch (COSVisitorException e1) {
								e1.printStackTrace();
							}
							secStage.close();
						}else{
							new ErrorStage("Input a file name with the \".pdf\" extension!");
						}
					}else{
						new ErrorStage("A file with that name already exists!");
					}
				}
			});
			Scene scene = new Scene(root, PDPage.PAGE_SIZE_LETTER.getWidth(), PDPage.PAGE_SIZE_LETTER.getHeight());
			secStage.setScene(scene);
			secStage.show();
		}
		
	}
	
	private class ConvertPDF implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			Stage secStage = new Stage();
			secStage.setTitle("Type in a txt file");
			TextField fileField = new TextField();
			fileField.setPromptText("Input a txt file path");
			Button loadFile = new Button("Load File");
			HBox menu = new HBox();
			menu.setPadding(new Insets(10, 10, 10, 10));
			menu.getChildren().add(fileField);
			menu.getChildren().add(loadFile);
			loadFile.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent e) {
					String nameOfFile = fileField.getText();
					if(!nameOfFile.equals("")){
						File file = new File(nameOfFile);
						if(file.exists() && file.isFile()){
							String fileData = "";
							String line = "";
							ArrayList<String> paragraphList = new ArrayList<String>();
							ObservableList<String> list = FXCollections.observableList(paragraphList);
							try {
								BufferedReader reader = new BufferedReader(new FileReader(file));
								while((line = reader.readLine()) != null){
									if(line.equals("") || line == null){
										list.add(new String(fileData));
										fileData = "";
									}else{
										fileData += line;
									}
									
								}
								list.add(new String(fileData));
								PDFTextFormatter formatter = new PDFTextFormatter(list);
								
								PDDocument document = new PDDocument();
								
								PDPage firstPage = new PDPage();
								PDPage page = firstPage;
								document.addPage(page);
								PDFont font = PDType1Font.HELVETICA_BOLD;
								try {
									PDPageContentStream contentStream = new PDPageContentStream(document, page);
									int x = 50, y = 700;
									for(int i = 0; i < formatter.getFormattedParagraphs().size(); i++){
										String[] brokenLines = formatter.getFormattedParagraphs().get(i).split("-");
										for(int j = 0; j < brokenLines.length; j++){
											contentStream.beginText();
											contentStream.setFont(font, 12);
											contentStream.moveTextPositionByAmount(x, y);
											contentStream.drawString(brokenLines[j]);
											contentStream.endText();
											y -= 20;
											if(y < 200){
												page = new PDPage();
												document.addPage(page);
												contentStream.close();
												contentStream = new PDPageContentStream(document, page);
												y = 700;
											}
										}
										
									}
									contentStream.close();
									document.save(file.getName().substring(0, file.getName().indexOf(".")) + ".pdf");
									document.close();
								} catch (IOException e1) {
									e1.printStackTrace();
								} catch (COSVisitorException e1) {
									e1.printStackTrace();
								}
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							secStage.close();
						}else{
							
						}
					}else{
						new ErrorStage("Please input a text file to read!");
					}
					
				}
			});
			Scene scene = new Scene(menu, 310, 50);
			secStage.setScene(scene);
			secStage.show();
		}
		
	}
}
