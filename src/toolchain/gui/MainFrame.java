package toolchain.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

    public MainFrame() {
        super("Educational Assembly Toolchain");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Close application on X
        setLocationRelativeTo(null); // Center on screen
    }
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
	}

}
