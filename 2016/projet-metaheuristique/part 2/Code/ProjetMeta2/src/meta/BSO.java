package meta;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import matrices.MatriceElement;

public class BSO {
	private byte[] Sref;
	private Dance danceCourant;
	private byte[] meilleureSource;
	private Dance danceMeilleureSource;
	private MatriceElement mat;
	private Dance[] tabDance;
	private ArrayList<byte[]> listTaboue;


	
	/******************************************************************************************************************
	  1- population initiale :  paramètre empirique ici nombre d'abeille (population initiale) 
	  							et type de flip 
	  							nb abeilles doit etre bcp plus petit que la taille de la solution
	  							- générer une solution aléatoire puis effectuer les flips
	  						    - générer aléatoirement des solution et ne retenir que celle qui 
	  						    ont une distance >= flip
	  						    
	  2- recherche d'une abeille : -> recherche locale : paramètre empirique searchIter (nombre
	  								d'itération max de la recherche locale)+
	  								- chaque abeille fournie son meilleure voisin et le met dans la
	  								table danse
	  3- table danse : soit : 
	  						- ne garder que la meilleure solution/abeille a chaque itération (celle qu'on 
	  						va utiliser) -> tableau de taille nombre d'abeille
	  						- garder trace de toutes les itération (matrice), a utiliser si on doit trouver
	  						plusieurs solution ex : quels sont les 10 meilleurs ...
	  						
	  4- itération i :  - choisir le nouveau Sref a partir de la table dance et mettre l'ancien dans LT
	  					- calculer DELTAf : - >  : choisir meilleure qualité
	  										- <= : voir nbChance : - >0 : meilleure qualité
	  															   - =0 : choisir meilleure diversité
	  															   (la meilleure diversité = choisir la meilleure
	  															   dans la table dance et maximiser la distance
	  															   avec les sommets déjà visité <=> qui sont dans LT 
	  															   si pas de solution assez éloignée générer une nouvelle 
	  															   solution aléatoire assez éloignée )
	  										
	 
	  
	 ********************************************************************************************************************/
	
	
	public BSO(MatriceElement m) {
		mat = m;
		listTaboue = new ArrayList<byte[]>();

	}

	public byte[][] beeInit(int pop_size,String flip){
		int nbVar = mat.getNbLignes();
		Sref = new byte[nbVar];
		Random r = new Random();
		byte[][] nouveauxChamps = new byte[pop_size][nbVar];
		
		/***** Génération solution aléatoire ******/
			for(int i = 0 ; i<nbVar;i++){
				Sref[i] = (byte)r.nextInt(2);
			}
		/******************************************/
		listTaboue.add(Sref);
		danceCourant = new Dance(mat.evaluer(Sref),0);
		
		meilleureSource = Sref.clone();
		danceMeilleureSource = new Dance(danceCourant.getQualite(),0);

		
		nouveauxChamps=genererNouveauxChamps(pop_size,Sref,flip);
		
		return nouveauxChamps;
	}
	
	public byte[][] genererNouveauxChamps(int pop_size, byte[] Sref, String flip){
		byte[][] nouveauxChamps = new byte[pop_size][mat.getNbLignes()];
		Random r = new Random();
		if(flip.compareTo("periodique")==0){
				for(int i = 0 ; i < pop_size ; i ++){
					nouveauxChamps[i]= Sref.clone(); 
					for (int j = i; j < Sref.length; j++) 
						if( (j-i) % pop_size == 0 )
							if(Sref[j] ==0)
								nouveauxChamps[i][j] = 1;
							else
								nouveauxChamps[i][j] = 0;
				}
		}
		else
			if(flip.compareTo("continue")==0){
				for(int i = 0 ; i < pop_size ; i ++){
					nouveauxChamps[i] = Sref.clone();
					for (int j = i; j < pop_size+i; j++) {
						if(Sref[j] ==0)
							nouveauxChamps[i][j] = 1;
						else
							nouveauxChamps[i][j] = 0;
					}
				}
			}
		else 
			if(flip.compareTo("aleatoire")==0){
				int caseAChanger;
				
				for(int i = 0 ; i < pop_size ; i ++){
					nouveauxChamps[i] = Sref.clone();
					for (int j = 0; j < pop_size; j++) {
						caseAChanger = r.nextInt(Sref.length);
						if(Sref[caseAChanger] ==0)
							nouveauxChamps[i][caseAChanger] = 1;
						else
							nouveauxChamps[i][caseAChanger] = 0;
					}
				}
			}
			else
				System.out.println("Type de flip incorrect veuillez taper : periodique, continue ou aleatoire");
		
		return nouveauxChamps;
	}
	
	
	public void appliquerBSO(int pop_size, String flip, int maxIter, int maxIterLocale, int seuilDeStagnation){
		byte[][] nouveauxChamps =beeInit(pop_size,flip);
		int stagnation =seuilDeStagnation;
		byte[] meilleureSommetQualite ;
		int positionMeilleurQualite;
		RechercheLocale bee = new RechercheLocale(mat);
		double qualite;
		double distance;
		tabDance = new Dance[pop_size];
		for (int i = 0; i < tabDance.length; i++) {
			tabDance[i]= new Dance();
		}
		int nbIter=0;
		
		double debut = System.nanoTime();
		while(nbIter < maxIter){
			System.out.println("******************************** Itération "+(nbIter+1)+" *****************************************");
			System.out.println("Dance meilleure source  : "+danceMeilleureSource.getQualite()+" , "+danceMeilleureSource.getDistance());
			System.out.println("Dance Sref  : "+danceCourant.getQualite()+" , "+danceCourant.getDistance());

			/* générer les nouveaux sommets et remplacer si necessaire dans la table dance */
//			System.out.println("taille nouveauxChamps: "+nouveauxChamps.length);
			System.out.println("[");
				for(int i=0;i<pop_size;i++){
					bee.setSolutionCourrante(nouveauxChamps[i]);
					bee.rechercheLocale(maxIterLocale);
					qualite = mat.evaluer(bee.getSolutionCourrante());
					distance = distance(Sref, nouveauxChamps[i]);
					if(qualite>tabDance[i].getQualite() || (qualite==tabDance[i].getQualite() && distance<tabDance[i].getDistance()))
						tabDance[i] = new Dance(qualite,distance);
					
					nouveauxChamps[i] = bee.getSolutionCourrante().clone();
					System.out.println("\tS"+(i+1)+" Bee"+(i+1)+" "+tabDance[i].getQualite()+" , "+tabDance[i].getDistance()+"\n\t "+afficherTab(nouveauxChamps[i]));
					
				}
				
				
			/* choisir le meilleure nouveau Sref */
				positionMeilleurQualite = positionMeilleureQualite(tabDance);
				meilleureSommetQualite = nouveauxChamps[positionMeilleurQualite];

				System.out.println("Delta = "+delta(tabDance[positionMeilleurQualite].getQualite()));


				
				if( delta(tabDance[positionMeilleurQualite].getQualite())>0 ){
					if(!listTaboue.contains(meilleureSommetQualite)){
						System.out.print("\nMeilleure qualitée: S"+(positionMeilleurQualite+1));
						System.out.println(" sa qualité = "+tabDance[positionMeilleurQualite].getQualite());

						System.arraycopy(meilleureSommetQualite, 0, Sref, 0, meilleureSommetQualite.length);
						danceCourant.setQualite(tabDance[positionMeilleurQualite].getQualite());
						danceCourant.setDistance(0);
						
						System.arraycopy(Sref, 0, meilleureSource, 0, Sref.length);
						danceMeilleureSource.setQualite(tabDance[positionMeilleurQualite].getQualite()); 
						danceMeilleureSource.setDistance(0);
							
						System.out.println("S"+(positionMeilleurQualite+1)+" non taboue et meilleure en qualité");
						System.out.println("Dance meilleure source : "+danceMeilleureSource.getQualite()+" , "+danceMeilleureSource.getDistance());

						stagnation = seuilDeStagnation;
					}
					else{
						System.out.println("S"+(positionMeilleurQualite+1)+" taboue");
					}
				}
				else{
					stagnation--;
					System.out.println("Stagnations autorisées restantes "+stagnation);

						if(stagnation==0) // prendre le plus distant
							if(!listTaboue.contains(nouveauxChamps[positionMeilleureDistance(tabDance)])){
								System.out.print("\nSeuil de stagnation atteint : diversification :");
								System.out.println("\n\t\t\t\tsommet le plus distant : S"+(positionMeilleureDistance(tabDance)+1));
								
								
								
								if(danceMeilleureSource.getQualite()<danceCourant.getQualite()){
									meilleureSource = Sref.clone();
									danceMeilleureSource.setQualite(danceCourant.getQualite()); 
									danceMeilleureSource.setDistance(0);
								}
								
								
								Sref = nouveauxChamps[positionMeilleureDistance(tabDance)].clone();
								danceCourant.setDistance(0);
								danceCourant.setQualite(tabDance[positionMeilleureDistance(tabDance)].getQualite());
								
								stagnation = seuilDeStagnation;
						}
						else
							if(!listTaboue.contains(meilleureSommetQualite)){
								Sref = meilleureSommetQualite.clone();
								danceCourant.setQualite(tabDance[positionMeilleurQualite].getQualite());
								danceCourant.setDistance(0);
								
						}
	
			}	
				System.out.println("]\n");
				nouveauxChamps = genererNouveauxChamps(pop_size, Sref, flip);
				listTaboue.add(Sref);
				nbIter++;
		}
		double fin = System.nanoTime();
			
		System.out.print("\n\nMeilleur sommet trouvé : ");
		System.out.println("Q: "+danceMeilleureSource.getQualite()+" D: "+danceMeilleureSource.getDistance());
		System.out.println(""+afficherTab(meilleureSource));
		System.out.println("\nTemps d'execution : "+(fin-debut)+" ns");	
	}
	
	public double delta(double qualiteSommet){ // differance de qualité
		return (qualiteSommet-danceMeilleureSource.getQualite());
	}
	
	public int positionMeilleureDistance(Dance[] tab){
		double meilleur = tab[0].getDistance();
		int posMeilleur=0;
		for(int i = 1 ; i < tab.length ; i++)
			if(tab[i].getDistance()<meilleur){
				meilleur = tab[i].getDistance();
				posMeilleur = i;
			}
		
		return posMeilleur;
	}
	
	public int positionMeilleureQualite(Dance[] tab){
		double meilleur = tab[0].getQualite();
		int posMeilleur=0;
		for(int i = 1 ; i < tab.length ; i++)
			if(tab[i].getQualite()>meilleur){
				meilleur = tab[i].getQualite();
				posMeilleur = i;
			}
		
		return posMeilleur;
	}
	
	public double distance(byte[]b1,byte[] b2){
		double res = 0;
		
		for(int i = 0;i<b1.length;i++)
			if(b1[i]!=b2[i])
				res++;
		res /=b1.length;
		
		return res;
	}
	
	public String afficherTab(byte[] tab){
		String res = "< "+tab[0];
		for (int i = 1; i < tab.length; i++) {
			res += " , "+tab[i];
		}
		return res+" >";
	}
	
	/**********************************************************************************************************************************************************************/
	/************************************************************* BSO AVEC TRACE DANS UN FICHIER *************************************************************************/
	/**********************************************************************************************************************************************************************/

	public void appliquerBSOFile(String nomFichier, int pop_size, String flip, int maxIter, int maxIterLocale, int seuilDeStagnation) throws IOException{
		byte[][] nouveauxChamps =beeInit(pop_size,flip);
		int stagnation =seuilDeStagnation;
		byte[] meilleureSommetQualite ;
		int positionMeilleurQualite;
		RechercheLocale bee = new RechercheLocale(mat);
		double qualite;
		double distance;
		tabDance = new Dance[pop_size];
		for (int i = 0; i < tabDance.length; i++) {
			tabDance[i]= new Dance();
		}
		int nbIter=0;
		FileWriter fw = new FileWriter( nomFichier );
		BufferedWriter bw = new BufferedWriter (fw);
		PrintWriter fichierSortie = new PrintWriter (bw); 
		
		
		
		
		
		double debut = System.nanoTime();
		while(nbIter < maxIter){
			fichierSortie.write("******************************** Itération "+(nbIter+1)+" *****************************************\n");
			fichierSortie.write("Dance meilleure source  : "+danceMeilleureSource.getQualite()+" , "+danceMeilleureSource.getDistance()+"\n");
			fichierSortie.write("Dance Sref  : "+danceCourant.getQualite()+" , "+danceCourant.getDistance()+"\n");

			/* générer les nouveaux sommets et remplacer si necessaire dans la table dance */
			fichierSortie.write("[\n");
				for(int i=0;i<pop_size;i++){
					bee.setSolutionCourrante(nouveauxChamps[i]);
					bee.rechercheLocale(maxIterLocale);
					qualite = mat.evaluer(bee.getSolutionCourrante());
					distance = distance(Sref, nouveauxChamps[i]);
					if(qualite>tabDance[i].getQualite() || (qualite==tabDance[i].getQualite() && distance<tabDance[i].getDistance()))
						tabDance[i] = new Dance(qualite,distance);
					
					nouveauxChamps[i] = bee.getSolutionCourrante().clone();
					fichierSortie.write("\tS"+(i+1)+" Bee"+(i+1)+" "+tabDance[i].getQualite()+" , "+tabDance[i].getDistance()+"\n\t "+afficherTab(nouveauxChamps[i])+"\n");
					
				}
				
				
			/* choisir le meilleure nouveau Sref */
				positionMeilleurQualite = positionMeilleureQualite(tabDance);
				meilleureSommetQualite = nouveauxChamps[positionMeilleurQualite];

				fichierSortie.write("Delta = "+delta(tabDance[positionMeilleurQualite].getQualite())+"\n");


				
				if( delta(tabDance[positionMeilleurQualite].getQualite())>0 ){
					if(!listTaboue.contains(meilleureSommetQualite)){
						fichierSortie.write("\nMeilleure qualitée: S"+(positionMeilleurQualite+1));
						fichierSortie.write(" sa qualité = "+tabDance[positionMeilleurQualite].getQualite()+"\n");

						System.arraycopy(meilleureSommetQualite, 0, Sref, 0, meilleureSommetQualite.length);
						danceCourant.setQualite(tabDance[positionMeilleurQualite].getQualite());
						danceCourant.setDistance(0);
						
						System.arraycopy(Sref, 0, meilleureSource, 0, Sref.length);
						danceMeilleureSource.setQualite(tabDance[positionMeilleurQualite].getQualite()); 
						danceMeilleureSource.setDistance(0);
							
						fichierSortie.write("S"+(positionMeilleurQualite+1)+" non taboue et meilleure en qualité\n");
						fichierSortie.write("Dance meilleure source : "+danceMeilleureSource.getQualite()+" , "+danceMeilleureSource.getDistance()+"\n");

						stagnation = seuilDeStagnation;
					}
					else{
						fichierSortie.write("S"+(positionMeilleurQualite+1)+" taboue\n");
					}
				}
				else{
					stagnation--;
					fichierSortie.write("Stagnations autorisées restantes "+stagnation+"\n");

						if(stagnation==0) // prendre le plus distant
							if(!listTaboue.contains(nouveauxChamps[positionMeilleureDistance(tabDance)])){
								fichierSortie.write("\nSeuil de stagnation atteint : diversification :");
								fichierSortie.write("\n\t\t\t\tsommet le plus distant : S"+(positionMeilleureDistance(tabDance)+1)+"\n");
								
								
								
								if(danceMeilleureSource.getQualite()<danceCourant.getQualite()){
									meilleureSource = Sref.clone();
									danceMeilleureSource.setQualite(danceCourant.getQualite()); 
									danceMeilleureSource.setDistance(0);
								}
								
								
								Sref = nouveauxChamps[positionMeilleureDistance(tabDance)].clone();
								danceCourant.setDistance(0);
								danceCourant.setQualite(tabDance[positionMeilleureDistance(tabDance)].getQualite());
								
								stagnation = seuilDeStagnation;
						}
						else
							if(!listTaboue.contains(meilleureSommetQualite)){
								Sref = meilleureSommetQualite.clone();
								danceCourant.setQualite(tabDance[positionMeilleurQualite].getQualite());
								danceCourant.setDistance(0);
								
						}
	
			}	
				fichierSortie.write("]\n\n");
				nouveauxChamps = genererNouveauxChamps(pop_size, Sref, flip);
				listTaboue.add(Sref);
				nbIter++;
				System.out.print(".");
		}
		double fin = System.nanoTime();
			
		fichierSortie.write("\n\nMeilleur sommet trouvé : ");
		fichierSortie.write("Q: "+danceMeilleureSource.getQualite()+" D: "+danceMeilleureSource.getDistance()+"\n");
		fichierSortie.write(""+afficherTab(meilleureSource)+"\n");
		fichierSortie.write("\nTemps d'execution : "+(fin-debut)+" ns\n");	
		
		bw.close();
		fichierSortie.close();
		fw.close();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
