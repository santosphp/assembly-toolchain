package toolchain.gui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import toolchain.gui.Theme;

public class StackPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public StackPanel() {
        setBorder(Theme.createTitledBorder("Stack"));
        setLayout(new BorderLayout());
        add(new JLabel("[Stack]", SwingConstants.CENTER), BorderLayout.CENTER);
        
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
    }
}
