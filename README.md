# Sistema de Programação para um Computador Hipotético

## Tecnologias Utilizadas
- Linguagem: Java
- Plataforma: Linux/Windows
- GUI: Swing

## Execução
O programa pode ser executado a partir do seguinte comando:
- `java -jar AssemblyToolchain.jar`

## Fluxo de Operação
### `Main`: 
```
public static void main(String[] args) {
    Toolchain toolchain = new Toolchain();
    javax.swing.SwingUtilities.invokeLater(() -> {
        new MainFrame(toolchain).setVisible(true);
    });
}
```
1. Cria uma `Toolchain`, um conjunto com todas ferramentas para o sistema de programação.
2. Cria e abre a GUI (`MainFrame`), que recebe uma referência de `toolchain` para prover métodos de controle ao usuário.

### Em `Toolchain` temos a preparação para a execução:
```
public class Toolchain {
    private final MacroProcessor macroProcessor;
    private final Assembler assembler;
    private final Linker linker;
    private final Loader loader;
    private final VirtualMachine vm;

    public Toolchain() {
        this.macroProcessor = new MacroProcessor();
        this.assembler = new Assembler();
        this.linker = new Linker();
        this.loader = new Loader();
        this.vm = new VirtualMachine();
    }
  
    public void prepare(String<> sourceFiles) {
        // Process macros -> assembles -> links -> loads
    }
    
    // Getters and setters
    public VirtualMachine getVM() { return vm; }
    // ...
```
O método `prepare()` realiza toda a cadeia de preparação, usando arquivos reais no disco.
Ele processa cada arquivo `.ASM`  através do processador de  macros e montador, gerantdo arquvios `.OBJ` e `.LST`.
Em seguida, o ligador recbe os arquivos `.OBJ` e produz um único executável `.HPX`, que é então passado ao carregador.

Todo este processo é acionado pela GUI, após o usuário escolher os arquivos-fonte.

### Ainda em Toolchain temos os modos de execução:
```
    // Mop == 00 || Mop == 02
	  public void runFast() {
	  	while (!vm.isHalted() && vm.getMop() == 0) {
              vm.step();
          }
	  }

    // Mop == 01
	  public void tick() {
	      if (vm.isHalted()) return;
          vm.step();
          updateGUI();
	  }
}
```

Após `prepare()`, o sistema pode executar o programa por três modos distintos:

- runFast() -> modo de execução contínuo, sem interação com a interface visual.
- tick() → executa uma instrução por chamada (modo manual ou clock) e interage com a interface visual.

A GUI decide quando e como cada modo é acionado, de acordo com as opções do usuário.

### Atualização da Interface Gráfica

Em `IDEPanel`, no método `setupListeners()` chamado de seu construtor, temos:
```
toolchain.setOnStep(() -> {
    memoryPanel.refresh(vm);
    registersPanel.refresh(vm);
    controlsPanel.setNextInstruction(vm.peekNextInstruction());
});
```
Essa função registra um _callback que será executado a cada ciclo de instrução. A `Toolchain` invoca esse callback automaticamente através do método `updateGUI()` ao final de cada `tick()`:
```
public void setOnStep(Runnable r) {
        this.onStep = r;
    }
    
    private void updateGUI() {
        if (onStep != null) onStep.run();
	}
```
