
import rs.ReseauSementique;


public class Main {

	public static void main(String[] args) {
		ReseauSementique rs = new ReseauSementique();
//		rs.reseauArche();
//		rs.reseauVehicule();
		rs.reseauEtreVivants();
//		rs.reseauCephalopodes();
		rs.construireGraphe();
		
		
//		System.out.println(rs.afficherReseau());
		
		rs.ecrire("C:\\Users\\Tiziri\\Desktop\\affichage réseau.txt",rs.afficherReseau());
		
//		System.out.println(rs.propagation("ARCHE", "composé", "BLOC")); // quelles sont les arches qui sont composés de blocs
//		rs.ecrire("C:\\Users\\Tiziri\\Desktop\\Etapes propagation.txt",rs.propagation("ARCHE", "composé", "BLOC")); 
		
//		System.out.println(rs.propagation("BLOC", "position", "vertical")); // quelles sont les blocs donc la position est vertical
//		rs.ecrire("C:\\Users\\Tiziri\\Desktop\\Etapes propagation.txt",rs.propagation("BLOC", "position", "vertical"));
		
//		System.out.println(rs.propagation("Véhicule", "consomme", "Poluant")); // quelles sont les véhicules qui consomment du carburant poluant
//		rs.ecrire("C:\\Users\\Tiziri\\Desktop\\Etapes propagation.txt",rs.propagation("Véhicule", "consomme", "Poluant"));

		System.out.println(rs.propagation("Animaux", "hait", "chats")); // quelles sont les animaux qui haissent les chats
//		rs.ecrire("C:\\Users\\Tiziri\\Desktop\\Etapes propagation.txt",rs.propagation("Animaux", "hait", "chats"));
	}

}
