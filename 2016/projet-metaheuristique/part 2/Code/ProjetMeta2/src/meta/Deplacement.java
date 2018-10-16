package meta;

public class Deplacement {
	private boolean deplacementZero;
	private int tenureZero;
	private boolean deplacementUn;
	private int tenureUn;
	
	public Deplacement() {
		super();
		deplacementZero = false;
		deplacementUn = false;
		tenureUn = 0;
		tenureZero = 0;
	}

	public int getTenureZero() {
		return tenureZero;
	}

	public void setTenureZero(int tenureZero) {
		this.tenureZero = tenureZero;
	}

	public int getTenureUn() {
		return tenureUn;
	}

	public void setTenureUn(int tenureUn) {
		this.tenureUn = tenureUn;
	}

	public boolean getDeplacementZero() {
		return deplacementZero;
	}

	public void setDeplacementZero(boolean deplacementZero) {
		this.deplacementZero = deplacementZero;
	}

	public boolean getDeplacementUn() {
		return deplacementUn;
	}

	public void setDeplacementUn(boolean deplacementUn) {
		this.deplacementUn = deplacementUn;
	}
	
	
	
}
