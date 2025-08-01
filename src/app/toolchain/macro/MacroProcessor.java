package app.toolchain.macro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

class Pair { 
	public String key;
	public int value;
		
	public Pair (String key, int value) {
		this.key = key;
		this.value = value;
	}
	
}


public class MacroProcessor {
	
	public static void main(String[] args) {
		MacroProcessor mp = new MacroProcessor();
		mp.processFile("src/app/toolchain/macro/macros1.txt", "src/app/toolchain/macro/sla.txt");
	}
	
	private ArrayList<Pair> macroNameTable;
	private ArrayList<String> macroDefinitionTable;
	
	private void expand (PrintWriter pw, int macroIndex, String mc) {
		int temp; 
		String aux;
		
		// ALA - Argument array list 
		String[] args = mc.split(" ");
		String[] params = macroDefinitionTable.get(macroNameTable.get(macroIndex).value).split(" ");
		
		for(int i = macroNameTable.get(macroIndex).value+1; true; i++) {
			aux = macroDefinitionTable.get(i);
			
			for(int j = 0; j< args.length; j++) {
				aux = aux.replaceAll(params[j], args[j]);
			}
			
			if(aux.equals("MEND")) {
				break;
			} else if (aux.equals("MACRO")) {
				i = define(i+1, args, params)-1; //fragil
			} else {
	    		temp = isMacro(aux);
	    		if(temp == -1) {
	    			pw.println(aux);
	    		} else {
	    			expand(pw, temp, aux);
	    		}

			}
		}
	}
	
	private int define (int macroIndex, String[] args, String[] params) {
		int aux = 0;
		String temp = macroDefinitionTable.get(macroIndex).split(" ")[0];
		for(int j = 0; j< args.length; j++) {
			temp = temp.replaceAll(params[j], args[j]);
		}
		macroNameTable.add(new Pair(temp, macroDefinitionTable.size()));
		for(int i = macroIndex; true; i++) {
			temp = macroDefinitionTable.get(i);
			
			for(int j = 0; j< args.length; j++) {
				temp = temp.replaceAll(params[j], args[j]);
			}
			System.out.println(temp);
			macroDefinitionTable.add(temp);
			if(temp.equals("MACRO")) aux++;
	    	if(temp.equals("MEND")) {
	    		if(aux > 0) aux--;
	    		else return i+1;
	    	}
		}
	}
	
	private int isMacro(String line) {
		String aux = line.split(" ")[0];
		for(int i = 0; i < macroNameTable.size(); i++) {
			if(macroNameTable.get(i).key.equals(aux)) {
				return i;
			}
		}
		return -1;
	}

	public void processFile(String file, String macroOutPath) {

		macroNameTable = new ArrayList<Pair>();
		macroDefinitionTable = new ArrayList<String>();
		
		// normal = 0, expensao = 1
		int mode = 0, temp, count = 0;
		String aux;

		try {
			System.out.println("Loading from file: " + file);
			File in = new File(file);
			File out = new File(macroOutPath);
		    Scanner sc = new Scanner(in);
		    PrintWriter pw = new PrintWriter(new FileWriter(out));
		    
		    while (sc.hasNextLine()) {
		    	aux = sc.nextLine();
		    	if(mode == 0) { // normal
		    		temp = isMacro(aux);
		    		if(aux.equals("MACRO")) {
				    	aux = sc.nextLine();
				    	macroNameTable.add(new Pair(aux.split(" ")[0], macroDefinitionTable.size()));
				    	macroDefinitionTable.add(aux);
				    	mode = 1;
				    	count = 0;
		    		} else if(temp != -1){
		    			expand(pw, temp, aux);
		    		} else {		    			
		    			pw.println(aux);
		    		}
		    		
		    	} else { // definicao
			    	macroDefinitionTable.add(aux);
			    	if(aux.equals("MACRO")) {
			    		count++;
			    	} else if(aux.equals("MEND")) {
			    		if(count > 0) count--;
			    		else mode = 0;
			    	}
		    	}
		    }
		    sc.close();
		    pw.close();
		    
		    System.out.println("gay");
		    int i =0;
		    for(String elem : macroDefinitionTable) {
		    	i++;
		    	System.out.println(i+" "+elem);
		    }
		    
		    for(Pair elem : macroNameTable) {
		    	System.out.println(elem.key);
		    }
		  } catch (FileNotFoundException e) {
		    System.out.println("An error occurred fetching the macros.");
		    e.printStackTrace();
		  } catch (IOException e) {
			  System.out.println("An error occurred fetching the macros.");
			  e.printStackTrace();
		 }
	}

}
