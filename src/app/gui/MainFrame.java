package app.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import app.toolchain.Toolchain;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame(Toolchain toolchain) {
		super("Educational Assembly Toolchain");
        setSize(1366, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Close application on X
        setLocationRelativeTo(null); // Center on screen
        //setResizable(false);
        
        
        IDEPanel idePanel = new IDEPanel(toolchain);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(idePanel, BorderLayout.CENTER);      
    }
}
