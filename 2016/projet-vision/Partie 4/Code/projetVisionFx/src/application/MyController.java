package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class MyController implements Initializable{
	
	@FXML private AnchorPane anchorePaneFenetre;
	@FXML private TextField champsFichier;
	@FXML private TextArea Log;
	@FXML private TextArea resultat;
	@FXML private Pane paneLog;
	@FXML private ImageView imgView;
	@FXML private Label resLabel;


	private File fichier;
	private String cheminImages;
	
	public void initialize(URL url, ResourceBundle rb) {
//		Log.setText("\n\n\n\n\n\n\n\n\n\n\n\n");
		fichier = null;
		cheminImages = "C:/Users/Tiziri/workspace/projetVisionFx/images_test/";
		resLabel.setVisible(false);
		resultat.setVisible(false);
	}
    


	
	@FXML protected void decider(Event event){
		if(fichier!=null){
			resLabel.setVisible(true);
			resultat.setVisible(true);
			MatlabVision mv = new MatlabVision();
			mv.modifierImage("textures.m", fichier.getName());
			mv.execInCmd("\"C:/Program Files/MATLAB/R2014a/bin/matlab.exe\" -nodisplay -nosplash -nodesktop -r run('C:/Users/Tiziri/Documents/MATLAB/textures.m');exit;");
			String resTotal = mv.getResult("C:/Users/Tiziri/Documents/MATLAB/resultat.txt");
			String res[] = resTotal.split("\n");
			if(res[0].compareTo("Taille Différente!")!=0){
				resultat.setText   ("\nRessemblance : "+(res[0].compareTo("1")==0?"Vrai\n\n":"Faux\n\n"));
				resultat.appendText("Distance          : "+res[1]+"\n\n");
				resultat.appendText("Min                 : "+res[2]+"\n\n");
				resultat.appendText("Max                : "+res[3]+"\n\n");
			}
			else{
				resultat.setText("Ressemblance: Faux\n");
				resultat.appendText("\n"+res[0]);
			}
		}else
			new MyPopUp("Veuillez choisir un fichier SVP", anchorePaneFenetre.getScene().getWindow());

	}
	
	
	@FXML protected void openFile(Event event){
		FileChooser selecteurFichier = new FileChooser();
		selecteurFichier.setInitialDirectory(new File(cheminImages));
//		FileChooser.ExtensionFilter filterTIF = new FileChooser.ExtensionFilter("Images (*.tif)", "*.tif");
//		selecteurFichier.getExtensionFilters().add(filterTIF);
		try{
			fichier = selecteurFichier.showOpenDialog(anchorePaneFenetre.getParent().getScene().getWindow());
			champsFichier.setText(fichier.getPath());
			Log.appendText((Log.getText().isEmpty()?"":"\n")+fichier.getName()+" ----> selected");
			
//			String newUrl = "file:/"+"";
			File f = new File("C:/Users/Tiziri/workspace/projetVisionFx/images_test-jpg/"+fichier.getName().substring(0,fichier.getName().length()-4)+".jpg");
//			System.out.println(f.toURI().toString());
			Image img = new Image(f.toURI().toString());
//			Image img = new Image(fichier.toURI().toString());
			imgView.setImage(img);

		} catch(NullPointerException e){
			e.printStackTrace();
			Log.appendText((Log.getText().isEmpty()?"":"\n")+"Nothing selected");

		}
	}

	
	@FXML protected void openLog(Event event){
		if(paneLog.isVisible()){
			anchorePaneFenetre.getParent().getScene().getWindow().setHeight(523); // 489
			paneLog.setVisible(false);
		}
		else{
			anchorePaneFenetre.getParent().getScene().getWindow().setHeight(578);
			paneLog.setVisible(true);
		}
			

	}
	
	@FXML protected void sousMenu_close(Event event){

	}
	@FXML protected void sousMenu_delete(Event event){
		champsFichier.setText("");
		fichier = null;
	}
	@FXML protected void sousMenu_about(Event event){
		new MyPopUp("\tApplication créer Par: \n\t\t\tGUELLAL Tiziri\n \n \tdans le cadre d'un TP de Vision Artificielle\n\tM2 SII année 2015-2016", anchorePaneFenetre.getScene().getWindow());
	}
//	@FXML protected void appliquerAlgo(Event event){
//	}
	
//	textArea.appendText((textArea.getText().isEmpty()?"":"\n")+fichier.getName()+" ----> selected");
//	new MyPopUp("Veuillez selectionner un fichier d'abord", anchorePaneFenetre.getScene().getWindow());

	class MyPopUp extends Stage{
		private Stage thiss;
		public MyPopUp(String message, Window origine){
			VBox root = new VBox();
			thiss=this;
			root.setAlignment(Pos.CENTER);
			Button tabala3 = new Button("Ok");
			root.getChildren().addAll(new Label(message), tabala3);
			Scene sc = new Scene(root, 400, 175);
			this.setScene(sc);
			this.initOwner(origine);
			this.initModality(Modality.WINDOW_MODAL);
			this.initStyle(StageStyle.DECORATED);
			this.show();
			tabala3.setOnAction(new EventHandler<ActionEvent>() {	
				@Override
				public void handle(ActionEvent arg0) {
					thiss.close();
				}
			});
			tabala3.setOnKeyPressed(new EventHandler<KeyEvent>() {	
				@Override
				public void handle(KeyEvent arg0) {
					thiss.close();
				}
			});
		}
	}
}
