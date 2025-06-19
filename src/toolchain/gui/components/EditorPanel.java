package toolchain.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class EditorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	public EditorPanel() {
        setBorder(new TitledBorder("Binary Editor"));
        setLayout(new BorderLayout());
        
        JTextArea editor = new JTextArea();
        editor.setLineWrap(true);
        
        editor.setPreferredSize(new Dimension(10, 10));
        
        JScrollPane scrollPane = new JScrollPane(editor);
        scrollPane.setPreferredSize(new Dimension(200, 150));
        
        add(scrollPane, BorderLayout.CENTER);
    }
}
