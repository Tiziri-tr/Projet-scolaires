package composants;

public class Lien {
	private String libelle;
	private String source;
	private String type;	
	
	public String getDessin() {
		if(type.compareTo("sorteDeNonStrict")==0)
			return "<|—";
		else if (type.compareTo("sorteDeStrict")==0)
			return "◄—";
		else if (type.compareTo("exception")==0)
			return "◄--";
		else if (type.compareTo("nonSorteDeStrict")==0)
			return "◄-X-";
		else if (type.compareTo("nonSorteDeNonStrict")==0)
			return "<|-X-"; //◁
		else
			return "<—";
	}

	public Lien(String libelle) {
		super();
		this.libelle = libelle.toLowerCase();
		this.type = "normal";
	}

	public Lien(String libelle, String source) {
		super();
		this.libelle = libelle.toLowerCase();
		this.source = source.toUpperCase(); 
		this.type = "normal";
	}
	
	public Lien(String libelle, String source, String type) {
		super();
		this.libelle = libelle.toLowerCase();
		this.source = source.toUpperCase();
		this.type = type;
	}



	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source.toUpperCase();
	}
	
	public String getLibelle() {
		return libelle;
	}
	
	public void setLibelle(String libelle) {
		this.libelle = libelle.toLowerCase();
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
