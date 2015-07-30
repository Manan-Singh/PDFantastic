package PDF_Maker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.ImageIOUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PictureMakerBox extends HBox {

	private CustomButton saveAsPDF;
	private CustomButton saveAsImage;
	
	
	public PictureMakerBox(){
		super();
		this.setPadding(new Insets(50, 10, 10, 120));
		this.setSpacing(100);
		saveAsPDF = new CustomButton("Save as PDF");
		saveAsImage = new CustomButton("Save as Image");
		this.getChildren().add(saveAsPDF);
		this.getChildren().add(saveAsImage);
		saveAsPDF.setOnAction(new SaveAsPDF());
		saveAsImage.setOnAction(new SaveAsImage());
	}
	
	private class SaveAsPDF implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			Stage secStage = new Stage();
			secStage.setTitle("Pick picture file");
			HBox root = new HBox();
			root.setPadding(new Insets(10, 10, 10, 10));
			root.setSpacing(5);
			TextField fileField = new TextField();
			fileField.setPromptText("File path");
			TextField xPos = new TextField();
			xPos.setPrefColumnCount(5);
			xPos.setPromptText("x position");
			TextField yPos = new TextField();
			yPos.setPrefColumnCount(5);
			yPos.setPromptText("y position");
			Button select = new Button("Select");
			select.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent e) {
					String fileName = fileField.getText();
					File file = new File(fileName);
					if(!fileName.equals("")){
						if(file.exists()){
							PDDocument document = new PDDocument();
							PDPage page = new PDPage();
							document.addPage(page);
							PDXObjectImage pdxImage = null;
							PDPageContentStream contentStream;
							int x = Integer.parseInt(xPos.getText());
							int y = Integer.parseInt(yPos.getText());
							try {
								contentStream = new PDPageContentStream(document, page);
								if(file.toString().substring(file.toString().indexOf(".") + 1).equals("jpg")){
									pdxImage = new PDJpeg(document, new FileInputStream(file));
								}else{
									BufferedImage bi = ImageIO.read(file);
									pdxImage = new PDPixelMap(document, bi);
								}
								contentStream.drawImage(pdxImage, x, y);
								contentStream.close();
								document.save(fileName.substring(0, fileName.indexOf(".")) + "ToPDF.pdf");
								document.close();
							} catch (IOException e1) {
								ErrorStage es = new ErrorStage("There was I/O error!");
							} catch (COSVisitorException e1) {
								ErrorStage es = new ErrorStage("An error occurred!");
							}
						}else{
							ErrorStage es = new ErrorStage("Such a file does not exist!");
						}
					}else{
						new ErrorStage("Input a file name to use!");
					}
					
				}
				
			});
			root.getChildren().addAll(fileField, xPos, yPos, select);
			Scene scene = new Scene(root, 500, 50);
			secStage.setScene(scene);
			secStage.show();
		}
		
	}
	
	private class SaveAsImage implements EventHandler<ActionEvent>{

		public void handle(ActionEvent e) {
			Stage secStage = new Stage();
			secStage.setTitle("Pick pdf file");
			VBox root = new VBox();
			root.setPadding(new Insets(10, 10, 10, 10));
			HBox cell1 = new HBox();
			HBox cell2 = new HBox();
			cell1.setPadding(new Insets(10, 10, 10, 10));
			TextField fileField = new TextField();
			Button select = new Button("Select");
			TextField pageField = new TextField("1");
			pageField.setPrefColumnCount(1);
			Label pgNmbr = new Label("Page # : ");
			select.setOnAction(new EventHandler<ActionEvent>() {

				public void handle(ActionEvent e) {
					String fileName = fileField.getText();
					if(!fileName.equals("")){
						int pageNumber = Integer.parseInt(pageField.getText());
						File file = new File(fileName);
						if(file.exists()){
							PDDocument document = null;
							try {
								document = PDDocument.load(file);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							PDPage pageToConvert = null;
							pageToConvert = (PDPage) document.getDocumentCatalog().getAllPages().get(pageNumber - 1);
							try {
								BufferedImage bi = pageToConvert.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
								ImageIOUtil.writeImage(bi, file.getName().substring(0, file.getName().indexOf(".")) + "page" +  pageNumber + ".png", 300);
								document.close();
								
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}else{
						new ErrorStage("Please input the name of the file!");
					}
					secStage.close();
					
				}
			});
			cell1.getChildren().addAll(fileField, select);
			cell2.getChildren().addAll(pgNmbr, pageField);
			root.getChildren().add(cell1);
			root.getChildren().add(cell2);
			Scene scene = new Scene(root, 300, 100);
			secStage.setScene(scene);
			secStage.show();
		}
		
	}
	
}

















