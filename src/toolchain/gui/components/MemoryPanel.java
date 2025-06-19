package toolchain.gui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class MemoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public MemoryPanel() {
        setBorder(new TitledBorder("Memory"));
        setLayout(new BorderLayout());
        add(new JLabel("[Memory View]", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
