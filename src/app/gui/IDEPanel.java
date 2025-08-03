package app.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File; // Import File
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import app.gui.components.ControlsPanel;
import app.gui.components.EditorPanel;
import app.gui.components.IOConsolePanel;
import app.gui.components.MemoryPanel;
import app.gui.components.RegistersPanel;
import app.gui.components.StackPanel;
import app.toolchain.Toolchain;
import app.toolchain.vm.VirtualMachine;

public class IDEPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private final Toolchain toolchain;
	private final VirtualMachine vm;
	private final MemoryPanel memoryPanel;
	private final StackPanel stackPanel;
    private final EditorPanel editorPanel;
    private final IOConsolePanel ioConsolePanel;
    private final ControlsPanel controlsPanel;
	private final RegistersPanel registersPanel;
	
	 private final List<String> sourceFiles = new ArrayList<>(); 

    private Timer clockTimer;
    private boolean clockRunning = false;

    public IDEPanel(Toolchain toolchain) {
        super(new GridBagLayout());
        this.toolchain = toolchain;
        this.vm = toolchain.getVM();

        this.memoryPanel = new MemoryPanel();
        this.stackPanel = new StackPanel();

        this.editorPanel = new EditorPanel();
        this.ioConsolePanel = new IOConsolePanel();
        
        this.controlsPanel = new ControlsPanel();
        this.registersPanel = new RegistersPanel();
        
        setupLayout();
        setupListeners();
	    vm.printOutput("Output test succefull!");
	    vm.notifyProgramFinished(); // Should show in console
    }

    private void setupLayout() {
        setBackground(Theme.BACKGROUND);
        setForeground(Theme.FOREGROUND);
    	
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
	    col3.add(stackPanel, gbc3);

	    add(col3, gbc);
    }
    
    private void setupListeners() {
    	toolchain.setOnStep(() -> {
    	    memoryPanel.refresh(vm);
    	    registersPanel.refresh(vm);
    	    stackPanel.refresh(vm);
    	    controlsPanel.setNextInstruction(vm.peekNextInstruction());
    	});
    	
        vm.setOnFinish(() -> {
        	ioConsolePanel.appendOutput("Program Finished!");
        	controlsPanel.disableExecutionButtons();
        });

    	vm.setOutputConsumer(text -> {
    	    SwingUtilities.invokeLater(() -> {
    	        ioConsolePanel.appendOutput(text); 
    	    });
    	});
    	
    	controlsPanel.getBuildButton().addActionListener(e -> {
            editorPanel.saveFile(false); 
    		
            sourceFiles.clear();	
    	    ioConsolePanel.clear();
    	    try {
                String area1Path = editorPanel.getArea1FilePath(); 
                if (area1Path != null && new File(area1Path).exists()) { //verifica a existência usando um novo File
                    sourceFiles.add(area1Path);
                    ioConsolePanel.appendOutput("Adicionado arquivo da Área1: " + new File(area1Path).getName());
                } else {
                    ioConsolePanel.appendOutput("AVISO: Nenhum arquivo carregado/salvo na Área1.");
                }

                String area2Path = editorPanel.getArea2FilePath(); 
                if (area2Path != null && new File(area2Path).exists()) {
                    sourceFiles.add(area2Path);
                    ioConsolePanel.appendOutput("Adicionado arquivo da Área2: " + new File(area2Path).getName());
                } else {
                    ioConsolePanel.appendOutput("AVISO: Nenhum arquivo carregado/salvo na Área2.");
                }
                if (sourceFiles.isEmpty()) {
                    ioConsolePanel.appendOutput("Nenhum arquivo de entrada válido para build.");
                    return;
                }
                toolchain.prepare(sourceFiles);
                ioConsolePanel.appendOutput("Build was succefull.");
                controlsPanel.enableExecutionButtons();
    	    } catch (Exception ex) {
    	        ioConsolePanel.appendOutput("Build failed: " + ex.getMessage());
    	        ex.printStackTrace();
    	    }
    	});

    	controlsPanel.getQuickRunButton().addActionListener(e -> {
    	    stopClock();
    	    vm.setMop(0);
    	    new Thread(() -> toolchain.runFast()).start(); // runFast won't freeze GUI
    	});
    	
    	controlsPanel.getRunButton().addActionListener(e -> {
    	    stopClock();
    	    vm.setMop(1);
    	    startClock(1000); // 1 second between ticks (1 Hz)
    	});

    	controlsPanel.getStepButton().addActionListener(e -> {
    	    stopClock();
    	    vm.setMop(2);
    	    toolchain.tick();
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
    
    private void startClock(int delayMs) {
        if (clockRunning || vm.isHalted()) return;

        clockTimer = new Timer(delayMs, e -> {
            toolchain.tick(); // Toolchain handles one instruction + GUI update via onStep
            if (vm.isHalted() || vm.getMop() != 1) {
                stopClock();
            }
        });
        clockTimer.start();
        clockRunning = true;
    }

    private void stopClock() {
        if (clockTimer != null) {
            clockTimer.stop();
            clockTimer = null;
        }
        clockRunning = false;
    }
}