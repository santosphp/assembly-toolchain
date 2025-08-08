package app.gui.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import app.gui.Theme;
import app.toolchain.vm.Memory;
import app.toolchain.vm.VirtualMachine; 

public class StackPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable stackTable;
    private DefaultTableModel tableModel;

    @SuppressWarnings("serial")
	public StackPanel() {
        setLayout(new BorderLayout());
        setBorder(Theme.createTitledBorder("Stack")); 
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);

        tableModel = new DefaultTableModel(new Object[]{"Address", "Value"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {	//impede edição direta
                return false;
            }
        };

        stackTable = new JTable(tableModel);
        styleStackTable(stackTable); 

        JScrollPane scrollPane = new JScrollPane(stackTable);
        scrollPane.setBorder(null); 
        
        scrollPane.setBackground(Theme.BACKGROUND);
        scrollPane.getViewport().setBackground(Theme.BACKGROUND);
        
        add(scrollPane, BorderLayout.CENTER); //adiciona ao painel
    }

    private void styleStackTable(JTable table) {
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
        table.setIntercellSpacing(new java.awt.Dimension(0, 0));
    }
    
    public void refresh(VirtualMachine vm) {
        tableModel.setRowCount(0);

        Memory memory = vm.getCpu().getMemory();
        List<Integer> stack = memory.getStackContents();

        for (int i = 0; i < stack.size(); ++i) {
        	// SP-00 is the top, i = stack.size() - 1 is the base
        	String addressDisplay = i == stack.size() - 1
			    ? String.format("SP-%02d (base)", i)
			    : String.format("SP-%02d", i);
            String valueDisplay = String.valueOf(stack.get(i));
            tableModel.addRow(new Object[]{addressDisplay, valueDisplay});
        }

    }

}
