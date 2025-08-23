package app.main;

import java.io.IOException;

import app.gui.MainFrame;
import app.toolchain.Toolchain;
import app.toolchain.Tables;

public class Main {
	public static void main(String[] args) {
		// Change the commented section to test the assembler...
        Tables tables = new Tables();
	    Toolchain toolchain = new Toolchain();
	    try {
			toolchain.getAssembler().assemble("assembler_test.txt", "files/object", "files/list", tables);
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