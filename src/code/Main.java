package code;

public class Main {
    public static void main(String[] args) {


        Decoder.obj.decoder.decodeString(".\\res\\Sim2.txt");


        /*
        System.out.println(Integer.toBinaryString(0x9A));
        System.out.println("00" + Integer.toBinaryString(0x35));
        System.out.println(Integer.toBinaryString(ALU.or(0x9A, 0x35)));
        System.out.println(String.format("0x%04X",ALU.or(0x9A, 0x35)));
        */




        /*
        for (Integer integer : ProgrammMemory.memory) {
            System.out.println(String.format("0x%04X",integer));
        }
        */

        System.out.println("Main decoding done");
	}


    /**
     * TODO refresh if something is added
     * resets the memory to the original
     */
    public static void resetMemory() {
        Ram ram = new Ram();
        //ProgrammMemory.memory = new Integer[1024];
        //ProgramMemory.memory = new ArrayList<>();
        Ram.bank = 0;
        Ram.wRegister = 0;
        //Stack.stack = new Integer[8];
        Stack.pointer = 0;

    }
}
