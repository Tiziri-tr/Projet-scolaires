package meta;

import java.util.Random;

import matrices.MatriceElement;

public class RechercheLocale {
	private byte[] solutionCourrante;
	private int nbVar;
	private int nbClauses;
	public MatriceElement mat;
	
	/*
	-soliution initiale: dans lespace de recherche 2^N
	-fonction objective : taux de satisfaction	satisfait/total	<<<------- meilleure solution 100% 
						  nombres de clauses satisfaites
	-exploration du voisinage : point etoile anneau // utiliser point
	-choix du meilleur voisin par rapport a la statégie d'exploration
	-condition d'arret : optimum lolcal(fct<100% mais autre condition darret atteinte), temps, nombre ditération,solution trouvé

	 */
	
	public double evaluerSolutionCourante(){
		double val=0; 
		byte[] tabClauses = new byte[mat.getNbClauses()];
		for(int i=0;i<tabClauses.length;i++)
			tabClauses[i] = 1;
		
		for(int i = 0 ; i<solutionCourrante.length;i++)
			for(int j=0;j<mat.getIndexVar()[i].length;j++)
				if(solutionCourrante[i]==0 && mat.getIndexVar()[i][j].getValeur()==0)
					tabClauses[mat.getIndexVar()[i][j].getSigne()-1]=0;
				else
					if(solutionCourrante[i]==1 && mat.getIndexVar()[i][j].getValeur()==1)
						tabClauses[mat.getIndexVar()[i][j].getSigne()-1]=0;
			
		for (byte b : tabClauses)
			if(b==0)
				val++;
		val/=mat.getNbClauses();
		
		return val; 
	}
	
	public double evaluer(byte[] tab){
		double val=0; 
		byte[] tabClauses = new byte[nbClauses];
		for(int i=0;i<tabClauses.length;i++)
			tabClauses[i] = 1;
		
		for(int i = 0 ; i<nbVar;i++)
			for(int j=0;j<mat.getIndexVar()[i].length;j++)
				if(tab[i]==0 && mat.getIndexVar()[i][j].getValeur()==0)
					tabClauses[mat.getIndexVar()[i][j].getSigne()-1]=0;
				else
					if(tab[i]==1 && mat.getIndexVar()[i][j].getValeur()==1)
						tabClauses[mat.getIndexVar()[i][j].getSigne()-1]=0;
			
		for (byte b : tabClauses)
			if(b==0)
				val++;
		val/=mat.getNbClauses();
		
		return val; 
	}
	
	public boolean voisingeParPoint(){
		double vSolCourante = evaluerSolutionCourante();
		byte[]  voisin;
		int i =0;
		boolean bool = false;
		while ( i< solutionCourrante.length && !bool){
			voisin = solutionCourrante.clone();
			if( solutionCourrante[i]== 0) 
				voisin[i]=1;
			else 
				voisin[i]=0;
			
			if ( mat.evaluer(voisin) > vSolCourante){
				solutionCourrante = voisin.clone();
				vSolCourante = mat.evaluer(voisin); 
				bool = true;
			}
			i++;
		}
		
		return bool;
	}
	
	public void appliquerRechercheLocale(int maxIter){
		
		int nbritr= 1; 
		boolean bool= true; 
		boolean resolut = false;
		
		while ( (nbritr <= maxIter && bool) && !resolut ){
			
			bool=voisingeParPoint();
			System.out.println("Itération "+nbritr+"\nSolution courante : ");
			for (byte b : solutionCourrante) {
				System.out.print("| "+b);
			}
			System.out.println("|\nQualité de la solution: "+(evaluerSolutionCourante()*100)+"%");
			if(evaluerSolutionCourante()==1)
				resolut=true;
			nbritr++; 
		}
		
	}
	
	public void rechercheLocale(int maxIter){
			
			int nbritr= 1; 
			boolean bool= true; 
			boolean resolut = false;
			
			while ( (nbritr <= maxIter && bool) && !resolut ){
				
				bool=voisingeParPoint();
				if(evaluerSolutionCourante()==1)
					resolut=true;
				nbritr++; 
			}
			
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

	public RechercheLocale(MatriceElement mat) {
		super();
		this.mat = mat;
		nbVar = mat.getNbLignes();
		solutionCourrante = genererSolution();
	}

	public byte[] genererSolution(){
		byte[] tab = new byte[nbVar];
		//random

		Random r = new Random();
		for(int i = 0 ; i<nbVar;i++){
			tab[i] = (byte)r.nextInt(2);
		}
		return tab;
	}
	
	
	

}
