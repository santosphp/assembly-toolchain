package app.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import app.gui.Theme;
import app.toolchain.vm.VirtualMachine;

public class RegistersPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Map<String, JTextField> registerFields;

	public RegistersPanel() {
        setBorder(Theme.createTitledBorder("Registers"));
        setLayout(new GridBagLayout());
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);

        registerFields = new HashMap<>();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        
        //passar o identificador do registrador para o método, para que possamos mapear o JTextField
        addRegisterField(this, gbc, "PC", "PC");
        addRegisterField(this, gbc, "SP", "SP");
        addRegisterField(this, gbc, "ACC", "ACC");
        addRegisterField(this, gbc, "MOP", "MOP"); //MOP implementado um pouco diferente (n existe na CPU, necessário discussão posterior
        addRegisterField(this, gbc, "R0", "R0");
        addRegisterField(this, gbc, "R1", "R1");
        addRegisterField(this, gbc, "RI", "RI");
        addRegisterField(this, gbc, "RE", "RE");
    }
	
	private void addRegisterField(JPanel parent, GridBagConstraints gbc, String labelText, String registerKey) {
        JLabel label = new JLabel(labelText + ":", SwingConstants.RIGHT);
        label.setForeground(Theme.FOREGROUND);
        gbc.gridx = 0;
        gbc.weightx = 0.3; 
        gbc.anchor = GridBagConstraints.WEST;
        parent.add(label, gbc);

        JTextField valueField = new JTextField("0x0000");
        valueField.setEditable(false);
        valueField.setBackground(Theme.BACKGROUND);
        valueField.setForeground(Theme.FOREGROUND);
        valueField.setFont(Theme.MONO_FONT);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        parent.add(valueField, gbc);

        registerFields.put(registerKey, valueField);	//adiciona JTextField p/ mapa

        gbc.gridy++;
    }

    //atualiza um campo de registrador específico
    private void updateRegisterField(String registerKey, int value, int bitSize) {
        JTextField field = registerFields.get(registerKey); //
        if (field != null) {
        	
        	String format = String.valueOf(value);
        	field.setText(format); //atualiza o campo com o valor formatado
        }
    }

	public void refresh(VirtualMachine vm) {
        Map<String, Integer> registersState = vm.getCpu().getRegistersState(); //adicionado getCpu na VM

        updateRegisterField("PC", registersState.get("PC"), 16);
        updateRegisterField("SP", registersState.get("SP"), 16);
        updateRegisterField("ACC", registersState.get("ACC"), 16);
        updateRegisterField("MOP", vm.getMop(), 8); //MOP é um atributo da VM
        updateRegisterField("R0", registersState.get("R0"), 16);
        updateRegisterField("R1", registersState.get("R1"), 16);
        updateRegisterField("RI", registersState.get("RI"), 16);
        updateRegisterField("RE", registersState.get("RE"), 16);
    }
}