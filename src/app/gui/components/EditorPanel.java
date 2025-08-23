package app.gui.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import app.gui.Theme;

public class EditorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPane;
    private JTextArea textArea; //área de texto
    private JTextArea textArea2;  //área 2

    //usar um mapa para armazenar o arquivo atual para cada aba, para que o "Save" funcione corretamente.
    private Map<JTextArea, File> currentFiles;
    
    //constantes para os índices das abas, facilita a referência
    public static final int AREA1_TAB_INDEX = 0;
    public static final int AREA2_TAB_INDEX = 1;

    public EditorPanel() {        
        setLayout(new BorderLayout());
        setBorder(Theme.createTitledBorder("Editor"));
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.BACKGROUND);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Theme.BACKGROUND);
        tabbedPane.setForeground(Theme.FOREGROUND);
        tabbedPane.setFont(Theme.MONO_BOLD);	//fonte do título

        // Config da aba 'Area1'
        textArea = new JTextArea();
        styleTextEditorJTextArea(textArea);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setBorder(null);
        tabbedPane.addTab("Área1", areaScrollPane);

        // Config da aba 'Area2'
        textArea2 = new JTextArea();
        styleTextEditorJTextArea(textArea2);
        JScrollPane secondScrollPane = new JScrollPane(textArea2);
        secondScrollPane.setBorder(null);
        tabbedPane.addTab("Área2", secondScrollPane);
        
        currentFiles = new HashMap<>();
        currentFiles.put(textArea, null);
        currentFiles.put(textArea2, null);

        add(tabbedPane, BorderLayout.CENTER);
        addFileButtons(); 
        setupTabChangeListener(); //configura o listener para a troca de abas
    }
    
    private void styleTextEditorJTextArea(JTextArea area) {
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(Theme.BACKGROUND);
        area.setForeground(Theme.FOREGROUND);
        area.setCaretColor(Theme.FOREGROUND);
        area.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        area.setLineWrap(true);
    }

    //métodos para obter e definir o texto das abas
    public String getAreaCode() {
        return textArea.getText();
    }

    public void setAreaCode(String code) {
    	textArea.setText(code);
    }
    
    public String getSecondCode() {
        return textArea2.getText();
    }

    public void setSecondCode(String code) {
    	textArea2.setText(code);
        setSelectedTabIndex(AREA2_TAB_INDEX); // Opcional: muda para a aba Area2 automaticamente
    }

    public int getSelectedTabIndex() {
        return tabbedPane.getSelectedIndex();
    }

    public void setSelectedTabIndex(int index) {
        tabbedPane.setSelectedIndex(index);
    }

    private JTextArea getCurrentTextArea() {
        // O componente selecionado no JTabbedPane é um JScrollPane.
        //precisa obter o 'view' do viewport do JScrollPane, que é o JTextArea.
        return (JTextArea) ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport().getView();
        //getSelComponent retorna component, o que necessita um cast,
        //transforma-se o component genérico em JScrollPane e é chamado seu método getView
    }

    private File getCurrentFileForSelectedTab() {
        return currentFiles.get(getCurrentTextArea());
    }

    private void setCurrentFileForSelectedTab(File file) {
        currentFiles.put(getCurrentTextArea(), file);
        //título da aba como nome do arquivo
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (file != null) {
            tabbedPane.setTitleAt(selectedIndex, (selectedIndex == AREA1_TAB_INDEX ? "Área: " : "Área2: ") + file.getName());
        } else {
            tabbedPane.setTitleAt(selectedIndex, (selectedIndex == AREA1_TAB_INDEX ? "Área" : "Área2"));
        }
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

        // Borda entre os botões de arquivo e a área de texto
        fileButtons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.FOREGROUND), // Linha inferior
                BorderFactory.createEmptyBorder(0, 0, 5, 0) // Preenchimento inferior
        ));
        
        add(fileButtons, BorderLayout.NORTH);
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = chooser.getSelectedFile();
                String content = Files.readString(selectedFile.toPath());
                JTextArea currentArea = getCurrentTextArea();
                currentArea.setText(content);
                setCurrentFileForSelectedTab(selectedFile); //associa o arquivo à aba selecionada
            } catch (IOException e) {
                showError("Failed to open file: " + e.getMessage());
            }
        }
    }

    public void saveFile(boolean forceChoose) {
        JTextArea currentArea = getCurrentTextArea(); // Obtém a área de texto da aba selecionada
        File currentFile = getCurrentFileForSelectedTab(); // Obtém o arquivo atual associado à aba

        if (currentFile == null || forceChoose) {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) return;
            currentFile = chooser.getSelectedFile();
            setCurrentFileForSelectedTab(currentFile); // Associa o novo arquivo à aba selecionada
        }

        try {
            Files.writeString(currentFile.toPath(), currentArea.getText());
        } catch (IOException e) {
            showError("Failed to save file: " + e.getMessage());
        }
    }
    
    public String getArea1FilePath() {
        File file = currentFiles.get(textArea);
        if (file != null) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    public String getArea2FilePath() {
        File file = currentFiles.get(textArea2);
        if (file != null) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    //Listener para detectar quando a aba é trocada e atualizar o título da aba
    private void setupTabChangeListener() {
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Quando uma aba é trocada, atualiza o título para refletir o arquivo atual, se houver
                int selectedIndex = tabbedPane.getSelectedIndex();
                JTextArea currentArea = (JTextArea) ((JScrollPane) tabbedPane.getComponentAt(selectedIndex)).getViewport().getView();
                File file = currentFiles.get(currentArea);
                if (file != null) {
                    tabbedPane.setTitleAt(selectedIndex, (selectedIndex == AREA1_TAB_INDEX ? "Área1: " : "Área2: ") + file.getName());
                } else {
                    tabbedPane.setTitleAt(selectedIndex, (selectedIndex == AREA1_TAB_INDEX ? "Área1" : "Área2"));
                }
            }
        });
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}