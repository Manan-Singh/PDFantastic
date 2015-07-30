package PDF_Maker;

import java.util.ArrayList;

import org.apache.pdfbox.pdmodel.PDPage;

import javafx.collections.ObservableList;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class PDFTextFormatter extends Text {
	
	private ArrayList<String> formattedParagraphs;
	private int charPosition = 0;
	private Text stringSoFar;
	private ObservableList paragraphs;
	
	public PDFTextFormatter(ObservableList paragraphs){
		super(); 
		this.paragraphs = paragraphs;
		stringSoFar = new Text();
		stringSoFar.setFont(Font.font("Helvetica", FontWeight.BOLD, 12));
		formattedParagraphs = new ArrayList<String>();
		for(int i = 0; i < paragraphs.size(); i++){
			formattedParagraphs.add("");
		}
		format();
	}
	
	private void format(){
		for(int i = 0; i < paragraphs.size(); i++){
			//Text nextChar = new Text("" + paragraphs.get(i).toString().charAt(charPosition));
			if(paragraphs.get(i).toString().length() != 0){
				Text nextChar = new Text("" + paragraphs.get(i).toString().charAt(charPosition));
				int lastStop = 0;
				while(charPosition < paragraphs.get(i).toString().length()){
					while(stringSoFar.getLayoutBounds().getWidth() + nextChar.getLayoutBounds().getWidth() < 
							PDPage.PAGE_SIZE_LETTER.getWidth() - 100){
						stringSoFar.setText(stringSoFar.getText() + nextChar.getText());
						//System.out.println("StringsoFar: " + stringSoFar.getText() + " CharPos: " + charPosition);
						charPosition++;
						if(charPosition > paragraphs.get(i).toString().length() - 1){
							break;
						}
						nextChar.setText("" + paragraphs.get(i).toString().charAt(charPosition));
					}
					int lastSpace = stringSoFar.getText().lastIndexOf(" ");
					if(lastSpace == -1){
						formattedParagraphs.set(i, formattedParagraphs.get(i) + (stringSoFar.getText()
								+ "-"));
						lastStop += stringSoFar.getText().length();
						stringSoFar.setText("");
						//lastStop = charPosition - 1;
					}else{
						
						
						if(charPosition == paragraphs.get(i).toString().length()){
							lastStop += stringSoFar.getText().length();
							//System.out.println("CharPos: " + charPosition + "ls : " + lastStop);
							formattedParagraphs.set(i, formattedParagraphs.get(i) + (stringSoFar.getText() + "-"));
							
						}else{
							lastStop += stringSoFar.getText().substring(0, lastSpace).length();
							//System.out.println("CharPos: " + charPosition + "| ls : " + lastStop);
							formattedParagraphs.set(i, formattedParagraphs.get(i) + (stringSoFar.getText().substring(0, lastSpace) + "-"));
							nextChar.setText("" + paragraphs.get(i).toString().charAt(lastStop));
						}
						stringSoFar.setText("");
						charPosition = lastStop;
						
						stringSoFar.setText("");
					}
				}
			}
			
			charPosition = 0;
		}
//		for(String x : formattedParagraphs){
//			System.out.println(x);
//		}
	}
	
	public ArrayList<String> getFormattedParagraphs(){
		return formattedParagraphs;
	}
}