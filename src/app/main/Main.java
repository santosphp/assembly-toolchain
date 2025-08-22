package app.main;

import app.gui.MainFrame;
import app.toolchain.Toolchain;

public class Main {
	public static void main(String[] args) {

		// Change the commented section to test the assembler and linker...
	    Toolchain toolchain = new Toolchain();
	    toolchain.testAssemblerLinker();

		/*
	    Toolchain toolchain = new Toolchain();
	    javax.swing.SwingUtilities.invokeLater(() -> {
	        new MainFrame(toolchain).setVisible(true);
	    });
		*/
	    
	}
}