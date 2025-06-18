package toolchain.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPanel;
	private CardLayout cardLayout;

	public MainFrame() {
		super("Educational Assembly Toolchain");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Close application on X
        setLocationRelativeTo(null); // Center on screen
        
        SidebarPanel sidebar = new SidebarPanel();
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        contentPanel.add(new MachinePanel(), "Machine");
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        
        cardLayout.show(contentPanel,  "Machine");
    }
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
	}

}
