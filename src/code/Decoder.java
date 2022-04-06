package code;

import java.util.ArrayList;



public class Decoder {
    public static AllObjects obj = AllObjects.getAllObjectsInstance();

    ArrayList<Integer> opCodeList = LSTFileReader.getOpcode();
    ArrayList<Integer> opVal = LSTFileReader.getOperationValue();
    ArrayList<Integer> decodeList = LSTFileReader.getDecodeList();

    public int decodeString(String src) {
        /**
         * initialized ram
         */

        System.out.println(Integer.toBinaryString(obj.ram.getStatus()));
        //System.out.println(obj.ram.getSpecificBit(3));
        //System.out.println(obj.ram.getSpecificBit(4));
        //System.out.println(obj.ram.getSpecificBit(5));


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




        int iHexOpcode = opCodeList.get(i);

        int iOpValue = opVal.get(i);

        //System.out.println(String.format("0x%02X",iOpValue));
        obj.stack.pushOnStack(decodeList.get(i));

        obj.mainFrame.updateStack(obj.stack.getStack());

        switch (iHexOpcode) {
            case 0b00110000 -> movLW(iOpValue);
            case 0b00111001 -> andLW(iOpValue);
            case 0b00111000 -> iorLW(iOpValue);
            case 0b00111100 -> subLW(iOpValue);
            case 0b00111010 -> xorLW(iOpValue);
            case 0b00111110 -> addLW(iOpValue);
            case 0b00101000 -> goTO(iOpValue);

        }
    }

    /**
     * moves 8 bit literal to W register
     */
    public void movLW(Integer i) {

        Ram.wRegister = i;
        System.out.println("movlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * W register is anded with the 8 bit literal
     * result ist stored in W register
     * TODO fix ALU and and use here
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
     * @param i 8 bit literal
     */
    public void iorLW(Integer i) {

        Ram.wRegister = obj.alu.or(Ram.wRegister, i);
        if (Ram.wRegister == 0) {

            obj.ram.setStatus(obj.ram.setZeroBit(true));
        }
        System.out.println("iorlw wRegister: "+ String.format("0x%02X", Ram.wRegister));
    }

    /**
     * he contents of W register is subtracted (2’s complement method) from the eight bit literal 'i'.
     * The result is placed in the W register.
     * @param i 8 bit literal
     *
     * TODO when do i have to set the DC bit ?
     */
    public void subLW(Integer i) {

        Ram.wRegister = i - Ram.wRegister;
        if (Ram.wRegister > 0) {
            obj.ram.setStatus(obj.ram.setCarryBit(true));

        } else if (Ram.wRegister == 0) {
            obj.ram.setStatus(obj.ram.setCarryBit(true));
            obj.ram.setStatus(obj.ram.setZeroBit(true));


        } else if (Ram.wRegister < 0) {
            /**
             * TODO subtraction with complement right?
             */
            Ram.wRegister = 256 + Ram.wRegister;

        }

        System.out.println("sublw wRegister: "+ String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of the W register are
     * XOR’ed with the eight bit literal 'i'.
     * The result is placed in the W register.
     * TODO fix c dc and z bit
     * @param i 8 bit literal
     */
    public void xorLW(Integer i) {
        Ram.wRegister = obj.alu.xor(Ram.wRegister, i);

        if (Ram.wRegister == 0) {
            obj.ram.setStatus(obj.ram.setZeroBit(true));

        }

        //System.out.println(Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("xorlw wRegister: "+ String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of the W register are
     * added to the eight bit literal ’i’ and the
     * result is placed in the W register
     * TODO fix c dc and z bit
     * @param i 8 bit literal
     */
    public void addLW(Integer i) {
        Ram.wRegister += i;
        //System.out.println(Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("addlw wRegister: "+ String.format("0x%02X", Ram.wRegister));
    }

    /**
     *
     * @param i number of the the next code segment
     */
    public void goTO(Integer i) {
        /**
         * loop to create artificial endless loop
         * TODO increase stackoverflow amount to wanted number
         */
        if (++ProgrammMemory.stopStackoverflow != 1) {
            functionCalls(i);
        }

        System.out.println("goto");

    }

}
