package toolchain.gui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class NextInstructionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public NextInstructionPanel() {
        setBorder(new TitledBorder("Next Instruction"));
        setLayout(new BorderLayout());
        add(new JLabel("[Next Instr]", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
