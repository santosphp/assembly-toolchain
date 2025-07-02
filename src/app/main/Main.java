package app.main;

import app.gui.MainFrame;
import app.toolchain.Toolchain;

public class Main {
	public static void main(String[] args) {
	    Toolchain toolchain = new Toolchain();
	    javax.swing.SwingUtilities.invokeLater(() -> {
	        new MainFrame(toolchain).setVisible(true);
	    });
	}
}
