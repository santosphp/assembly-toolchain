package app.gui.components;

import java.awt.BorderLayout;

import javax.swing.*;

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

        DefaultListModel<String> myList = new DefaultListModel<>();
        myList.addElement("Register PC: "+ 0);      //brute forced implementation of registers
        myList.addElement("Register SP: "+ 0);      //should have a better way to do this
        myList.addElement("Register ACC: "+ 0);
        myList.addElement("Register RI: "+ 0);
        myList.addElement("Register RE: "+ 0);
        myList.addElement("Register R0: "+ 0);
        myList.addElement("Register R1: "+ 0);
        myList.addElement("Register MOP: "+ 0);
        JList<String> list = new JList<>(myList);
        this.add(list, BorderLayout.CENTER);        //could be changed
    }

	public void refresh(VirtualMachine vm) {
		// TODO Auto-generated method stub
		
	}
    public void setRegisterValue(int Value){      //possible implementation based on nextInstructionLabel
    //    registerValueLabel.setText("registrador" + value);    //don't know how exactly to refresh register value
    }
}
