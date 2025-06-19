package toolchain.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame() {
		super("Educational Assembly Toolchain");
        setSize(1024, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Close application on X
        setLocationRelativeTo(null); // Center on screen
        
        SidebarPanel sidebar = new SidebarPanel();
        ContentPanel contentPanel = new ContentPanel();
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);      
    }
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
	}

}
