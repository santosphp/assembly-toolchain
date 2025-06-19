package toolchain.gui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class IOConsolePanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public IOConsolePanel() {
        setBorder(new TitledBorder("I/O Console"));
        setLayout(new BorderLayout());
        add(new JLabel("[I/O Console]", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
