package matrices;

public class Element {
	private int signe;
	private int valeur;
//	private boolean lastVariable=false;
	
	public Element(int signe, int valeur/*,boolean lastVariable*/) {
		super();
		this.signe = signe;
		this.valeur = valeur;
//		this.lastVariable=lastVariable;
	}

//	public boolean isLastVariable() {
//		return lastVariable;
//	}
//
//	public void setLastVariable(boolean lastVariable) {
//		this.lastVariable = lastVariable;
//	}

	public int getSigne() {
		return signe;
	}

	public void setSigne(int signe) {
		this.signe = signe;
	}

	public int getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	
	
}
