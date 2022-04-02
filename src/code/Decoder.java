package code;

import java.util.ArrayList;



public class Decoder {
    AllObjects obj = new AllObjects();

    public int decodeString(String src) {
        /**
         * initialized ram
         */

        System.out.println(Integer.toBinaryString(obj.ram.getStatus()));


        //LSTFileReader file = new LSTFileReader();
        try {
            LSTFileReader.read(src);
        } catch (Exception e) {
            //System.out.println(e);
            e.printStackTrace();
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


        int iSizeOfDecodedList = LSTFileReader.getDecodeList().size();

        for (int i = 0; i < iSizeOfDecodedList; i++) {


            functionCalls(i);

            //System.out.println(String.format("0x%02X",Memory.stack[i]));
        }

        //stack.printStack();

        //System.out.println(iSizeOfDecodedList);
        return 1;
    }

    /**
     * makes the function calls
     */
    public void functionCalls(Integer i) {

        ArrayList<Integer> opCodeList = LSTFileReader.getOpcode();
        ArrayList<Integer> opVal = LSTFileReader.getOperationValue();
        ArrayList<Integer> decodeList = LSTFileReader.getDecodeList();

        int iOpCode = LSTFileReader.getOpcode(i);

        int iHexOpcode = opCodeList.get(i);

        int iOpValue = opVal.get(i);

        //System.out.println(String.format("0x%02X",iOpValue));
        obj.stack.pushOnStack(decodeList.get(i));

        obj.mainFrame.updateStack(obj.stack.getStack());

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
    public void movLW(Integer i) {
        System.out.println("movlw wRegister: " + String.format("0x%02X", Ram.wRegister));
        Ram.wRegister = i;
    }

    /**
     * W register is anded with the 8 bit literal
     * result ist stored in W register
     *
     * @param i 8 bit literal
     */
    public void andLW(Integer i) {
        Ram.wRegister = obj.alu.and(Ram.wRegister, i);
        System.out.println("andlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of the W register is
     * OR’ed with the eight bit literal 'i'. The
     * result is placed in the W register.
     *
     * @param i
     */
    public void iorLW(Integer i) {

        Ram.wRegister = obj.alu.or(Ram.wRegister, i);
        if (Ram.wRegister == 0) {
            obj.ram.setZeroBit(true);
        }
        System.out.println("iorlw");
    }

    public void subLW() {
        System.out.println("sublw");
    }

    public void xorLW() {

        System.out.println("xorlw");
    }

    /**
     * The contents of the W register are
     * added to the eight bit literal ’i’ and the
     * result is placed in the W register
     *
     * @param i
     */
    public void addLW(Integer i) {
        Ram.wRegister += i;
        System.out.println("addlw");
    }


    public void goTO(Integer i) {

        ProgrammMemory.stopStackoverflow++;


        /**
         * loop to create artificial endless loop
         * TODO increase to 10
         */
        if (ProgrammMemory.stopStackoverflow != 3) {
            functionCalls(i);
        }
        System.out.println("goto");

    }

}
