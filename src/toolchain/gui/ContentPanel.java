package toolchain.gui;

import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JPanel;

import toolchain.gui.tabs.MachinePanel;
import toolchain.vm.VirtualMachine;

public class ContentPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public ContentPanel(VirtualMachine vm) {
        setLayout(new CardLayout());
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
        
        addTab("Machine", new MachinePanel(vm));
    }
    
    public void addTab(String name, JPanel panel) {
        applyBaseTheme(panel);
        add(panel, name);
    }
    
    private void applyBaseTheme(Container container) {
        container.setBackground(Theme.BACKGROUND);
        container.setForeground(Theme.FOREGROUND);
    }
}
