# Sistema de Programação para um Computador Hipotético

## Tecnologias Utilizadas
- Linguagem: Java
- Plataforma: Linux/Windows
- GUI: Swing

## Fluxo de Operação
O programa pode ser executado em dois modos principais:
- GUI: `java -jar AssemblyToolchain.jar`
- CLI: `java -jar AssemblyToolchain.jar input1.asm input2.asm`
```
public static void main(String[] args) {
    // Initialize modules
    Assembler assembler = new Assembler();
    MacroProcessor macroProc = new MacroProcessor();
    Linker linker = new Linker();
    Loader loader = new Loader();
    VirtualMachine vm = new VirtualMachine();
    CLITool cli = new CLITool(vm, assembler, macroProc, linker, loader);

    if (args.length == 0) {
        // No arguments -> launch GUI mode
        vm.setMop(1);  // GUI mode
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame(vm).setVisible(true);
        });
    } else {
        // Arguments present -> CLI mode
        vm.setMop(0);  // CLI mode
        cli.runCLI(args);  // pass all CLI args, e.g., source files
    }
}
```
**Nota:** A interface CLI poderá futuramente suportar flags para executar apenas partes específicas do processo, como -a (assembler), -L (linker), etc. 

