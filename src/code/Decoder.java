package code;

import java.util.ArrayList;

public class Decoder {

    public static int decodeString(String src) {




        //LSTFileReader file = new LSTFileReader();
        try {
            LSTFileReader.read(src);
        } catch (Exception e) {
            //System.out.println(e);
            e.printStackTrace();
        }




        int iSizeOfDecodedList = LSTFileReader.getDecodeList().size();

        for (int i = 0; i < iSizeOfDecodedList; i++) {


            functionCalls(i);

            //System.out.println(String.format("0x%02X",Memory.stack[i]));
        }


        /**
         * initializes memory to remember the programCounter
         */
        var decodeList = LSTFileReader.getDecodeList();
        for (Integer integer : decodeList) {

            int i = 0;
            //ProgrammMemory.memory[i] = integer;
            ProgrammMemory.memory.add(integer);
            i++;
        }




        //System.out.println(iSizeOfDecodedList);

        return 1;
    }

    /**
     * makes the function calls
     */
    public static void functionCalls(Integer i) {
        ArrayList<Integer> opCodeList = LSTFileReader.getOpcode();
        ArrayList<Integer> opVal = LSTFileReader.getOperationValue();

        int iOpCode = LSTFileReader.getOpcode(i);

        int iHexOpcode = opCodeList.get(i);

        int iOpValue = opVal.get(i);

        //System.out.println(String.format("0x%02X",iOpValue));
        Stack.pushOnStack(iOpCode);
        switch (iHexOpcode) {
            case 0b00110000 -> movLW(iOpValue);
            case 0b00111001 -> andLW(iOpValue);
            case 0b00111000 -> iorLW(iOpValue);
            case 0b00111100 -> subLW();
            case 0b00111010 -> xorLW();
            case 0b00111110 -> addLW(iOpValue);
            case 0b00101000 -> goTO(iOpValue);

        }
    }

    /**
     * moves 8 bit literal to W register
     */
    public static void movLW(Integer i) {
        System.out.println("movlw wRegister: " + String.format("0x%02X", Ram.wRegister));
        Ram.wRegister = i;
    }

    /**
     * W register is anded with the 8 bit literal
     * result ist stored in W register
     *
     * @param i 8 bit literal
     */
    public static void andLW(Integer i) {
        Ram.wRegister = ALU.and(Ram.wRegister, i);
        System.out.println("andlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of the W register is
     * OR’ed with the eight bit literal 'i'. The
     * result is placed in the W register.
     * @param i
     */
    public static void iorLW(Integer i) {
        System.out.println("iorlw");
    }

    public static void subLW() {
        System.out.println("sublw");
    }

    public static void xorLW() {

        System.out.println("xorlw");
    }

    /**
     * The contents of the W register are
     * added to the eight bit literal ’i’ and the
     * result is placed in the W register
     *
     * @param i
     */
    public static void addLW(Integer i) {
        Ram.wRegister += i;
        System.out.println("addlw");
    }

    public static void goTO(Integer i) {

        ProgrammMemory.stopStackoverflow++;


        /**
         * loop to create artificial endless loop
         */
        if (ProgrammMemory.stopStackoverflow != 10) {
            functionCalls(i);
        }
        System.out.println("goto");

    }

}
