package toolchain.gui;

import javax.swing.*;
import java.awt.*;

public class RegistersPanel extends JPanel {
    private static final long serialVersionUID = 1L;    //identificador de versão para classe serializável

    public RegistersPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets  = new Insets(2,5,2,5);
        c.fill      = GridBagConstraints.HORIZONTAL;

        c.gridx      = 0;   // colunas
        c.gridy      = 0;   // linhas
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("PC:"), c);

        c.gridx      = 1;
        c.gridy      = 0;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("0x0000:"), c);

        c.gridx      = 0;
        c.gridy      = 1;
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("SP:"), c);

        c.gridx      = 1;
        c.gridy      = 1;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("0x0000:"), c);

    }
}
