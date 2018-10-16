package composants;

public class Marquage {
	private boolean marquage;
	private String type;
	
	
	public Marquage(boolean marquage) {
		super();
		this.marquage = marquage;
		this.type = "";
	}
	
	public Marquage(boolean marquage, String type) {
		super();
		this.marquage = marquage;
		this.type = type;
	}
	public boolean isMarked() {
		return marquage;
	}

	public void setMarquage(boolean marquage) {
		this.marquage = marquage;
	}

	public String by() {
		return type;
	}
	
	public boolean MarkedBy(String s ) {
		if(type.compareTo(s)==0)
			return true;
		else return false;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
