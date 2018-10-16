
import java.io.IOException;

import matrices.MatriceElement;
import meta.BSO;
import meta.RechercheLocale;
import meta.RechercheTaboue;


public class Main {

	public static void main(String[] args) throws IOException {
		MatriceElement mat = new MatriceElement();

		String benchmark = "v5029c19934.cnf";
		String typeFlip = "continue";
		int pop_size= 153;
		int maxIter = 250;
		int maxIterLocale = 175;
		int seuilDeStagnation = 50;
		
		mat.lectureFichier("D:\\9raya\\M1 SII\\S2\\Méta-heuristiques\\TP\\Projet\\part 2\\Benchmarks\\"+benchmark);

//		RechercheLocale rl = new RechercheLocale(mat);
//		rl.appliquerRechercheLocale(75);
		
//		RechercheTaboue rtb = new RechercheTaboue(mat);
//		rtb.appliquerRechercheTaboue(75,30);
		
		BSO bso = new BSO(mat);
		

		
//		bso.appliquerBSO(pop_size,typeFlip, maxIter, maxIterLocale, seuilDeStagnation);
		
		
		String nomFichier = "D:\\9raya\\M1 SII\\S2\\Méta-heuristiques\\TP\\Projet\\part 2\\tests\\BSO "+benchmark+" popSize"+pop_size+" maxIter"+maxIter+" maxIterLocale"+maxIterLocale+" Seuil"+seuilDeStagnation+".txt";
		bso.appliquerBSOFile(nomFichier,pop_size,typeFlip, maxIter, maxIterLocale, seuilDeStagnation);

		
	}

}
