package toolchain.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class SidebarPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;  // Added to suppress a specific warning

	private Color sidebarColor = new Color(45, 45, 45);
	private Color unselectedBtnColor = new Color(55, 55, 55);
	private Color contentPanelColor = new Color(70, 70, 70);
	
	public SidebarPanel() {
		setPreferredSize(new Dimension(100, 0));  // Intentionally has a fixed width
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        

		setBackground(sidebarColor);
        
        
        JLabel header = new JLabel("Tabs", SwingConstants.CENTER);
        header.setForeground(Color.LIGHT_GRAY);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 15f));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(12));
        add(header);
        add(Box.createVerticalStrut(8));
        
        UIManager.put("ToggleButton.select", contentPanelColor);  // contentPanel's background color
        String[] buttons = {"Home", "Assembler", "Linker", "Machine"};
        ButtonGroup group = new ButtonGroup();
        
        for (String name : buttons) {
            JToggleButton btn = new JToggleButton(name);
            styleButton(btn);
            btn.setEnabled(false);
            
            if (name.equals("Machine")) {
                btn.setEnabled(true);
                btn.setSelected(true);
            }

            group.add(btn);
            add(btn);
            add(Box.createVerticalStrut(6));

        }
        add(Box.createVerticalGlue());
    }
	
	 private void styleButton(JToggleButton btn) {
	        Dimension size = new Dimension(100, 40);
	        btn.setMinimumSize(size);
	        btn.setMaximumSize(size);
	        btn.setPreferredSize(size);

	        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
	        btn.setBackground(unselectedBtnColor);
	        btn.setForeground(Color.WHITE);
	        
	        // Remove highlighted borders
	        btn.setFocusPainted(false);
	        btn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));	   
	        
	        btn.addChangeListener(e -> {
	            if (btn.isSelected()) {
	                btn.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.WHITE));
	            } else {
	                btn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
	            }
	        });
	    }
	
}
