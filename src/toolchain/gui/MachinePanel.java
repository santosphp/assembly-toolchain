package toolchain.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MachinePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public MachinePanel() {
        setBackground(new Color(70, 70, 70));
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Machine Panel", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 24));


        add(label, BorderLayout.CENTER);
    }
}

