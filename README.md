# Sistema de Programação para um Computador Hipotético
## Requisitos e Execução
- Tecnologias: Java 21, Swing
- Plataforma: Linux/Windows
- Para executar: `java -jar AssemblyToolchain.jar`

## Estrutura
```bash
src/app/
├── gui
│   ├── components
│   │   ├── ControlsPanel.java
│   │   ├── EditorPanel.java
│   │   ├── IOConsolePanel.java
│   │   ├── MemoryPanel.java
│   │   ├── RegistersPanel.java
│   │   └── StackPanel.java
│   ├── IDEPanel.java
│   ├── MainFrame.java
│   └── Theme.java
├── main
│   └── Main.java
└── toolchain
    ├── assembler
    │   └── Assembler.java
    ├── linker
    │   └── Linker.java
    ├── loader
    │   └── Loader.java
    ├── macro
    │   └── MacroProcessor.java
    ├── Toolchain.java
    └── vm
        ├── CPU.java
        └── VirtualMachine.java
```

## Sumário
1. [Ponto de partida](#start)
2. [Modos de Execução](#executionModes)
3. [Atualização da Interface](#guiUpdate)
4. [I/O entre GUI e VM](#guiVM)
5. [Gerência de Arquivos](#files)
6. [Documentação Detalhada](#docs)


### 1. Ponto de Partida <a name="start"></a>
#### `Main`: 
```java
public static void main(String[] args) {
    Toolchain toolchain = new Toolchain();
    javax.swing.SwingUtilities.invokeLater(() -> {
        new MainFrame(toolchain).setVisible(true);
    });
}
```
1. Cria uma `Toolchain`, um conjunto com todas as ferramentas para o sistema de programação.
2. Cria e abre a GUI (`MainFrame`), que recebe uma referência de `toolchain` para prover métodos de controle ao usuário.

Em `Toolchain` temos a preparação para a execução:
```java
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
Ele processa cada arquivo `.ASM`  através do processador de  macros e montador, gerando arquivos `.OBJ` e `.LST`.
Em seguida, o ligador recebe os arquivos `.OBJ` e produz um único executável `.HPX`, que é então passado ao carregador.

Todo este processo é acionado pelo botão `Build` da GUI, após o usuário definir os arquivos-fonte.

### 2. Modos de Execução <a name="executionModes"></a>
Ainda em Toolchain temos:
```java
// Mop == 00 
public void runFast() {
    while (!vm.isHalted() && vm.getMop() == 0) {
        vm.step();
    }
}

// Mop == 01 || Mop == 02
public void tick() {
    if (vm.isHalted()) return;
    vm.step();
    updateGUI();
}
```

Após `prepare()`, o sistema pode executar o programa por três modos distintos:

- runFast() -> modo de execução contínuo, sem interação com a interface visual.
- tick() -> executa uma instrução por chamada (modo manual ou clock) e interage com a interface visual.

A GUI decide quando e como cada modo é acionado, de acordo com as opções do usuário.

### 3. Atualização da Interface Gráfica <a name="guiUpdate"></a>
Em `IDEPanel`, no método `setupListeners()` chamado de seu construtor, temos:
```java
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

### 4. Comunicação I/O entre a GUI e a Máquina Virtual <a name="guiVM"></a>
### 5. Gerência de Arquivos <a name="files"></a>
### 6. Documentação Detalhada <a name="docs"></a>

