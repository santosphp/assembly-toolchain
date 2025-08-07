package app.main;

import java.io.IOException;

import app.gui.MainFrame;
import app.toolchain.Toolchain;

public class Main {
	public static void main(String[] args) {
		// Change the commented section to test the assembler...
	    Toolchain toolchain = new Toolchain();
	    try {
			toolchain.getAssembler().assemble("assembler_test.txt", "object", "list");
		} catch (IOException e) {
			e.printStackTrace();
		}

		/*
	    Toolchain toolchain = new Toolchain();
	    javax.swing.SwingUtilities.invokeLater(() -> {
	        new MainFrame(toolchain).setVisible(true);
	    });
		*/
	    
	}
}