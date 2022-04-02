package code;
import gui.MainFrame;


import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        MainFrame myFrame = new MainFrame();

        //LSTFileReader.read(".\\res\\Sim1.txt");
        
        if ((Decoder.decodeString(".\\res\\Sim1.txt")) == 1) {
            //Stack.printStack();
            //System.out.println(Memory.wRegister);

            System.out.println("Main decoding done");
        }

        /*
        for (Integer integer : ProgrammMemory.memory) {
            System.out.println(String.format("0x%04X",integer));
        }
        */

	}


    /**
     * TODO refresh if something is added
     * resets the memory to the original
     */
    public static void resetMemory() {
        Ram ram = new Ram();
        //ProgrammMemory.memory = new Integer[1024];
        ProgrammMemory.memory = new ArrayList<>();
        Ram.bank = 0;
        Ram.wRegister = 0;
        Stack.stack = new Integer[8];
        Stack.pointer = 0;

    }
}
