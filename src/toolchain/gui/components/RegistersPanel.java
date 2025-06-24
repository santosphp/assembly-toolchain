package toolchain.gui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import toolchain.gui.Theme;

public class RegistersPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public RegistersPanel() {
        setBorder(Theme.createTitledBorder("Registers"));
        setLayout(new BorderLayout());
        add(new JLabel("[Registers]", SwingConstants.CENTER), BorderLayout.CENTER);
        
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
    }
}
