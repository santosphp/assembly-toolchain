package toolchain.gui.components;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ControlsPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public ControlsPanel() {
        setBorder(new TitledBorder("Controls"));
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        add(new JButton("Run"));
        add(new JButton("Step"));
    }
}
