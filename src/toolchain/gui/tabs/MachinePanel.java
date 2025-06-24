package toolchain.gui.tabs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import toolchain.gui.components.ControlsPanel;
import toolchain.gui.components.EditorPanel;
import toolchain.gui.components.IOConsolePanel;
import toolchain.gui.components.MemoryPanel;
import toolchain.gui.components.RegistersPanel;
import toolchain.gui.components.StackPanel;

public class MachinePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public MachinePanel() {
	    super(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5); // Puts 5px margins all around each column
	    gbc.fill = GridBagConstraints.BOTH;

	    // Column 1: Memory (20% width) 
	    gbc.gridx = 0;
	    gbc.weightx = 0.2;
	    gbc.weighty = 1.0;
	    add(new MemoryPanel(), gbc);

	    // Column 2: Editor and I/O (40% width)
	    gbc.gridx = 1;
	    gbc.weightx = 0.4;
	    JPanel col2 = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc2 = new GridBagConstraints();
	    gbc2.fill = GridBagConstraints.BOTH;
	    gbc2.weightx = 1.0;  // Necessary for components to fill up the column horizontally
	    gbc2.insets = new Insets(0, 0, 0, 0); // No margins in between components

	    gbc2.gridy = 0;
	    gbc2.weighty = 0.7;  // 70%
	    col2.add(new EditorPanel(), gbc2);

	    gbc2.gridy = 2;
	    gbc2.weighty = 0.3;  // 30%
	    col2.add(new IOConsolePanel(), gbc2);

	    add(col2, gbc);

	    // Column 3: Control panel, Registers and Stack  (30% width)
	    gbc.gridx = 2;
	    gbc.weightx = 0.3;
	    JPanel col3 = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc3 = new GridBagConstraints();
	    gbc3.fill = GridBagConstraints.BOTH;
	    gbc3.weightx = 1.0;  // Necessary for components to fill up the column horizontally
	    gbc3.insets = new Insets(0, 0, 0, 0); // No margins in between components

	    gbc3.gridy = 0;
	    gbc3.weighty = 0.15;  // 15%
	    col3.add(new ControlsPanel(), gbc3);

	    gbc3.gridy = 1;
	    gbc3.weighty = 0.3;  // 30%
	    col3.add(new RegistersPanel(), gbc3);

	    gbc3.gridy = 2;
	    gbc3.weighty = 0.55;  // 55%
	    col3.add(new StackPanel(), gbc3);

	    add(col3, gbc);
	}
}
