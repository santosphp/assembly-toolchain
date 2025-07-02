package app.gui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import app.gui.Theme;

public class EditorPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTextArea textArea;
    private File currentFile;
    
	public EditorPanel() {		
        setLayout(new BorderLayout());
        setBorder(Theme.createTitledBorder("Editor"));
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.BACKGROUND);
        
        textArea = new JTextArea();
        styleTextEditorJTextArea(textArea);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
        addFileButtons();
    }
	
	private void styleTextEditorJTextArea(JTextArea area) {
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(Theme.BACKGROUND);
        area.setForeground(Theme.FOREGROUND);
        area.setCaretColor(Theme.FOREGROUND);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        area.setLineWrap(true);
    }

	public String getCode() {
		return textArea.getText();
	}
	
    public void setCode(String code) {
        textArea.setText(code);
    }
	    
    private void addFileButtons() {
        JPanel fileButtons = new JPanel();
        fileButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        fileButtons.setBackground(Theme.BACKGROUND);

        JButton openButton = Theme.createButton("Open");
        JButton saveButton = Theme.createButton("Save");
        JButton saveAsButton = Theme.createButton("Save As");

        openButton.addActionListener(e -> openFile());
        saveButton.addActionListener(e -> saveFile(false));
        saveAsButton.addActionListener(e -> saveFile(true));

        fileButtons.add(openButton);
        fileButtons.add(saveButton);
        fileButtons.add(saveAsButton);

        // Border between fileButtons and JTextArea
        fileButtons.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.FOREGROUND), // Bottom line
        	    BorderFactory.createEmptyBorder(0, 0, 5, 0) // Bottom padding only
        ));
        
        add(fileButtons, BorderLayout.NORTH);
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                currentFile = chooser.getSelectedFile();
                String content = Files.readString(currentFile.toPath());
                textArea.setText(content);
            } catch (IOException e) {
                showError("Failed to open file: " + e.getMessage());
            }
        }
    }

    public void saveFile(boolean forceChoose) {
        if (currentFile == null || forceChoose) {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) return;
            currentFile = chooser.getSelectedFile();
        }

        try {
            Files.writeString(currentFile.toPath(), getCode());
        } catch (IOException e) {
            showError("Failed to save file: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
