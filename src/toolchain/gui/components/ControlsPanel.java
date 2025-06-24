package toolchain.gui.components;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import toolchain.gui.Theme;

public class ControlsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public ControlsPanel() {
        setBorder(Theme.createTitledBorder("Controls"));
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        add(new JButton("Run"));
        add(new JButton("Step"));
        
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
    }
}
