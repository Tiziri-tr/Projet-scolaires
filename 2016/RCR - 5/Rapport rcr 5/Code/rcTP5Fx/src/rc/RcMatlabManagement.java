package rc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RcMatlabManagement {
	
	private String nomFichier;
	
	public RcMatlabManagement(String nomFichier){
		this.nomFichier = nomFichier;
	}
	
	public RcMatlabManagement(){
		this.nomFichier = "";
	}
	
	public String getNomFichier() {
		return nomFichier;
	}


	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}

//	public boolean fichierVide(String fichier){
//		
//		
//		return true;
//	}
	
	public String fileToString(String path){
		String res = "";
		InputStream ips;
		String ligne;
		try {
			ips = new FileInputStream(path);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			while((ligne= br.readLine())!=null){
				res += ligne+"\n";
			}
				
			br.close();
		}catch (IOException e) {
			res = e.getMessage();
		}
		return res;
	}
	
	public void modifierNoeudsParents(int nb_nodes,int nb_parent_max ){
		InputStream ips;
		String ligne;
		int i=0;
		String nouvelleChaine="";
		try {
			ips = new FileInputStream("C:/anahla/PNT/"+nomFichier);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			
			while((ligne= br.readLine())!=null){
				i++;
				if (i==11)
					nouvelleChaine+="nb_nodes="+nb_nodes+";\n";
				else if(i==12)
						nouvelleChaine+="nb_parent_max="+nb_parent_max+";\n";
					else
						nouvelleChaine+=ligne+"\n";
				
			}
			br.close();
			
			FileWriter fwi = new FileWriter("C:/anahla/PNT/"+nomFichier);
			BufferedWriter bwi = new BufferedWriter (fwi);
			PrintWriter fichier = new PrintWriter (bwi); 
			fichier.write(nouvelleChaine);
			fichier.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	
	public void modifierParents(int nb_parent_max ){
		InputStream ips;
		String ligne;
		int i=0;
		String nouvelleChaine="";
		try {
			ips = new FileInputStream("C:/anahla/PNT/"+nomFichier);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			
			while((ligne= br.readLine())!=null){
				i++;
				if(i==12)
					nouvelleChaine+="nb_parent_max="+nb_parent_max+";\n";
				else
					nouvelleChaine+=ligne+"\n";
				
			}
			br.close();
			
			FileWriter fwi = new FileWriter("C:/anahla/PNT/"+nomFichier);
			BufferedWriter bwi = new BufferedWriter (fwi);
			PrintWriter fichier = new PrintWriter (bwi); 
			fichier.write(nouvelleChaine);
			fichier.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public void modifierNoeuds(int nb_nodes){
		InputStream ips;
		String ligne;
		int i=0;
		String nouvelleChaine="";
		try {
			ips = new FileInputStream("C:/anahla/PNT/"+nomFichier);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			
			while((ligne= br.readLine())!=null){
				i++;
				if (i==11)
					nouvelleChaine+="nb_nodes="+nb_nodes+";\n";
				else
					nouvelleChaine+=ligne+"\n";
				
			}
			br.close();
			
			FileWriter fwi = new FileWriter("C:/anahla/PNT/"+nomFichier);
			BufferedWriter bwi = new BufferedWriter (fwi);
			PrintWriter fichier = new PrintWriter (bwi); 
			fichier.write(nouvelleChaine);
			fichier.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public String execInCmd(String cmd){	
		// CMD POUR EXECUTER UN .m DANS SANS LANCER MATLAB
		// \"C:/Program Files/MATLAB/R2014a/bin/matlab.exe\" -nodisplay -nosplash -nodesktop -r run('C:/anahla/PNT/prop2evid.m');exit;
			String resultatCmd = "";
			cmd = "cmd /c "+cmd;
			Process p;
			try {
				p = Runtime.getRuntime().exec(cmd);
				InputStream in = p.getInputStream();			
				int ch;
		         while((ch = in.read()) != -1) {
		        	 resultatCmd +=((char)ch);
		         }
		         
		         while(isProcessRunning("MATLAB.exe"));

			} catch (IOException e) {
				e.printStackTrace();
			}	
			
		return resultatCmd;
	}
	
	
	public static boolean isProcessRunning(String serviceName){
		Process p;
		try {
			p = Runtime.getRuntime().exec("tasklist");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
			p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
//				System.out.println(line);
				if (line.contains(serviceName)) {
					return true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		 return false;

	}
	
}
