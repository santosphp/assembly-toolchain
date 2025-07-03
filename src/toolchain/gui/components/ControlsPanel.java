package toolchain.gui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import toolchain.gui.Theme;

public class ControlsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JLabel nextInstructionLabel;
    private final JButton loadButton;
    private final JButton runButton;
    private final JButton stepButton;
    
	public ControlsPanel() {
        setBorder(Theme.createTitledBorder("Controls"));
        setLayout(new BorderLayout(5, 5));
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonsPanel.setBackground(Theme.BACKGROUND);

        loadButton = Theme.createButton("Load");
        runButton = Theme.createButton("Run");
        stepButton = Theme.createButton("Step");

        buttonsPanel.add(loadButton);
        buttonsPanel.add(runButton);
        buttonsPanel.add(stepButton);

        add(buttonsPanel, BorderLayout.NORTH);
            
        // Next instruction
        nextInstructionLabel = new JLabel("Next instruction: NOP");
        nextInstructionLabel.setForeground(Theme.FOREGROUND);
        nextInstructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(nextInstructionLabel, BorderLayout.CENTER);
        
    }
	
	public void setNextInstruction(String instruction) {
        nextInstructionLabel.setText("Next instruction: " + instruction);
    }
	
    public JButton getLoadButton() { return loadButton; }
    public JButton getRunButton() { return runButton; }
    public JButton getStepButton() { return stepButton; }
}
