package toolchain.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class SidebarPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public SidebarPanel() {
		setPreferredSize(new Dimension(150, 0));  // Fixed width for sidebar
		setBackground(new Color(70, 70, 70));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add spacing at the top
        add(Box.createVerticalStrut(30));

        String[] buttons = {"Home", "Assembler", "Linker", "Machine"};

        ButtonGroup group = new ButtonGroup();

        for (String name : buttons) {
            JToggleButton btn = new JToggleButton(name);
            styleButton(btn);

            if (name.equals("Machine")) {
                btn.setSelected(true); // Default selected
            }

            group.add(btn);
            add(btn);
            add(Box.createVerticalStrut(15));
        }

        add(Box.createVerticalGlue());
    }
	
	 private void styleButton(JToggleButton btn) {
	        Dimension size = new Dimension(130, 40);
	        btn.setMinimumSize(size);
	        btn.setMaximumSize(size);
	        btn.setPreferredSize(size);

	        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
	        btn.setFocusPainted(false);
	        btn.setBackground(new Color(60, 60, 60));
	        btn.setForeground(Color.WHITE);
	        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

	        // Selected state color
	        btn.setSelectedIcon(null);
	        btn.setContentAreaFilled(true);

	        btn.addChangeListener(e -> {
	            if (btn.isSelected()) {
	                btn.setBackground(new Color(200, 160, 0));  // Dark yellow when selected
	            } else {
	                btn.setBackground(new Color(60, 60, 60));
	            }
	        });
	    }
	
}
