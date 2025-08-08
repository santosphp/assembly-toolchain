package app.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable; 
import javax.swing.table.DefaultTableModel; 
import app.gui.Theme;
import app.toolchain.vm.VirtualMachine;
import app.toolchain.vm.Memory;

public class MemoryPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable memoryTable; // a tabela visualizável pelo usuário
    private DefaultTableModel tableModel; // O modelo de dados para a tabela, ou seja, a interface dos processos internos do swing

    @SuppressWarnings("serial")
	public MemoryPanel() {
        setLayout(new BorderLayout());
        setBorder(Theme.createTitledBorder("Memory")); 
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
        setMinimumSize(new Dimension(200, 200));

        tableModel = new DefaultTableModel(new Object[]{"Address", "Value"}, 0) {	
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        memoryTable = new JTable(tableModel);
        styleMemoryTable(memoryTable);

        JScrollPane scrollPane = new JScrollPane(memoryTable);
        scrollPane.setBorder(null); 
        
        scrollPane.setBackground(Theme.BACKGROUND);
        scrollPane.getViewport().setBackground(Theme.BACKGROUND);
        
        add(scrollPane, BorderLayout.CENTER); 
    }

    private void styleMemoryTable(JTable table) {
        table.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
        table.setBackground(Theme.BACKGROUND);
        table.setForeground(Theme.FOREGROUND);
        table.setSelectionBackground(Theme.FOREGROUND); 
        table.setSelectionForeground(Theme.BACKGROUND);

        table.getTableHeader().setBackground(Theme.BACKGROUND.darker()); 
        table.getTableHeader().setForeground(Theme.FOREGROUND);
        table.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 12));
        table.getTableHeader().setReorderingAllowed(false); 
        table.getTableHeader().setResizingAllowed(false);   

        table.getColumnModel().getColumn(0).setPreferredWidth(80); 
        table.getColumnModel().getColumn(1).setPreferredWidth(100); 
        
        table.setShowGrid(false); 
        table.setIntercellSpacing(new java.awt.Dimension(0, 0)); //remove espaçamento entre células
    }

    public void refresh(VirtualMachine vm) {
        tableModel.setRowCount(0);    // limpa o modelo da tabela
        
        Memory memory = vm.getCpu().getMemory();
        if (memory == null) {
            System.err.println("Erro: Instância de Memory é nula na CPU.");
            return;
        }
        int memorySize = memory.getSize(); // Obtém o tamanho total da memória (1024 atualmnte)
        
        for (int i = 0; i < memorySize; i++) {
            int value = memory.read(i);
        	
            String addressDec = String.valueOf(i);
            String valueDec = String.valueOf(value);
            
            tableModel.addRow(new Object[]{addressDec, valueDec});
        }
    }
}