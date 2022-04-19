package code;

import java.util.ArrayList;


/**
 * TODO jede instruction per functioncall einzeln ausführen lassen
 */
public class Decoder {

    public static AllObjects obj = AllObjects.getAllObjectsInstance();

    ArrayList<Integer> opCodeList = LSTFileReader.getOpcode();
    ArrayList<Integer> opVal = LSTFileReader.getOperationValue();
    ArrayList<Integer> decodeList = LSTFileReader.getDecodeList();
    ArrayList<Integer> last11Bits = LSTFileReader.getLast11Bits();
    ArrayList<Integer> pcList = LSTFileReader.getPcList();


    /**
     * decodes the lst file and adds all to the programm memory
     *
     * @param src string of the source
     */
    public void decodeString(String src) {
        //System.out.println(Integer.toBinaryString(obj.ram.getStatus()));

        try {
            LSTFileReader.read(src);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //ProgramMemory.memory.addAll(decodeList);

    }

    /**
     * does one step at a time
     * TODO update pic ui here
     */
    public void nextStep() {
        functionCalls(Ram.programmCounter);
        obj.mainFrame.updateStack(obj.stack);

        //System.out.println("Stackpointer: " + Stack.pointer);
        //obj.stack.printStack();

    }

    /**
     * makes the function calls
     */
    public void functionCalls(Integer i) {

        if (i < decodeList.size()) {
            //int iHexOpcode = opCodeList.get(i);

            //int iOpValue = opVal.get(i);

            //System.out.println(String.format("0x%02X",iOpValue));
            int iOpCode = obj.alu.and(decodeList.get(i), 0xFF00);
            int iOpValue = obj.alu.and(decodeList.get(i), 0x00FF);
            int iWholeInstruction = obj.alu.and(decodeList.get(i), 0xFFFF);


            Ram.programmCounter++;


            //System.out.println(iOpCode);
            //switch (iHexOpcode) {
            switch (iOpCode) {
                case 0b0011_0000_0000_0000 -> movLW(iOpValue);
                case 0b0011_1001_0000_0000 -> andLW(iOpValue);
                case 0b0011_1000_0000_0000 -> iorLW(iOpValue);
                case 0b0011_1100_0000_0000 -> subLW(iOpValue);
                case 0b0011_1010_0000_0000 -> xorLW(iOpValue);
                case 0b0011_1110_0000_0000 -> addLW(iOpValue);
                case 0b0010_1000_0000_0000 -> goTO(last11Bits.get(i));
                case 0b0010_0000_0000_0000 -> call(last11Bits.get(i));
                case 0b0011_0100_0000_0000 -> retLW(iOpValue);
                case 0b0000_0000_0000_0000 -> {
                    if (iOpValue == 0b0000) {
                        nop();
                    }
                    if (iOpValue == 0b1000) {
                        returnToTos();
                    }
                    if (iOpValue == 0b1001) {
                        //TODO RETFIE
                    }
                }
                case 0b0000_0111_0000_0000 -> addWF(iOpValue);
                default -> System.out.println("Default");
            }
        }
    }

    /**
     * moves 8 bit literal to W register
     */
    public void movLW(Integer i) {


        Ram.wRegister = i;
        //System.out.println("In movlw " + Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("movlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * W register is anded with the 8 bit literal
     * result ist stored in W register
     *
     * @param i 8 bit literal
     */
    public void andLW(Integer i) {
        Ram.wRegister = obj.alu.and(Ram.wRegister, i);
        //System.out.println("In andlw " + Integer.toBinaryString(obj.ram.getStatus()));
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
            obj.ram.setZeroBit(true);

        }
        //System.out.println("In iorlw " + Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("iorlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }


    /**
     * he contents of W register is subtracted (2’s complement method) from the eight bit literal 'i'.
     * The result is placed in the W register.
     *
     * @param i 8 bit literal
     */
    public void subLW(Integer i) {
        int wRegBeforeSub = Ram.wRegister;

        boolean b = obj.alu.isDigitCarry(wRegBeforeSub, i);
        obj.ram.setDigitCarryBit(b);


        Ram.wRegister = i - Ram.wRegister;

        if (Ram.wRegister > 0) {
            obj.ram.setCarryBit(true);

        } else if (Ram.wRegister == 0) {
            obj.ram.setCarryBit(true);
            obj.ram.setZeroBit(true);


        } else {
            Ram.wRegister = obj.alu.xor(Ram.wRegister, 0xFF);

        }

        //System.out.println("In sublw " + Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("sublw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of the W register are
     * XOR’ed with the eight bit literal 'i'.
     * The result is placed in the W register.
     *
     * @param i 8 bit literal
     */
    public void xorLW(Integer i) {
        Ram.wRegister = obj.alu.xor(Ram.wRegister, i);

        if (Ram.wRegister == 0) {
            obj.ram.setZeroBit(true);

        }

        //System.out.println("in xorlw " + Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("xorlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of the W register are
     * added to the eight bit literal ’i’ and the
     * result is placed in the W register
     *
     * @param i 8 bit literal
     */
    public void addLW(Integer i) {

        int wregBefore = Ram.wRegister;
        boolean b = obj.alu.isDigitCarry(Ram.wRegister, i);

        Ram.wRegister += i;
        if (Ram.wRegister == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }

        if (Ram.wRegister > 255) {
            Ram.wRegister = obj.alu.and(Ram.wRegister, 0xFF);
            obj.ram.setCarryBit(true);
        } else {
            obj.ram.setCarryBit(false);
        }

        obj.ram.setDigitCarryBit(b);
        //System.out.println("in addlw " + Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("addlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * @param i number of the next code segment
     */
    public void goTO(Integer i) {
        int pcOfThisInstruction = Ram.programmCounter - 1;
        if (obj.programMemory.checkCycle(pcOfThisInstruction) == false) {
            Ram.programmCounter = i;
            Ram.programmCounter = obj.ram.setBit(11, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(3));
            Ram.programmCounter = obj.ram.setBit(12, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(4));

            System.out.println("goto " + i + " cycle 1");
        } else {
            System.out.println("goto " + i + " cycle 2");
        }
        obj.programMemory.cycleList.add(pcOfThisInstruction);

    }


    /**
     * Call Subroutine. First, return address
     * (PC+1) is pushed onto the stack. The
     * eleven bit immediate address is loaded
     * into PC bits <10:0>. The upper bits of
     * the PC are loaded from PCLATH. CALL
     * is a two cycle instruction.
     * TODO (PCLATH<4:3>) → PC<12:11> ?????????
     * TODO cycles nachfragen
     *
     * @param i 11bit address
     */
    public void call(Integer i) {
        int pcOfThisInstruction = Ram.programmCounter - 1;
        if (obj.programMemory.checkCycle(pcOfThisInstruction) == false) {
            obj.stack.pushOnStack(Ram.programmCounter);
            Ram.programmCounter = obj.ram.setBit(11, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(3));
            Ram.programmCounter = obj.ram.setBit(12, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(4));
            Ram.programmCounter = i;
            System.out.println("call " + i + " cycle 1");
        } else {
            System.out.println("call " + i + " cycle 2");
        }
        obj.programMemory.cycleList.add(pcOfThisInstruction);
    }

    /**
     * The W register is loaded with the eight
     * bit literal ’i’. The program counter is
     * loaded from the top of the stack (the
     * return address). This is a two cycle
     * instruction.
     *
     * @param i 8 bit literal which is loaded into wRegister
     */
    public void retLW(Integer i) {


        int pcOfThisInstruction = Ram.programmCounter - 1;
        if (obj.programMemory.checkCycle(pcOfThisInstruction) == false) {
            Ram.wRegister = i;
            Ram.programmCounter = obj.stack.pop();
            System.out.println("retLW: " + String.format("0x%02X", Ram.wRegister) +
                    " next instruction: " + Ram.programmCounter);
            System.out.println("goto " + i + " cycle 1");
        } else {
            System.out.println("goto " + i + " cycle 2");
        }
        obj.programMemory.cycleList.add(pcOfThisInstruction);

    }

    /**
     * normal name return
     * Return from subroutine. The stack is
     * POPed and the top of the stack (TOS)
     * is loaded into the program counter. This
     * is a two cycle instruction.
     */
    public void returnToTos() {
        Integer[] localStack = obj.stack.getStack();
        //System.out.println("next to call " +Ram.programmCounter);

        int localReadPointer = obj.stack.pointer - 1;
        if (localReadPointer == 8) {
            localReadPointer = 0;
        } else if (localReadPointer < 0) {
            localReadPointer = 7;
        }

        if (localStack[localReadPointer] != null) {
            Ram.programmCounter = obj.stack.pop();
            //System.out.println("next to call " +Ram.programmCounter);
            System.out.println("return to: " + Ram.programmCounter + " cycle 1");
        } else {
            System.out.println("retrun to cycle 2");
        }
    }

    /**
     * Move data from W register to register
     * 'f' in the ram section
     * TODO decide between bank 0 and 1
     * @param f
     */
    public void movWF(Integer f) {

    }

    public void addWF(Integer i) {


        System.out.println("addwf");
    }

    /**
     * no operation
     */
    public void nop() {
        System.out.println("nop");
    }

}
