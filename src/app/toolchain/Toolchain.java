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
    private Assembler assembler;
    private Linker linker;
    private Loader loader;
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
        this.tables = new Tables();
        this.loader = new Loader(tables);
        this.vm = new VirtualMachine();
        this.vmDebugMode = false;
        this.macroProcessorDebugMode = false;
        this.assemblerDebugMode = false;
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

            // Strip MASMAPRG_ prefix
            baseName = baseName.substring("MASMAPRG_".length());
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
        Integer finalStackSize = linker.link(objFiles, tables, true, 0, hpxOut);
    
        // 4. Load into VM
        loader.load(vm, hpxOut, finalStackSize);
        updateGUI();
    }
    
    private void reset() {
    	vm.reset();
    	this.tables = new Tables();
    	this.loader = new Loader(tables);
    	this.assembler = new Assembler();
    	this.linker = new Linker();
	  }

    // Test Method for assembler and linker
    public void testAssemblerLinker()
    {
	    List<String> modules = new ArrayList<>(2);
	    modules.add("prog");
	    modules.add("math");
	    
	    System.out.println("Assembling...");
	    for (String module : modules)
	    {
		    try {
				this.assembler.assemble(module + ".txt", "files/object", "files/list", tables);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    System.out.println("\nLinking...");
		this.linker.link(modules, this.tables, true, 0, modules.get(0));
    }
    
    public void setOnStep(Runnable r) {
        this.onStep = r;
    }
    
    private String removeExtension(String filename) {
	    int dotIndex = filename.lastIndexOf('.');
	    if (dotIndex >= 0) return filename.substring(0, dotIndex);
	    return filename;
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
    public Linker getLinker() { return linker; }
}
