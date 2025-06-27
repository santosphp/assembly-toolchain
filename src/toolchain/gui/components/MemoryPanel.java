package toolchain.gui.components;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import toolchain.gui.Theme;
import toolchain.vm.VirtualMachine;

public class MemoryPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public MemoryPanel() {
        setBorder(Theme.createTitledBorder("Memory"));
        setLayout(new BorderLayout());
        add(new JLabel("[Memory View]", SwingConstants.CENTER), BorderLayout.CENTER);
        
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
    }

	public void refresh(VirtualMachine vm) {
		// TODO Auto-generated method stub
		
	}
}
