package toolchain.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPanel;

import toolchain.gui.tabs.MachinePanel;

public class ContentPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    public static final Color BASE_BACKGROUND = new Color(70, 70, 70);
    public static final Color BASE_FOREGROUND = new Color(230, 230, 230); // "Soft white"
    
    public ContentPanel() {
        setLayout(new CardLayout());
        setBackground(BASE_BACKGROUND);
        setForeground(BASE_FOREGROUND);
        
        addTab("Machine", new MachinePanel());
    }
    
    public void addTab(String name, JPanel panel) {
        applyBaseTheme(panel);
        add(panel, name);
    }
    
    private void applyBaseTheme(Container container) {
        container.setBackground(BASE_BACKGROUND);
        container.setForeground(BASE_FOREGROUND);
        
        for (Component comp : container.getComponents()) {
            if (comp instanceof JComponent) {
                JComponent jcomp = (JComponent) comp;
                jcomp.setBackground(BASE_BACKGROUND);
                jcomp.setForeground(BASE_FOREGROUND);
                
                // Recursively apply to child containers
                if (comp instanceof Container) {
                    applyBaseTheme((Container) comp);
                }
            }
        }
    }
}
