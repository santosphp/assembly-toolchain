package toolchain.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import toolchain.gui.Theme;

public class EditorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public EditorPanel() {
        setLayout(new BorderLayout());
        setBorder(Theme.createTitledBorder("Editor"));
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.BACKGROUND);
        
        JTextArea textArea = new JTextArea();
        styleTerminalOutput(textArea);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }
	
	private void styleTerminalOutput(JTextArea area) {
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(Theme.BACKGROUND);
        area.setForeground(Theme.FOREGROUND);
        area.setCaretColor(Theme.FOREGROUND);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        area.setLineWrap(true);
    }
}
