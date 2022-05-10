package code;

public class Main {
    public static void main(String[] args) {


        Decoder.obj.decoder.decodeString(".\\res\\Sim7.txt");

        /*
        System.out.println(Integer.toBinaryString(0x9A));
        System.out.println("00" + Integer.toBinaryString(0x35));
        System.out.println(Integer.toBinaryString(ALU.or(0x9A, 0x35)));
        System.out.println(String.format("0x%04X",ALU.or(0x9A, 0x35)));
        */


        System.out.println("Main decoding done");
	}


}
