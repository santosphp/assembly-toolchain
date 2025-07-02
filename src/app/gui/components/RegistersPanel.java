package app.gui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.gui.Theme;
import app.toolchain.vm.VirtualMachine;

public class RegistersPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public RegistersPanel() {
        setBorder(Theme.createTitledBorder("Registers"));
        setLayout(new BorderLayout());
        add(new JLabel("[Registers]", SwingConstants.CENTER), BorderLayout.CENTER);
        
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
    }

	public void refresh(VirtualMachine vm) {
		// TODO Auto-generated method stub
		
	}
}
