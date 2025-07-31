package app.gui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import app.gui.Theme;

public class ControlsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JLabel nextInstructionLabel;
    private final JButton buildButton;
    private final JButton runButton;
    private final JButton quickRunButton;
    private final JButton stepButton;
    
	public ControlsPanel() {
        setBorder(Theme.createTitledBorder("Controls"));
        setLayout(new BorderLayout(5, 5));
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonsPanel.setBackground(Theme.BACKGROUND);

        buildButton = Theme.createButton("Build");
        buildButton.setToolTipText("Assemble and link the current code into the VM");
        
        quickRunButton = Theme.createButton("Quick run");
        runButton = Theme.createButton("Run");
        stepButton = Theme.createButton("Step");
        
        disableExecutionButtons();
        
        buttonsPanel.add(buildButton);
        buttonsPanel.add(quickRunButton);
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
	
	public void disableExecutionButtons() {
		buildButton.setEnabled(true);
        quickRunButton.setEnabled(false);
        runButton.setEnabled(false);
        stepButton.setEnabled(false);
	}
	
	public void enableExecutionButtons() {
		buildButton.setEnabled(true);
        quickRunButton.setEnabled(true);
        runButton.setEnabled(true);
        stepButton.setEnabled(true);
	}
	
	
    public JButton getBuildButton() { return buildButton; }
    public JButton getRunButton() { return runButton; }
    public JButton getStepButton() { return stepButton; }
	public JButton getQuickRunButton() { return quickRunButton; }
}
