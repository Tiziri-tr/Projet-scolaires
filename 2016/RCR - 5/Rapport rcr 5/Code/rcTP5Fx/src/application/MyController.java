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
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import rc.RcMatlabManagement;

public class MyController implements Initializable{
	
	@FXML private AnchorPane anchorePaneFenetre;
	@FXML private TextField champsFichier;
	@FXML private TextField nb_noeuds;
	@FXML private TextField nb_parents;
	@FXML private TextArea Log;
	@FXML private TextArea textAreaNombreki;
	@FXML private TextArea textAreaKi;
	@FXML private TextArea textAreaGraphe;
	@FXML private Pane paneLog;
	@FXML private Pane panNoeudsParents;
	@FXML private TabPane tabPane;
	
	private File fichier;
	
	public void initialize(URL url, ResourceBundle rb) {
//		Log.setText("\n\n\n\n\n\n\n\n\n\n\n\n");
		paneLog.setVisible(false);
		panNoeudsParents.setVisible(false);
		tabPane.setVisible(false);
		nb_noeuds.setText("54");
		nb_parents.setText("3");
	}
    
	@FXML protected void openFile(Event event){
		FileChooser selecteurFichier = new FileChooser();
		selecteurFichier.setInitialDirectory(new File("C:/anahla/PNT"));
		FileChooser.ExtensionFilter filterCNF = new FileChooser.ExtensionFilter("MATLAB (*.m)", "*.m");
		selecteurFichier.getExtensionFilters().add(filterCNF);
		try{
			fichier = selecteurFichier.showOpenDialog(anchorePaneFenetre.getParent().getScene().getWindow());
			champsFichier.setText(fichier.getPath());
			Log.appendText((Log.getText().isEmpty()?"":"\n")+fichier.getName()+" ----> selected");
			panNoeudsParents.setVisible(true);
		} catch(NullPointerException e){
			Log.appendText((Log.getText().isEmpty()?"":"\n")+"Nothing selected");

		}
	}

	
	@FXML protected void openLog(Event event){
		if(paneLog.isVisible()){
			anchorePaneFenetre.getParent().getScene().getWindow().setHeight(347);;
			paneLog.setVisible(false);
		}
		else{
			anchorePaneFenetre.getParent().getScene().getWindow().setHeight(607);;
			paneLog.setVisible(true);
		}
			

	}
	
	@FXML protected void btn_annuler(Event event){
		nb_noeuds.setText("");
		nb_parents.setText("");
	}
	
	@FXML protected void btn_valider(Event event){
		if(nb_noeuds.getText().compareTo("")==0 && nb_parents.getText().compareTo("")==0)
			new MyPopUp("Veuillez remplir les deux champs S.V.P", anchorePaneFenetre.getScene().getWindow());
		else{
			Log.appendText((Log.getText().isEmpty()?"":"\n")+"Modification des tailles effectuée.");
			Log.appendText((Log.getText().isEmpty()?"":"\n")+"Lancement de l'exécution du fichier sur matlab...");
			tabPane.setVisible(true);
			
			RcMatlabManagement rc = new RcMatlabManagement(fichier.getName());
			int nbNoeuds= Integer.parseInt(nb_noeuds.getText());
			int nbParents = Integer.parseInt(nb_parents.getText());
			
			long debut = System.currentTimeMillis();
			rc.modifierNoeudsParents(nbNoeuds,nbParents);
			rc.execInCmd("\"C:/Program Files/MATLAB/R2014a/bin/matlab.exe\" -nodisplay -nosplash -nodesktop -r run('C:/anahla/PNT/"+fichier.getName()+"');exit;");
			long fin = System.currentTimeMillis();

			
			textAreaGraphe.setText(rc.fileToString("C:/cygwin/home/licence/graphe.m"));
			textAreaKi.setText(rc.fileToString("C:/cygwin/home/licence/ki.m"));
			textAreaNombreki.setText(rc.fileToString("C:/cygwin/home/licence/nombreki.m"));
			if(new File("C:/cygwin/home/licence/graphe.m").length()>0)
				Log.appendText((Log.getText().isEmpty()?"":"\n")+"Fin de l'execution avec succès. Temps="+(fin-debut)+"ms");
			else
				Log.appendText((Log.getText().isEmpty()?"":"\n")+"Une erreur est survenue pour noeuds="+nbNoeuds+" et parents="+nbParents+".  Temps="+(fin-debut)+"ms");


		}
	}
	
	
	
	@FXML protected void sousMenu_cygwin(Event event){
		RcMatlabManagement rc = new RcMatlabManagement();
		rc.execInCmd("C:/cygwin/bin/mintty.exe");
		
	}
	@FXML protected void sousMenu_delete(Event event){
		champsFichier.setText("");
		panNoeudsParents.setVisible(false);
		tabPane.setVisible(false);
		fichier = null;
	}
	@FXML protected void sousMenu_about(Event event){
		new MyPopUp("\tApplication créer Par: \n\t\t\tGUELLAL Tiziri\n \n \tdans le cadre d'un TP de Représentation des connaissances \n\tet raisonnement automatique 2 \n\tM2 SII année 2015-2016", anchorePaneFenetre.getScene().getWindow());
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