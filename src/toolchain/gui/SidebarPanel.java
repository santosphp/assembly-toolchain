package toolchain.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

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

    // Color definitions
    private static final Color SIDEBAR_BG = new Color(45, 45, 45);
    private static final Color BUTTON_BG = new Color(55, 55, 55);
    private static final Color BUTTON_SELECTED_BG = new Color(70, 70, 70); // Match content panel
    private static final Color BUTTON_TEXT = new Color(220, 220, 220); // Soft white
    private static final Color BUTTON_SELECTED_TEXT = Color.WHITE;
	
    private final Map<String, JToggleButton> buttonMap = new HashMap<>();
    
	public SidebarPanel(ContentPanel contentPanel) {
		setPreferredSize(new Dimension(100, 0));  // Intentionally has a fixed width
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(SIDEBAR_BG);
        
        
        JLabel header = new JLabel("Tabs", SwingConstants.CENTER);
        header.setForeground(Color.LIGHT_GRAY);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 15f));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(12));
        add(header);
        add(Box.createVerticalStrut(8));
        
        UIManager.put("ToggleButton.background", BUTTON_BG);
        UIManager.put("ToggleButton.foreground", BUTTON_TEXT);
        UIManager.put("ToggleButton.select", BUTTON_SELECTED_BG);
        
        String[] buttons = {"Home", "Macro", "Assembler", "Linker","Machine"};
        ButtonGroup group = new ButtonGroup();
        
        for (String name : buttons) {
            JToggleButton btn = new JToggleButton(name);
            styleButton(btn);
            
            // At the final part of the project the default will be "Home"
            boolean enabled = name.equals("Machine") || name.equals("Assembler");
            btn.setEnabled(enabled);
            buttonMap.put(name, btn);
            if(enabled) {
                btn.setForeground(BUTTON_SELECTED_TEXT);
                btn.setBackground(BUTTON_SELECTED_BG);
            }
            btn.addActionListener(e -> contentPanel.showTab(name));

            group.add(btn);
            add(btn);
            add(Box.createVerticalStrut(6));

        }
        add(Box.createVerticalGlue());
    }
	
	public void selectButton(String name) {
        JToggleButton btn = buttonMap.get(name);
        if (btn != null && btn.isEnabled()) {
            btn.setSelected(true);
        }
    }
	
	private void styleButton(JToggleButton btn) {
		Dimension size = new Dimension(100, 40);
		btn.setMinimumSize(size);
		btn.setMaximumSize(size);
		btn.setPreferredSize(size);
		
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setBackground(BUTTON_BG);
		btn.setForeground(BUTTON_TEXT);
		
		// Remove highlighted borders
	    btn.setFocusPainted(false);
	    btn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));	   
	    
	    btn.addChangeListener(e -> {
	        if (btn.isSelected()) {
	            btn.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, Color.WHITE));
	            btn.setBackground(BUTTON_SELECTED_BG);
	            btn.setForeground(BUTTON_SELECTED_TEXT);
	        } else {
	            btn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		        btn.setBackground(BUTTON_BG);
	            btn.setForeground(BUTTON_TEXT);
	        }
    	});
	}
}
