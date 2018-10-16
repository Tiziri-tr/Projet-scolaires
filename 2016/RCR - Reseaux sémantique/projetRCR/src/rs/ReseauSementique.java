package rs;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import composants.Classe;
import composants.Lien;
import composants.Marquage;

public class ReseauSementique {
	private ArrayList<Classe> listC;
	Graph g;
	
	public void construireGraphe(){
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		g = new SingleGraph("Tiziri");
		g.addAttribute("ui.quality");
        g.addAttribute("ui.antialias");
//		g.display();
		Viewer v = g.display(true);
		g.setAttribute("stylesheet", 
				  "node { "
                + "     shape: box; "
                + "     padding: 5px; "
                + "     fill-color: white; "
                + "     stroke-mode: plain; "
                + "     size-mode: fit; "                
                + "} "
                
                + "edge {"
                + " arrow-shape: arrow ;"
                + " stroke-color: red ;"

                + " shape: line;"
                + "}"
                
                + "edge.sorteDeNonStrict {"
//                + ""
//                + ""
                +"}"
                
                + "edge.sorteDeStrict {"
                +"}"     
                
                + "edge.exception {"
                + " stroke-mode: dots;"
                +"}"
                
                + "edge.nonSorteDeStrict {"
                +"}"
                
                + "edge.nonSorteDeNonStrict {"
                +"}"

				);
		for (Classe c : listC) {
			g.addNode(c.getNom()).addAttribute("ui.label", c.getNom() + "");
		}
		
		for (Classe c : listC) {
			if (c.getLiens()==null)
				continue;
			for (Lien l : c.getLiens()){
				String id = l.getSource()+","+c.getNom();
				g.addEdge(id, l.getSource(), c.getNom(), true).addAttribute("ui.label", l.getLibelle()+"");

				if(l.getType().compareTo("sorteDeNonStrict")==0)
					g.getEdge(id).addAttribute("ui.class", "sorteDeNonStrict");
				else if (l.getType().compareTo("sorteDeStrict")==0)
					g.getEdge(id).addAttribute("ui.class", "sorteDeStrict");
				else if (l.getType().compareTo("exception")==0)
					g.getEdge(id).addAttribute("ui.class", "exception");
				else if (l.getType().compareTo("nonSorteDeStrict")==0)
					g.getEdge(id).addAttribute("ui.class", "nonSorteDeStrict");
				else if (l.getType().compareTo("nonSorteDeNonStrict")==0)
					g.getEdge(id).addAttribute("ui.class", "nonSorteDeNonStrict"); //◁
				else
					g.getEdge(id).addAttribute("ui.class","normal"); 
			}
		}
		try {
			Thread.sleep(3000);
			v.disableAutoLayout();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public String afficherReseau(){
		String res = "";
		for (Classe c : listC) {
			if(c.getLiens()!=null){
				res += "\n\n"+c.getNom();
				for (Lien l : c.getLiens()) {
					res += "\n\t"+l.getDessin()+" "+l.getLibelle()+" "+"- "+l.getSource(); //―
				}
			}
		}
			
		return res;
	}
	
	public void ecrire(String rep, String chaineAEcrire){
		try{
			FileWriter fw = new FileWriter( rep);
			BufferedWriter bw = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(rep), "utf8"));
			PrintWriter fichierSortie = new PrintWriter (bw);
			fichierSortie.write(chaineAEcrire);
			fichierSortie.close();
			bw.close();
			fw.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public String propagation(String source, String lien, String dest){
		g.setAttribute("stylesheet", 
				  "node { "
              + "     shape: box; "
              + "     padding: 5px; "
              + "     fill-color: white; "
              + "     stroke-mode: plain; "
              + "     size-mode: fit; "                
              + "} "
              
              + "node.top{"
              + " stroke-color: red; "
              + " stroke-width: 2px; "

              + "}"
              
              + "node.bot{"
              + " stroke-color: blue; "
              + " stroke-width: 2px; "
              + "}"
              
              + "edge {"
              + " arrow-shape: arrow ;"
              + " shape: line;"
              + "}"
              
              + "edge.sorteDeNonStrict {"
//              + ""
//              + ""
              +"}"
              
              + "edge.sorteDeStrict {"
              +"}"     
              
              + "edge.exception {"
              + " stroke-mode: dots;"
              +"}"
              
              + "edge.nonSorteDeStrict {"
              +"}"
              
              + "edge.nonSorteDeNonStrict {"
              +"}"
				);
		source = source.toUpperCase();
		lien = lien.toLowerCase();
		dest = dest.toUpperCase();
		
		String res= "";
		int posSource = getPosInList(source);
		int posDest = getPosInList(dest);
		int posCourant=0;
		int etape = 0;
		int tailleHaut;
		int tailleBas;
		int cpt=0;
		ArrayList<Classe> successeurAMarquerHaut = new ArrayList<Classe>();
		ArrayList<Classe> successeurAMarquerBas = new ArrayList<Classe>();
		if(posSource==-1)
			return "Source introuvable";
		if(posDest==-1)
			return "Destination introuvable";
		
		successeurAMarquerHaut.add(listC.get(posSource));		
		successeurAMarquerBas.add(listC.get(posDest));
		
		while(!successeurAMarquerHaut.isEmpty() || !successeurAMarquerBas.isEmpty()){
			res+= ("\n\t*****************ETAPE"+etape+"*****************");
			try {
				Thread.sleep(2001);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tailleHaut = successeurAMarquerHaut.size();
			for(int i=0; i<tailleHaut;i++){	
				if(!successeurAMarquerHaut.get(i).getMarquage().isMarked()){
					successeurAMarquerHaut.get(i).setMarquage(new Marquage(true,"M1")); 
					res+= ("\n"+successeurAMarquerHaut.get(i).getNom()+" M1("+etape+")");
					g.getNode(successeurAMarquerHaut.get(i).getNom()).addAttribute("ui.class", "top");
					
					if(successeurAMarquerHaut.get(i).getLiens()!=null)
						for (Lien l : successeurAMarquerHaut.get(i).getLiens()) {
							posCourant = getPosInList(l.getSource());
							if(posCourant == -1)
								return (l.getSource()+" introuvable");
							if(l.getLibelle().compareTo("is_a")==0 && l.getType().compareTo("normal")==0){
//								res+=("\n"+listC.get(posCourant).getNom()+" added haut at etape"+etape);
								successeurAMarquerHaut.add(listC.get(posCourant));
							}
						}
				}
			}
			for(int i = 0 ; i< tailleHaut;i++)
				successeurAMarquerHaut.remove(0);
			
			
			tailleBas = successeurAMarquerBas.size();
			for(int i=0; i<tailleBas;i++){
				if(!successeurAMarquerBas.get(i).getMarquage().isMarked()){
					successeurAMarquerBas.get(i).setMarquage(new Marquage(true,"M2")); 
					res+= ("\n"+successeurAMarquerBas.get(i).getNom()+" M2("+etape+")");
					
					g.getNode(successeurAMarquerBas.get(i).getNom()).addAttribute("ui.class", "bot");

					
					if(successeurAMarquerBas.get(i).getLiens()!=null)
						for (Lien l : successeurAMarquerBas.get(i).getLiens()) {
							posCourant = getPosInList(l.getSource());
							if(posCourant == -1)
								return (l.getSource()+" introuvable");
							if(l.getLibelle().compareTo("is_a")==0 && l.getType().compareTo("normal")==0){
								successeurAMarquerBas.add(listC.get(posCourant));
//								res+=("\n"+listC.get(posCourant).getNom()+" added bas at etape"+etape);
	
							}
						}
				}
			}	
			for(int i = 0 ; i< tailleBas;i++)
				successeurAMarquerBas.remove(0);
			

			for(int i = 0; i<listC.size();i++){
//				System.out.println("Dans:: "+listC.get(i).getNom());
				if(listC.get(i).getMarquage().isMarked()){
					if(listC.get(i).getLiens()!=null)
						for(Lien l : listC.get(i).getLiens()){
								posCourant = getPosInList(l.getSource());
								if(posCourant!=-1)
									if(l.getLibelle().compareTo(lien)==0 && listC.get(posCourant).getMarquage().isMarked() && !listC.get(posCourant).getMarquage().MarkedBy(listC.get(i).getMarquage().by())){
										cpt++;
										res += ("\n\tRéponse "+cpt+": "+listC.get(posCourant).getNom()+" "+l.getLibelle()+" "+listC.get(i).getNom()+" ");
									}
									
							}
								
					}
			}
			
			etape++;
		}
		
		
		
		
		return res;
	}
	
	public int getPosInList(String nom){
		boolean trouvee=false;
		int position = 0;
		while(position<listC.size() && !trouvee){
			if(listC.get(position).getNom().compareTo(nom)==0)
				trouvee=true;
			
			position++;
		}
		
		if (trouvee)
			return (position-1);
		else
			return -1;
	}
	/***************************************************************************************************************************************/
	/***************************************************** RESEAU ETRE VIVANTS *************************************************************/
	/***************************************************************************************************************************************/

	public void reseauEtreVivants(){
		listC = new ArrayList<Classe>();
		
		Classe c = new Classe("Etre vivants");
		ArrayList<Lien> listL = new ArrayList<Lien>();
		Lien l = new Lien("is_a","Vegetaux");
		listL.add(l);
		l = new Lien("is_a","Animaux");
		listL.add(l);
		l = new Lien("is_a","Humains");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Vegetaux");
		listC.add(c);
		
		c = new Classe("Humains");
		listC.add(c);
		
		c = new Classe("Animaux");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","Reptils");
		listL.add(l);
		l = new Lien("is_a","Canins");
		listL.add(l);
		l = new Lien("is_a","Felins");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Reptils");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","serpent");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Canins");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","chiens");
		listL.add(l);
		l = new Lien("is_a","loups");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Felins");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","tigres");
		listL.add(l);
		l = new Lien("is_a","chats");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("serpent");
		listC.add(c);
		
		c = new Classe("chiens");
		listC.add(c);
		
		c = new Classe("loups");
		listL = new ArrayList<Lien>();
		l = new Lien("hait","chats");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("tigres");
		listC.add(c);
		
		c = new Classe("chats");
		listL = new ArrayList<Lien>();
		l = new Lien("hait","loups");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
	}
	
	/***************************************************************************************************************************************/
	/***************************************************** RESEAU VEHICULE *************************************************************/
	/***************************************************************************************************************************************/

	public void reseauVehicule(){
		listC = new ArrayList<Classe>();
		
		Classe c = new Classe("Moyen de locomotion");
		ArrayList<Lien> listL = new ArrayList<Lien>();
		Lien l = new Lien("is_a","Véhicule");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Véhicule");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","Léger");
		listL.add(l);
		l = new Lien("is_a","Lourd");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Léger");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","4x4");
		listL.add(l);
		l = new Lien("is_a","2x4");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Lourd");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","Camion");
		listL.add(l);
		l = new Lien("is_a","Semi-remorque");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("4x4");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","Q5");
		listL.add(l);
		l = new Lien("is_a","Touareg");
		listL.add(l);
		l = new Lien("is_a","Land rover");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("2x4");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","308");
		listL.add(l);
		l = new Lien("is_a","clio");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Camion");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","iveco");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Semi-remorque");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","MAN");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Carburant");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","Poluant");
		listL.add(l);
		l = new Lien("is_a","Non poluant");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Poluant");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","Essance");
		listL.add(l);
		l = new Lien("is_a","Diesel");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Non poluant");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","Bio");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Essance");
		listL = new ArrayList<Lien>();
		l = new Lien("consomme","Touareg");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Diesel");
		listL = new ArrayList<Lien>();
		l = new Lien("consomme","Q5");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Bio");
		listL = new ArrayList<Lien>();
		l = new Lien("consomme","Land rover");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Q5");
		listC.add(c);
		c = new Classe("Touareg");
		listC.add(c);
		c = new Classe("Land rover");
		listC.add(c);
		c = new Classe("308");
		listC.add(c);
		c = new Classe("clio");
		listC.add(c);
		c = new Classe("iveco");
		listC.add(c);
		c = new Classe("MAN");
		listC.add(c);
	}
	
	

	/***************************************************************************************************************************************/
	/***************************************************** RESEAU ARCHE *************************************************************/
	/***************************************************************************************************************************************/

	
	public void reseauArche(){
		listC = new ArrayList<Classe>();
		
		Classe c = new Classe("ARCHE");
		ArrayList<Lien> listL = new ArrayList<Lien>();
		Lien l = new Lien("is_a","Timgad");
		listL.add(l);
		l = new Lien("is_a","Bab el Gherb");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("Timgad");
		listC.add(c);
		
		c = new Classe("Bab el Gherb");
		listC.add(c);

		c = new Classe("A");
		listL = new ArrayList<Lien>();
		l = new Lien("Horizontal","A");
		listL.add(l);
		l = new Lien("ED","C");
		listL.add(l);
		l = new Lien("EG","B");
		listL.add(l);
		l = new Lien("composé","Bab el Gherb");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("B");
		listL = new ArrayList<Lien>();
		l = new Lien("composé","Bab el Gherb");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);

		c = new Classe("C");
		listL = new ArrayList<Lien>();
		l = new Lien("composé","Bab el Gherb");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("BLOC");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","A");
		listL.add(l);
		l = new Lien("is_a","B");
		listL.add(l);
		l = new Lien("is_a","C");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
				
		c = new Classe("vertical");
		listL = new ArrayList<Lien>();
		l = new Lien("position","B");
		listL.add(l);
		l = new Lien("position","C");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);

	}
	
	/***************************************************************************************************************************************/
	/***************************************************** RESEAU CEPHALOPODE *************************************************************/
	/***************************************************************************************************************************************/

	
	public void reseauCephalopodes(){
		listC = new ArrayList<Classe>();
		
		Classe c = new Classe("Animal a coquille");
		ArrayList<Lien> listL = new ArrayList<Lien>();
		Lien l = new Lien("is_a","mollusques","sorteDeNonStrict");
		listL.add(l);
		l = new Lien("is_a","cephalopode","sorteDeNonStrict");
		listL.add(l);
		l = new Lien("is_a","nautile","sorteDeStrict");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("mollusques");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","cephalopode","nonSorteDeNonStrict");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);
		
		c = new Classe("cephalopode");
		listL = new ArrayList<Lien>();
		l = new Lien("is_a","nautile","sorteDeStrict");
		listL.add(l);
		c.setLiens(listL);
		listC.add(c);

		c = new Classe("nautile");
		listC.add(c);
		
	}

	
	
}
