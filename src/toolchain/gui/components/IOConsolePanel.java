package toolchain.gui.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
        area.setForeground(Theme.FOREGROUND);  
        area.setCaretColor(Theme.FOREGROUND);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        area.setLineWrap(true);
    }

    private void styleTerminalInput(JTextField field) {
        field.setFont(new Font("Monospaced", Font.PLAIN, 14));
        field.setBackground(Theme.BACKGROUND);
        field.setForeground(Theme.FOREGROUND);
        field.setCaretColor(Theme.FOREGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.FOREGROUND, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
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
    
    public void setOnInputSubmitted(Consumer<String> callback) {
        inputField.addActionListener(e -> {
            String input = inputField.getText().trim();
            inputField.setText("");
            if (!input.isEmpty()) {
                callback.accept(input);
            }
        });
    }

}
