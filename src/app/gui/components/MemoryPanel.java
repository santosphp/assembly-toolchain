package app.gui.components;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable; 
import javax.swing.table.DefaultTableModel; 
import app.gui.Theme;
import app.toolchain.vm.VirtualMachine; 

public class MemoryPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable memoryTable; // a tabela visualizável pelo usuário
    private DefaultTableModel tableModel; // O modelo de dados para a tabela, ou seja, a interface dos processos internos do swing

    public MemoryPanel() {
        setLayout(new BorderLayout());
        setBorder(Theme.createTitledBorder("Memory")); 
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);

        tableModel = new DefaultTableModel(new Object[]{"Address", "Value"}, 0) {	
            @Override
            public boolean isCellEditable(int row, int column) {	// impede a edição direta dos nomes da tabela
                return false;
            }
        };

        memoryTable = new JTable(tableModel);
        styleMemoryTable(memoryTable);

        JScrollPane scrollPane = new JScrollPane(memoryTable);	// barra de rolage para não ultrapassar limite de espaço
        scrollPane.setBorder(null); 
        
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
        table.setIntercellSpacing(new java.awt.Dimension(0, 0)); // remove espaçamento entre células
    }

    public void refresh(VirtualMachine vm) {
        tableModel.setRowCount(0);    // limpa o modelo da tabela

        //app.toolchain.vm.Memory memory = vm.getMemory(); // assumindo vm.getMemory() retorna a instância de Memory
        //int memorySize = memory.getSize(); // obtém o tamanho total da memória (1024)
        
        int memorySize = 1024;		//temporário até termos os métodos

        for (int i = 0; i < memorySize; i++) {		//preenche a tabela
            //int value = memory.read(i); // Lê o valor de cada posição
        	int value = memorySize + i;		//temporário até termos os métodos
        	
            String addressHex = String.format("%04X", i); // endereço em 4 dígitos hexadecimais
            String valueDec = String.valueOf(value);     // valor em decimal
            //String valueHex = String.format("%04X", value & 0xFFFF); // valor em 4 hexadecimais (considerando 16 bits)

            tableModel.addRow(new Object[]{addressHex, valueDec});		//valuedec pode ser trocado por valueHex
        }
    }
}