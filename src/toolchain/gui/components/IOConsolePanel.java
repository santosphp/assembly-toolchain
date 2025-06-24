package toolchain.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import toolchain.gui.Theme;

public class IOConsolePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JTextArea textArea;
    private final JTextField inputField;

	public IOConsolePanel() {
        setLayout(new BorderLayout());
        setBorder(Theme.createTitledBorder("I/O Console"));
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);


        // Output area
        textArea = new JTextArea();
        styleTerminalOutput(textArea);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        
        // Input area
        inputField = new JTextField();
        styleTerminalInput(inputField);
        add(inputField, BorderLayout.SOUTH);
    }
	
	 private void styleTerminalOutput(JTextArea area) {
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(Theme.BACKGROUND);
        area.setForeground(Theme.FOREGROUND);  // Bright green
        area.setCaretColor(Color.GREEN);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        area.setLineWrap(true);
    }

    private void styleTerminalInput(JTextField field) {
        field.setFont(new Font("Monospaced", Font.PLAIN, 14));
        field.setBackground(Theme.BACKGROUND);
        field.setForeground(Theme.FOREGROUND);
        field.setCaretColor(Theme.FOREGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.FOREGROUND, 1), // darker green
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    // Public methods for console operations
    public void appendOutput(String text) {
        textArea.append(text + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    public void clear() {
        textArea.setText("");
    }

    public String getInput() {
        String input = inputField.getText();
        inputField.setText("");
        return input;
    }
}
