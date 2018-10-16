package meta;

import java.util.Random;

import matrices.MatriceElement;

public class RechercheTaboue{
	private byte[] solutionCourrante;
	private byte[] meilleureSolution;
	private int nbVar;
	private int nbClauses;
	private MatriceElement mat;
	private Deplacement[] listeTaboue;
	private int tailleLT;
	
	public RechercheTaboue(MatriceElement mat) {
		this.nbVar = mat.getNbLignes();
		this.nbClauses = mat.getNbClauses();
		this.mat = mat;
		listeTaboue = new Deplacement[nbVar];
		tailleLT = nbVar;
		for(int i = 0 ; i<nbVar;i++)
			listeTaboue[i] = new Deplacement();
		this.solutionCourrante = genererSolution();
		
	}
	
	


	public int getTailleLT() {
		return tailleLT;
	}

	public void setTailleLT(int tailleLT) {
		this.tailleLT = tailleLT;
	}

	public Deplacement[] getListeTaboue() {
		return listeTaboue;
	}

	public void setListeTaboue(Deplacement[] listeTaboue) {
		this.listeTaboue = listeTaboue;
	}

	public byte[] getSolutionCourrante() {
		return solutionCourrante;
	}

	public void setSolutionCourrante(byte[] solutionCourrante) {
		this.solutionCourrante = solutionCourrante;
	}

	public int getNbVar() {
		return nbVar;
	}

	public void setNbVar(int nbVar) {
		this.nbVar = nbVar;
	}

	public int getNbClauses() {
		return nbClauses;
	}

	public void setNbClauses(int nbClauses) {
		this.nbClauses = nbClauses;
	}

	public MatriceElement getMat() {
		return mat;
	}

	public void setMat(MatriceElement mat) {
		this.mat = mat;
	}

	public byte[] genererSolution(){
		byte[] tab = new byte[nbVar];
		Random r = new Random();
		byte b ;
		
		for(int i = 0 ; i<nbVar;i++){
			b = (byte)r.nextInt(2);
			tab[i] = b;
			if(b==0)
				listeTaboue[i].setDeplacementZero(true);
			else
				listeTaboue[i].setDeplacementUn(true);
		}
		return tab;
	}
	
	public boolean voisingeParPoint(){
		double vSolCourante = mat.evaluer(solutionCourrante);
		byte[]  voisin = new byte[nbVar];
		byte[]  voisinAspiration = new byte[nbVar];
		int i =0;
		boolean amelioration = false;
		boolean deplacementAutorise;
		
		while ( i< solutionCourrante.length && !amelioration){
			deplacementAutorise = true;
			voisin = solutionCourrante.clone();
			vSolCourante = mat.evaluer(solutionCourrante);
			
			if( solutionCourrante[i]== 0)
				if(listeTaboue[i].getDeplacementUn()){
					/***** critère d'aspiration ******/
					voisinAspiration=solutionCourrante.clone();
					voisinAspiration[i]=1;
					if(mat.evaluer(voisinAspiration)>vSolCourante){
						solutionCourrante = voisinAspiration.clone();
						vSolCourante = mat.evaluer(voisinAspiration);
//						listeTaboue[i].setDeplacementUn(true);
					}
					else{
						deplacementAutorise=false;
					}		
					/********************************/

				}
					
				else{
					voisin[i]=1;
					listeTaboue[i].setDeplacementUn(true);
					tailleLT++;
				}
									
			else 
				if(listeTaboue[i].getDeplacementZero()){
					/***** critère d'aspiration ******/
					voisinAspiration=solutionCourrante.clone();
					voisinAspiration[i]=0;
					if(mat.evaluer(voisinAspiration)>vSolCourante){
						solutionCourrante = voisinAspiration.clone();
						vSolCourante = mat.evaluer(voisinAspiration);
//						listeTaboue[i].setDeplacementZero(true);
					}
					else{
						deplacementAutorise=false;
					}	
					/*******************************/

				}
				else{
					voisin[i]=0;
					listeTaboue[i].setDeplacementZero(true);
					tailleLT++;
				}
					
			
			if(deplacementAutorise)
				if ( mat.evaluer(voisin) > vSolCourante){
					solutionCourrante = voisin.clone();
					vSolCourante = mat.evaluer(voisin); 
					amelioration = true;
				}
			i++;
		}
		
		return amelioration;
	}
	
	public int majLT() {
		int cptTrue=0;
		
		for(int i = 0 ; i < listeTaboue.length;i++)  {
			if(listeTaboue[i].getDeplacementUn()){
				cptTrue++;
				listeTaboue[i].setTenureUn(listeTaboue[i].getTenureUn()+1);
				if(listeTaboue[i].getTenureUn()>=tailleLT){
					listeTaboue[i].setDeplacementUn(false);
					listeTaboue[i].setTenureUn(0);
				}
			}
			if(listeTaboue[i].getDeplacementZero()){
				cptTrue++;
				listeTaboue[i].setTenureZero(listeTaboue[i].getTenureZero()+1);
				if(listeTaboue[i].getTenureZero()>=tailleLT){
					listeTaboue[i].setDeplacementZero(false);
					listeTaboue[i].setTenureZero(0);
				}
			}
		}
		
		return cptTrue;
	}
	
	public void appliquerRechercheTaboue(int maxIter,int seuilDeStagnation){
		
		int nbritr= 1; 
		boolean amelioration= true; 
		int stagnation = 0;
		meilleureSolution = solutionCourrante.clone();
		boolean resolut = false;

		
		while ( nbritr <= maxIter && !resolut/*&& amelioration */){
			
			tailleLT = majLT(); // tenure
			amelioration=voisingeParPoint();
			if(amelioration){
				if(mat.evaluer(solutionCourrante)>mat.evaluer(meilleureSolution))
					meilleureSolution = solutionCourrante.clone();
				
				System.out.println("\n******************* Itération "+nbritr+" **********************\n\nSolution courante : ");
				for (byte b : solutionCourrante) {
					System.out.print("| "+b);
				}
				System.out.println("|\n\nQualité de la solution: "+(mat.evaluer(solutionCourrante)*100)+"%");
				if(mat.evaluer(solutionCourrante)==1)
					resolut=true;
				System.out.println("Taille liste taboue: "+tailleLT);
				stagnation=0;
			}
			else{
				stagnation++;
				System.out.print(".");
				if(stagnation >=seuilDeStagnation){
					System.out.println("stagnation == seuil ");
					stagnation=0;
					meilleureSolution = solutionCourrante.clone();
					solutionCourrante = genererSolution();
					nbritr=1;
					System.out.println("\n\nProcessus réinitialisé...");
				}
			}
			
			nbritr++; 
		}
		
		
		System.out.println("\n\n\nQualité de la meilleure solution trouvée: "+(mat.evaluer(meilleureSolution)*100)+"%");
		
	}
	
}
