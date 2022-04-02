package code;
import gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        gui.MainFrame myFrame = new MainFrame();
        //LSTFileReader.read(".\\res\\Sim1.txt");
        
        if ((Decoder.decodeString(".\\res\\Sim1.txt")) == 1) {
            //Memory.printStack();
            //System.out.println(Memory.wRegister);
            System.out.println("Main decoding done");
        }



	}


    /**
     * TODO refresh if something is added
     * resets the memory to the original
     */
    public static void resetMemory() {
        Ram ram = new Ram();
        ProgrammMemory.memory = new Integer[1024];
        Ram.bank = 0;
        Ram.wRegister = 0;
        Stack.stack = new Integer[8];
        Stack.pointer = 0;

    }
}
