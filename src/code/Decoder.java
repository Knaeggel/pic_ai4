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


        ProgrammMemory.memory.addAll(decodeList);

    }

    /**
     * does one step at a time
     */
    public void nextStep() {
        functionCalls(Ram.programmCounter);
    }

    /**
     * makes the function calls
     */
    public void functionCalls(Integer i) {


        int iHexOpcode = opCodeList.get(i);

        int iOpValue = opVal.get(i);

        //System.out.println(String.format("0x%02X",iOpValue));
        obj.stack.pushOnStack(decodeList.get(i));

        obj.mainFrame.updateStack(obj.stack);

        Ram.programmCounter++;
        switch (iHexOpcode) {
            case 0b0011_0000 -> movLW(iOpValue);
            case 0b0011_1001 -> andLW(iOpValue);
            case 0b0011_1000 -> iorLW(iOpValue);
            case 0b0011_1100 -> subLW(iOpValue);
            case 0b0011_1010 -> xorLW(iOpValue);
            case 0b0011_1110 -> addLW(iOpValue);
            case 0b0010_1000 -> goTO(iOpValue);
            case 0b0010_0000 -> call(iOpValue);
            case 0b0011_0100 -> retLW(iOpValue);
            case 0b0000_0000 -> {
                if (iOpValue == 0b0000){
                    nop();
                }
                if (iOpValue == 0b1000) {
                    reTurn();
                }
                if (iOpValue == 0b1001) {
                    //TODO RETFIE
                }
            }
            default -> System.out.println("Default");

        }
    }

    /**
     * @param wRegBeforeSub value of the wRegister before any operation
     * @param i             operation value
     * @return boolen value if you have to set the DigitCarry bit or not
     */
    public boolean needToSetDigitCarry(int wRegBeforeSub, int i) {
        boolean bRet;
        wRegBeforeSub = obj.alu.xor(wRegBeforeSub, 0xFF) + 1;
        if (((i & 0xF) + (wRegBeforeSub & 0xF) + 1) > 15) {
            bRet = true;
        } else {
            bRet = false;
        }
        return bRet;
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

        boolean b = needToSetDigitCarry(wRegBeforeSub, i);
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
        obj.ram.setDigitCarryBit(needToSetDigitCarry(wregBefore, i));
        //System.out.println("in addlw " + Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("addlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * TODO runtime counter needs to be increased by 2
     *
     * @param i number of the next code segment
     */
    public void goTO(Integer i) {

        Ram.programmCounter = i;
        System.out.println("goto");

    }

    public void call(Integer i) {

        System.out.println("call " + i);
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
        Ram.wRegister = i;
        Ram.programmCounter = obj.stack.pop();
        System.out.println("retLW");
    }

    /**
     * Return from subroutine. The stack is
     * POPed and the top of the stack (TOS)
     * is loaded into the program counter. This
     * is a two cycle instruction.
     */
    public void reTurn() {
        System.out.println("return");
    }

    /**
     * no operation
     */
    public void nop() {
        System.out.println("nop");
    }

}
