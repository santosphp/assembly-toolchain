package toolchain.main;

import toolchain.gui.MainFrame;
import toolchain.vm.VirtualMachine;

public class Main {

	public static void main(String[] args) {        
        VirtualMachine vm = new VirtualMachine();
        MainFrame gui = new MainFrame(vm);
        vm.setMop(0);  // CLI mode
        
        vm.setMop(1); // Run mode
        if (vm.getMop() != 0)
        	gui.setVisible(true);
        else
            System.out.println("AssemblyToolchain is running on CLI mode!");
	}

}
