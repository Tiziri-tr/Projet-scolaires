package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MatlabVision {
	
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
	
	public String getResult(String path){
		String res="";
		String ligne="";
		InputStream ips;
		try {
			ips = new FileInputStream(path);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			while((ligne = br.readLine())!=null)
				res +=ligne+"\n";
			br.close();
		} catch (FileNotFoundException e) {
			res=e.getMessage();
		} catch (IOException e) {
			res=e.getMessage();
		}
	
		return res;
	}
	
	public void modifierImage(String nomFichier,String newImg){
		InputStream ips;
		String Oldigne;
		int i=0;
		String nouvelleChaine="";
		try {
			ips = new FileInputStream("C:/Users/Tiziri/Documents/MATLAB/"+nomFichier);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			
			while((Oldigne= br.readLine())!=null){
				i++;
				if(i==9)
					nouvelleChaine+="imgTest = imread('C:/Users/Tiziri/workspace/projetVisionFx/images_test/"+newImg+"');\n";
				else
					nouvelleChaine+=Oldigne+"\n";
				
			}
			br.close();
			
			FileWriter fwi = new FileWriter("C:/Users/Tiziri/Documents/MATLAB/"+nomFichier);
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
	
}
