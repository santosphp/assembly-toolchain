package toolchain.main;

import java.util.Scanner;

import toolchain.gui.MainFrame;
import toolchain.vm.VirtualMachine;

public class Main {

	private static Scanner scanner;

	public static void main(String[] args) {        
        VirtualMachine vm = new VirtualMachine();
        MainFrame gui = new MainFrame(vm);
        
        vm.setMop(0);  // CLI mode
        vm.setMop(1); // Run mode
        
        if (vm.getMop() != 0) {
        	gui.setVisible(true);
        	vm.printOutput("Output test, this method should probably be private!");
        }
        else {
            System.out.println("AssemblyToolchain is running on CLI mode!");
            scanner = new Scanner(System.in);
            System.out.print("Enter an integer: ");
            int number = scanner.nextInt();
            vm.pushInput(number); // Add to the inputBuffer
            vm.printOutput(Integer.toString(vm.readInput()));
        }
	}

}
