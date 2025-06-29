package toolchain.gui;

import java.awt.CardLayout;
import java.awt.Container;

import javax.swing.JPanel;

import toolchain.gui.tabs.*;
import toolchain.vm.VirtualMachine;

public class ContentPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final CardLayout layout;
    
    public ContentPanel(VirtualMachine vm) {
        layout = new CardLayout();
        setLayout(layout);
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
        
        addTab("Machine", new MachinePanel(vm));
        addTab("Assembler", new AssemblerPanel(vm));
    }
    
    public void addTab(String name, JPanel panel) {
        applyBaseTheme(panel);
        add(panel, name);
    }
    
    public void showTab(String name) {
        layout.show(this, name);
    }
    
    private void applyBaseTheme(Container container) {
        container.setBackground(Theme.BACKGROUND);
        container.setForeground(Theme.FOREGROUND);
    }
}
