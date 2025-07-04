package app.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import app.gui.Theme;
import app.toolchain.vm.VirtualMachine;

public class RegistersPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public RegistersPanel() {
        setBorder(Theme.createTitledBorder("Registers"));
        //setLayout(new BorderLayout());
        //add(new JLabel("[Registers]", SwingConstants.CENTER), BorderLayout.CENTER);
        setLayout(new GridBagLayout());
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5); // espaçamento dos pixels
        gbc.fill = GridBagConstraints.HORIZONTAL; // Preenche horizontalmente
        gbc.gridy = 0; 	// inicializa como 0 para valor padrão não ser obtido (RELATIVE), garantindo que tenha 8 linhas
        gbc.weighty = 0.1;	//	espaçamento entre linhas
        
        addRegisterField(this, gbc, "PC", "pc");
        addRegisterField(this, gbc, "SP", "sp");
        addRegisterField(this, gbc, "ACC", "acc");
        addRegisterField(this, gbc, "MOP", "mop");
        addRegisterField(this, gbc, "R0", "r0");
        addRegisterField(this, gbc, "R1", "r1");
        addRegisterField(this, gbc, "RI", "ri"); // Registrador Interno, talvez possam ser omitidos da visualização
        addRegisterField(this, gbc, "RE", "re"); // Registrador Interno
       
    }
	
	private void addRegisterField(JPanel parent, GridBagConstraints gbc, String labelText, String registerKey) {
        JLabel label = new JLabel(labelText + ":", SwingConstants.RIGHT);
        label.setForeground(Theme.FOREGROUND);
        gbc.gridx = 0;
        gbc.weightx = 0.3; 
        gbc.anchor = GridBagConstraints.WEST;	// alinha p/ esquerda
        parent.add(label, gbc);

        JTextField valueField = new JTextField("0x0000"); // valor inicial
        valueField.setEditable(false);
        valueField.setBackground(Theme.BACKGROUND);
        valueField.setForeground(Theme.FOREGROUND);
        valueField.setFont(Theme.MONO_FONT);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 0.1;
        gbc.anchor = GridBagConstraints.WEST;
        parent.add(valueField, gbc);

        gbc.gridy++; // Próxima linha para o próximo registrador
    }

	public void refresh(VirtualMachine vm) {
        // Exemplo de como obter e formatar valores (implementar getPC, getSP, etc. na VM)
        /**
        updateRegisterField("pc", vm.getPC(), 16);
        updateRegisterField("sp", vm.getSP(), 16);
        updateRegisterField("acc", vm.getACC(), 16);
        updateRegisterField("mop", vm.getMOP(), 8); // MOP é de 8 bits
        updateRegisterField("r0", vm.getR0(), 16);
        updateRegisterField("r1", vm.getR1(), 16);
        updateRegisterField("ri", vm.getRI(), 16); // Registrador Interno
        updateRegisterField("re", vm.getRE(), 16); // Registrador Interno
        **/
    }
    
}
