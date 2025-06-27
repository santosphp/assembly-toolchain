package toolchain.gui.tabs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import toolchain.gui.components.ControlsPanel;
import toolchain.gui.components.EditorPanel;
import toolchain.gui.components.IOConsolePanel;
import toolchain.gui.components.MemoryPanel;
import toolchain.gui.components.RegistersPanel;
import toolchain.gui.components.StackPanel;
import toolchain.vm.VirtualMachine;
import toolchain.vm.vmlistener.VMListener;

public class MachinePanel extends JPanel implements VMListener {
	
	private static final long serialVersionUID = 1L;
	private final VirtualMachine vm;
	private final MemoryPanel memoryPanel;
    private final EditorPanel editorPanel;
    private final IOConsolePanel ioConsolePanel;
    private final ControlsPanel controlsPanel;
	private final RegistersPanel registersPanel;

    public MachinePanel(VirtualMachine vm) {
        super(new GridBagLayout());
        this.vm = vm;
        this.vm.setListener(this);

        this.memoryPanel = new MemoryPanel();

        this.editorPanel = new EditorPanel();
        this.ioConsolePanel = new IOConsolePanel();
        
        this.controlsPanel = new ControlsPanel();
        this.registersPanel = new RegistersPanel();
        
        setupLayout();
        setupListeners();
    }

    private void setupLayout() {
        GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5); // Puts 5px margins all around each column
	    gbc.fill = GridBagConstraints.BOTH;

	    // Column 1: Memory (20% width) 
	    gbc.gridx = 0;
	    gbc.weightx = 0.2;
	    gbc.weighty = 1.0;
	    add(memoryPanel, gbc);

	    // Column 2: Editor and I/O (40% width)
	    gbc.gridx = 1;
	    gbc.weightx = 0.4;
	    JPanel col2 = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc2 = new GridBagConstraints();
	    gbc2.fill = GridBagConstraints.BOTH;
	    gbc2.weightx = 1.0;  // Necessary for components to fill up the column horizontally
	    gbc2.insets = new Insets(0, 0, 0, 0); // No margins in between components

	    gbc2.gridy = 0;
	    gbc2.weighty = 0.7;  // 70%
	    col2.add(editorPanel, gbc2);

	    gbc2.gridy = 2;
	    gbc2.weighty = 0.3;  // 30%
	    col2.add(ioConsolePanel, gbc2);

	    add(col2, gbc);

	    // Column 3: Control panel, Registers and Stack  (30% width)
	    gbc.gridx = 2;
	    gbc.weightx = 0.3;
	    JPanel col3 = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc3 = new GridBagConstraints();
	    gbc3.fill = GridBagConstraints.BOTH;
	    gbc3.weightx = 1.0;  // Necessary for components to fill up the column horizontally
	    gbc3.insets = new Insets(0, 0, 0, 0); // No margins in between components

	    gbc3.gridy = 0;
	    gbc3.weighty = 0.15;  // 15%
	    col3.add(controlsPanel, gbc3);

	    gbc3.gridy = 1;
	    gbc3.weighty = 0.3;  // 30%
	    col3.add(registersPanel, gbc3);

	    gbc3.gridy = 2;
	    gbc3.weighty = 0.55;  // 55%
	    col3.add(new StackPanel(), gbc3);

	    add(col3, gbc);
    }

    private List<Integer> parseBinary(String code) {
        String bits = code.replaceAll("[^01]", "");  // Remove all non-binary chars (spaces, newlines, etc.)

        if (bits.length() % 16 != 0) {
            throw new IllegalArgumentException("Input is not a multiple of 16 bits.");
        }

        List<Integer> words = new ArrayList<>();
        for (int i = 0; i < bits.length(); i += 16) {
            String word = bits.substring(i, i + 16);
            words.add(Integer.parseUnsignedInt(word, 2));  // Base 2 to base 10 (integer)
        }

        return words;
    }

    
    private void setupListeners() {
    	controlsPanel.getLoadButton().addActionListener(e -> {
    	    String binaryCode = editorPanel.getCode();
    	    List<Integer> words = parseBinary(binaryCode);
    	    vm.setProgramData(words);
    	});

    	controlsPanel.getRunButton().addActionListener(e -> {
    	    vm.setMop(1);
    	    vm.run();
    	});

    	controlsPanel.getStepButton().addActionListener(e -> {
    	    vm.setMop(2);
    	    vm.step();
    	});
    	
    	ioConsolePanel.setOnInputSubmitted(input -> {
    	    try {
    	    	int value = Integer.parseInt(input);
    	        vm.pushInput(value);
    	        ioConsolePanel.appendOutput("> " + input);
    	    } catch (NumberFormatException ex) {
    	        ioConsolePanel.appendOutput("Invalid input: " + input);
    	    }
    	});

    }

	@Override
	public void onCycleCompleted() {
        memoryPanel.refresh(vm);
        registersPanel.refresh(vm);
        controlsPanel.setNextInstruction(vm.peekNextInstruction());
	}

	@Override
	public void onProgramFinished() {
		ioConsolePanel.appendOutput("Program finished!");		
	}

	@Override
	public void onProgramDataInitialized() {
		ioConsolePanel.clear();
		ioConsolePanel.appendOutput("Program loaded!");
	}

	@Override
	public void onOutputProduced(String message) {
		ioConsolePanel.appendOutput(message);
	}
}
