package app.toolchain;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import app.toolchain.assembler.Assembler;
import app.toolchain.linker.Linker;
import app.toolchain.loader.Loader;
import app.toolchain.macro.MacroProcessor;
import app.toolchain.vm.VirtualMachine;

public class Toolchain {
    private final MacroProcessor macroProcessor;
    private final Assembler assembler;
    private final Linker linker;
    private final Loader loader;
    private final VirtualMachine vm;
    
    private Tables tables;

    private Runnable onStep;
    private final Boolean vmDebugMode;
    private final Boolean macroProcessorDebugMode;
    private final Boolean assemblerDebugMode;

    
    public Toolchain() {
        this.macroProcessor = new MacroProcessor();
        this.assembler = new Assembler();
        this.linker = new Linker();
        this.loader = new Loader();
        this.vm = new VirtualMachine();
        this.tables = new Tables();
        this.vmDebugMode = false;
        this.macroProcessorDebugMode = false;
        this.assemblerDebugMode = true;
    }

    public void prepare(List<String> sourceFileNames) {
    	reset();
    	
        // Test only the VM with .HPX files
        if (vmDebugMode) {
            vm.loadFromFile(sourceFileNames.getFirst());
            updateGUI();
            return;
        }

        // 1. Process macros
        List<String> macroOutputs = new ArrayList<>();
        for (String file : sourceFileNames) {
        	File f = new File(file);
        	String parent = f.getParent();
        	String name = f.getName();
            String macroOutPath = parent + File.separator + "MASMAPRG_" + name;
            macroProcessor.processFile(file, macroOutPath);
            macroOutputs.add(macroOutPath);
        }
        if (macroProcessorDebugMode) return;
        
        // 2. Assemble
        tables.cleanTables(); // Clean previous tables
        List<String> objFiles = new ArrayList<>();

        for (String macroFile : macroOutputs) {
            File mf = new File(macroFile).getAbsoluteFile();
            String baseName = removeExtension(mf.getName());
            
            File objFile = new File(mf.getParentFile(), baseName + ".OBJ");
            File lstFile = new File(mf.getParentFile(), baseName + ".LST");

            try {
                if (!assembler.assemble(mf.getPath(), objFile.getPath(), lstFile.getPath(), tables)) {
                    System.out.println("Assembler ended with ERROR!!!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            objFiles.add(objFile.getPath());
        }
        if (assemblerDebugMode) return;
    
        // 3. Link
        String finalName = removeExtension(sourceFileNames.getFirst());
        String hpxOut = finalName + ".HPX";
        linker.link(objFiles, hpxOut);
    
        // 4. Load into VM
        loader.load(vm, hpxOut);        
    }
    
    private void reset() {
    	vm.reset();
	}

	public void setOnStep(Runnable r) {
        this.onStep = r;
    }
    
    private String removeExtension(String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	public void runFast() {
		while (!vm.isHalted() && vm.getMop() == 0) {
            vm.step();
        }
        updateGUI();
	}

	public void tick() {
		// System.out.println("VM is halted: " + vm.isHalted());
	    if (vm.isHalted()) return; // early exit if already halted
        vm.step();
        updateGUI();
	}
	
	private void updateGUI() {
        if (onStep != null) onStep.run();
	}
	
	// Getters and setters
    public VirtualMachine getVM() { return vm; }
    public Assembler getAssembler() { return assembler; }
}
