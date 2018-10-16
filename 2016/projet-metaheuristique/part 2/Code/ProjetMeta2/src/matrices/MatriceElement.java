package matrices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


public class MatriceElement {
	private int nbLignes;
	private int nbClauses;
	private Element[][] indexVar;

	
	public MatriceElement(){
		
	}
	
	
	public double evaluer(byte[] tab){
		double val=0; 
		byte[] tabClauses = new byte[nbClauses];
		for(int i=0;i<tabClauses.length;i++)
			tabClauses[i] = 1;
		
		for(int i = 0 ; i<nbLignes;i++)
			for(int j=0;j<indexVar[i].length;j++)
				if(tab[i]==0 && indexVar[i][j].getValeur()==0)
					tabClauses[indexVar[i][j].getSigne()-1]=0;
				else
					if(tab[i]==1 && indexVar[i][j].getValeur()==1)
						tabClauses[indexVar[i][j].getSigne()-1]=0;
			
		for (byte b : tabClauses)
			if(b==0)
				val++;
		
		
//		System.out.println(val);
		
		val/=nbClauses;
		
		return val; 
	}
	
	
	//************************* tester Si Resolut ******************************* //

	public double tauxSatisfaction(byte[] tab){
		double taux=0;
		
		
		
		return taux;
	}
	//*************************************************************************** //

	public int getNbLignes() {
		return nbLignes;
	}

	public void setNbLignes(int nbLignes) {
		this.nbLignes = nbLignes;
	}

	public Element[][] getIndexVar() {
		return indexVar;
	}

	public void setIndexVar(Element[][] indexVar) {
		this.indexVar = indexVar;
	}
	
	public int getNbClauses() {
		return nbClauses;
	}

	public void setNbClauses(int nbClauses) {
		this.nbClauses = nbClauses;
	}	
	
	public void lectureFichier(String fichier){
		String[] tab;
		ArrayList<String> tabMat = new ArrayList<String>();
		int nbZero=0;
		try{
			InputStream ips=new FileInputStream(fichier); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			ligne = br.readLine();
			tab = ligne.split(" ");
			nbLignes = Integer.parseInt(tab[2]); 
			nbClauses= Integer.parseInt(tab[3]);
			indexVar = new Element[nbLignes][];

			while ((ligne=br.readLine())!=null){
				tab = ligne.split(" ");
				for(int j = 0; j< tab.length;j++)
					tabMat.add(tab[j]);
				
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		for(int i = 0; i < nbLignes ; i++){
			nbZero = 0;
			for(int j = 0 ; j < tabMat.size()-1 ; j++){
				if(tabMat.get(j).compareTo(" ")!=0)
					if((i+1) == Math.abs(Integer.parseInt(tabMat.get(j))))
						nbZero ++;
				
			}
			indexVar[i]= new Element[nbZero];
		}

		
		int position = 0;
		
		
		for(int i = 0; i < nbLignes; i ++){
			nbZero= 1 ; position = 0;
			for(int j = 0 ; j < tabMat.size()-1 ; j++){
				if(Integer.parseInt(tabMat.get(j))==0)
					nbZero++;
				if((i+1)==Math.abs(Integer.parseInt(tabMat.get(j)))){
					if(Integer.parseInt(tabMat.get(j)) < 0){
						indexVar[i][position]= new Element( nbZero,1);
						position++;
					}
					else{
						indexVar[i][position]= new Element( nbZero,0);
						position++;
					}
				}
				
			}
		}
				
		
	}
	

	
	public String toString(){
		String chaine = "";
		for (int i=0;i<nbLignes;i++){
		chaine+="X"+(i+1)+": ";
			for(int j=0;j<indexVar[i].length;j++){
				chaine+=" | C"+indexVar[i][j].getSigne()+""+(indexVar[i][j].getValeur()==0?"+":"-");
			}
		chaine+=" |\n";
		}
		return chaine;
	}
	
	
	
	public void EcrireMatrice(String chemin){

		try {
			FileWriter fw = new FileWriter(chemin);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter fichierSortie = new PrintWriter (bw); 
			fichierSortie.println(this.toString());
			fichierSortie.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public void afficher(){
		for (int i=0;i<nbLignes;i++){
		System.out.print("X"+(i+1)+": ");
			for(int j=0;j<indexVar[i].length;j++){
				System.out.print(" | C"+indexVar[i][j].getSigne()+""+(indexVar[i][j].getValeur()==0?"+":"-"));
			}
		System.out.println(" |");
		}
	}
	
	
}
