package composants;

import java.util.ArrayList;

public class Classe {
	private String nom;
	private ArrayList<Lien> liens;
	private Marquage marquage;
	
	public Classe(String nom) {
		super();
		this.nom = nom.toUpperCase();
		marquage = new Marquage(false);
	}
	
	public String getNom() {
		return nom;
	}

	public Marquage getMarquage() {
		return marquage;
	}

	public void setMarquage(Marquage marquage) {
		this.marquage = marquage;
	}



	public void setNom(String nom) {
		this.nom = nom.toUpperCase();
	}

	public ArrayList<Lien> getLiens() {
		return liens;
	}

	public void setLiens(ArrayList<Lien> liens) {
		this.liens = liens;
	}

	
}
