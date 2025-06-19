package toolchain.gui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class RegistersPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public RegistersPanel() {
        setBorder(new TitledBorder("Registers"));
        setLayout(new BorderLayout());
        add(new JLabel("[Registers]", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
