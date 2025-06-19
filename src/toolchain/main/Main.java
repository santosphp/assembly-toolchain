package toolchain.main;

import toolchain.vm.VirtualMachine;

public class Main {

	public static void main(String[] args) {
        System.out.println("AssemblyToolchain is running!");
        
        VirtualMachine virtualMachine = new VirtualMachine(0, "Teste...");
        
        System.out.println(virtualMachine.getCpu().getRegistersState());
        

        virtualMachine.loadFromFile("Teste");
        
        virtualMachine.startSimulation();
        
	}

}
