package code;

import java.util.ArrayList;


/**
 * TODO jede instruction per functioncall einzeln ausführen lassen
 */
@SuppressWarnings("RedundantIfStatement")
public class Decoder {

    public static AllObjects obj = AllObjects.getAllObjectsInstance();

    ArrayList<Integer> decodeList = LSTFileReader.getDecodeList();
    ArrayList<Integer> last11Bits = LSTFileReader.getLast11Bits();


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
        obj.mainFrame.breakpoints();
    }

    /**
     * makes the function calls
     */
    public void functionCalls(Integer i) {

        if (i < decodeList.size()) {

            int iOpCode = obj.alu.and(decodeList.get(i), 0xFF00);
            int iOpValue = obj.alu.and(decodeList.get(i), 0x00FF);
            int iWholeInstruction = obj.alu.and(decodeList.get(i), 0xFFFF);


            System.out.println("PC: " + String.format("%02X", Ram.programmCounter));
            Ram.programmCounter++;

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
                    if (iOpValue == 0b0000_0000) {
                        nop();
                    }
                    if (iOpValue == 0b0000_1000) {
                        returnToTos();
                    }
                    if (iOpValue == 0b0000_1001) {
                        //TODO RETFIE
                        System.out.println("retfie");
                    }
                    if (obj.ram.getNthBitOfValue(7, iOpValue) == 1) {
                        int newOpval = obj.alu.and(iOpValue, 0b0111_1111);
                        movWF(newOpval);
                    }
                }
                case 0b0000_0111_0000_0000 -> addWF(iOpValue);
                case 0b0000_0101_0000_0000 -> andWF(iOpValue);
                case 0b0000_0001_0000_0000 -> {
                    if (obj.ram.getNthBitOfValue(7, iOpValue) == 1) {
                        int newOpval = obj.alu.and(iOpValue, 0b0111_1111);
                        clrf(newOpval);
                    }
                    if (obj.ram.getNthBitOfValue(7, iOpValue) == 0) {
                        clrw();
                    }
                }
                case 0b0000_1001_0000_0000 -> comf(iOpValue);
                case 0b0000_0011_0000_0000 -> decf(iOpValue);
                case 0b0000_1010_0000_0000 -> incf(iOpValue);
                case 0b0000_1000_0000_0000 -> movf(iOpValue);
                case 0b0000_0100_0000_0000 -> iorwf(iOpValue);
                case 0b0000_0010_0000_0000 -> subWF(iOpValue);
                case 0b0000_1110_0000_0000 -> swapf(iOpValue);
                case 0b0000_0110_0000_0000 -> xorWF(iOpValue);
                case 0b0000_1101_0000_0000 -> rlf(iOpValue);
                case 0b0000_1100_0000_0000 -> rrf(iOpValue);
                case 0b0000_1011_0000_0000 -> decfsz(iOpValue);
                case 0b0000_1111_0000_0000 -> incfsz(iOpValue);
                case 0b0001_0100_0000_0000,
                        0b0001_0101_0000_0000,
                        0b0001_0110_0000_0000,
                        0b0001_0111_0000_0000 -> bsf(iWholeInstruction);
                case 0b0001_0000_0000_0000,
                        0b0001_0001_0000_0000,
                        0b0001_0010_0000_0000,
                        0b0001_0011_0000_0000 -> bcf(iWholeInstruction);
                case 0b0001_1000_0000_0000,
                        0b0001_1001_0000_0000,
                        0b0001_1010_0000_0000,
                        0b0001_1011_0000_0000 -> btfsc(iWholeInstruction);
                case 0b0001_1100_0000_0000,
                        0b0001_1101_0000_0000,
                        0b0001_1110_0000_0000,
                        0b0001_1111_0000_0000 -> btfss(iWholeInstruction);


                default -> System.out.println("Default");
            }
        }
    }

    private void affectZeroCarryDigitCarry(boolean b, int resultOfAdd) {
        if (resultOfAdd == 0) {
            Decoder.obj.ram.setZeroBit(true);
        } else {
            Decoder.obj.ram.setZeroBit(false);
        }

        if (resultOfAdd > 255 || resultOfAdd < 0) {
            Ram.wRegister = Decoder.obj.alu.and(Ram.wRegister, 0xFF);
            Decoder.obj.ram.setCarryBit(true);
        } else {
            Decoder.obj.ram.setCarryBit(false);
        }
        Decoder.obj.ram.setDigitCarryBit(b);
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
     * <p>
     * Daher gilt: Wenn der PIC eine Subtraktion durchführt:
     * Ergebnis 0 oder größer → Carry gesetzt
     * Ergebnis kleiner 0 → Carry gelöscht
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
            obj.ram.setCarryBit(false);
        }
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

        boolean b = obj.alu.isDigitCarry(Ram.wRegister, i);

        Ram.wRegister += i;

        int resultOfAdd = Ram.wRegister;

        //obj.ram.affectStatusBits(b, Ram.wRegister);
        affectZeroCarryDigitCarry(b, resultOfAdd);

        //System.out.println("in addlw " + Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("addlw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }


    /**
     * TODO bearbeiten mit gotoSecondCycle
     *
     * @param i number of the next code segment
     */
    public void goTO(Integer i) {
        int pcOfThisInstruction = Ram.programmCounter - 1;
        if (!obj.programMemory.checkCycle(pcOfThisInstruction)) {
            Ram.programmCounter = i;
            Ram.programmCounter = obj.ram.setBit(11, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(3));
            Ram.programmCounter = obj.ram.setBit(12, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(4));

            System.out.println("goto " + String.format("0x%02X", i) + " cycle 1");
        } else {
            System.out.println("goto " + String.format("0x%02X", i) + " cycle 2");
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
        if (!obj.programMemory.checkCycle(pcOfThisInstruction)) {
            obj.stack.pushOnStack(Ram.programmCounter);
            Ram.programmCounter = i;
            Ram.programmCounter = obj.ram.setBit(11, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(3));
            Ram.programmCounter = obj.ram.setBit(12, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(4));

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
        if (!obj.programMemory.checkCycle(pcOfThisInstruction)) {
            Ram.wRegister = i;
            Ram.programmCounter = obj.stack.pop();
            System.out.println("retLW: " + String.format("0x%02X", Ram.wRegister) +
                    " next instruction: " + Ram.programmCounter);
            System.out.println("retlw goto 0x" + String.format("%02X", Ram.programmCounter) + " cycle 1");
        } else {
            System.out.println("retlw goto 0x" + String.format("%02X", Ram.programmCounter) + " cycle 2");
        }
        obj.programMemory.cycleList.add(pcOfThisInstruction);

    }

    /**
     * normal name return.
     * Return from subroutine. The stack is
     * POPed and the top of the stack (TOS)
     * is loaded into the program counter. This
     * is a two cycle instruction.
     */
    public void returnToTos() {
        Integer[] localStack = obj.stack.getStack();
        //System.out.println("next to call " +Ram.programmCounter);

        int localReadPointer = Stack.pointer - 1;
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
            System.out.println("return to cycle 2");
        }
    }

    /**
     * Move data from W register to register
     * 'f' in the ram section
     *
     * @param f 7bit literal
     */
    public void movWF(Integer f) {

        obj.ram.setRamAt(f, Ram.wRegister);
        System.out.println("movwf saved in " + String.format("0x%02X", f));
    }

    /**
     * Add the contents of the W register with the
     * contents of register ’f’. If ’d’ is 0 the result is
     * stored in the W register. If ’d’ is 1 the result is
     * stored back in register ’f’.
     * TODO look if status bits are affected the right way
     *
     * @param i 7bit literal
     */
    public void addWF(Integer i) {
        int dest = obj.ram.getNthBitOfValue(7, i);
        int addressInRam = obj.alu.and(i, 0b0111_1111);

        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        boolean b = obj.alu.isDigitCarry(Ram.wRegister, valueOnAdress);

        int resultOfAdd = Ram.wRegister + valueOnAdress;
        //System.out.println(String.format("0x%04X", resultOfAdd));

        if (dest == 0) {
            Ram.wRegister = resultOfAdd;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, resultOfAdd);
        }

        //obj.ram.affectStatusBits(b, resultOfAdd);
        affectZeroCarryDigitCarry(b, resultOfAdd);

        System.out.println("addwf");
    }

    /**
     * AND the W register with contents of register 'f'.
     * If 'd' is 0 the result is stored in the W register. If 'd' is 1 the result is stored back in
     * register 'f'.
     *
     * @param f 7bit literal
     */
    public void andWF(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);


        int addressInRam = obj.alu.and(f, 0b0111_1111);

        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        boolean b = obj.alu.isDigitCarry(Ram.wRegister, valueOnAdress);

        int resultOfAnd = obj.alu.and(Ram.wRegister, valueOnAdress);

        if (dest == 0) {
            Ram.wRegister = resultOfAnd;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, resultOfAnd);
        }

        //obj.ram.affectStatusBits(b, Ram.wRegister);

        affectZeroCarryDigitCarry(b, resultOfAnd);

        System.out.println("andwf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of register ’f’ are cleared
     * and the Z bit is set.
     *
     * @param f 7bit literal
     */
    public void clrf(Integer f) {

        obj.ram.setRamAt(f, 0);
        obj.ram.setZeroBit(true);
        System.out.println("clrf register " + String.format("%02X", f) + " = " + obj.ram.getRamAt(f));
        //System.out.println(obj.ram.getRamAt(f));
    }

    /**
     * he contents of register ’f’ are complemented. If ’d’ is 0 the result is stored in
     * W. If ’d’ is 1 the result is stored back in
     * register ’f’.
     *
     * @param f 7bit literal
     */
    public void comf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);

        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        int resultOfComplement = obj.alu.getCompliment(valueOnAdress);

        if (dest == 0) {
            Ram.wRegister = resultOfComplement;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, resultOfComplement);
        }
        if (resultOfComplement == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }

        System.out.println("comf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * Decrement contents of register ’f’. If ’d’ is 0 the
     * result is stored in the W register. If ’d’ is
     * 1 the result is stored back in register ’f’.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void decf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        valueOnAdress -= 1;
        if (valueOnAdress < 0) {
            valueOnAdress = 0xFF;
        }

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }
        if (valueOnAdress == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }
        System.out.println("decf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * the contents of register ’f’ are incremented.
     * If ’d’ is 0 the result is placed in
     * the W register. If ’d’ is 1 the result is
     * placed back in register ’f’.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void incf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        valueOnAdress += 1;
        if (valueOnAdress > 0xFF) {
            valueOnAdress = 0;
        }

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }
        if (valueOnAdress == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }
        System.out.println("incf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of register f is moved to a
     * destination dependant upon the status
     * of d. If d = 0, destination is W register. If
     * d = 1, the destination is file register f
     * itself. d = 1 is useful to test a file register
     * since status flag Z is affected.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void movf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }
        if (valueOnAdress == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }
        System.out.println("movf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * Inclusive OR the W register with contents of
     * register ’f’. If ’d’ is 0 the result is placed in the
     * W register. If ’d’ is 1 the result is placed
     * back in register ’f’.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void iorwf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        int result = obj.alu.or(Ram.wRegister, valueOnAdress);

        if (dest == 0) {
            Ram.wRegister = result;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, result);
        }

        if (result == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }
        System.out.println("iorwf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * Subtract (2’s complement method) contents
     * of W register from register 'f'. If 'd' is 0 the
     * result is stored in the W register. If 'd' is 1 the
     * result is stored back in register 'f'.
     * <p>
     * <p>
     * Daher gilt: Wenn der PIC eine Subtraktion durchführt:
     * Ergebnis 0 oder größer → Carry gesetzt
     * Ergebnis kleiner 0 → Carry gelöscht
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void subWF(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        boolean b = obj.alu.isDigitCarry(Ram.wRegister, valueOnAdress);

        int result = valueOnAdress - Ram.wRegister;

        if (result >= 0) {
            Ram.wRegister = Decoder.obj.alu.and(Ram.wRegister, 0xFF);
            Decoder.obj.ram.setCarryBit(true);
        } else {
            Decoder.obj.ram.setCarryBit(false);
        }

        if (result == 0) {
            Decoder.obj.ram.setZeroBit(true);
        } else {
            Decoder.obj.ram.setZeroBit(false);
        }
        Decoder.obj.ram.setDigitCarryBit(b);

        if (result < 0) {
            result = 256 + result;
        }

        if (dest == 0) {
            Ram.wRegister = result;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, result);
        }
        System.out.println("subwf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The upper and lower nibbles of contents of
     * register 'f' are exchanged. If 'd' is 0 the result
     * is placed in W register. If 'd' is 1 the result
     * is placed in register 'f'.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void swapf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        int lownibble = valueOnAdress & 0x0F;
        int uppernibble = (valueOnAdress & 0XF0) >> 4;

        int result = (lownibble << 4) | uppernibble;


        if (dest == 0) {
            Ram.wRegister = result;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, result);
        }
        System.out.println("swapf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * Exclusive OR the contents of the W
     * register with contents of register 'f'. If 'd' is
     * 0 the result is stored in the W register. If 'd' is
     * 1 the result is stored back in register 'f'.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void xorWF(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        int result = obj.alu.xor(Ram.wRegister, valueOnAdress);

        if (dest == 0) {
            Ram.wRegister = result;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, result);
        }

        if (result == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }

        System.out.println("xorwf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * W register is cleared. Zero bit (Z) is set
     */
    public void clrw() {
        Ram.wRegister = 0;
        obj.ram.setZeroBit(true);
        System.out.println("clrw wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of register ’f’ are rotated
     * one bit to the left through the Carry
     * Flag. If ’d’ is 0 the result is placed in the
     * W register. If ’d’ is 1 the result is stored
     * back in register ’f’.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void rlf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        int carry = obj.ram.getSpecificStatusBit(0);

        if (obj.ram.getNthBitOfValue(7, valueOnAdress) == 1) {
            if (carry == 1) {
                obj.ram.setCarryBit(false);
            }
            obj.ram.setCarryBit(true);
        } else {
            obj.ram.setCarryBit(false);
        }
        valueOnAdress <<= 1;
        valueOnAdress = obj.alu.and(valueOnAdress, 0xFF);
        valueOnAdress += carry;

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }

        //System.out.println(Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("rlf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of register ’f’ are rotated
     * one bit to the right through the Carry
     * Flag. If ’d’ is 0 the result is placed in the
     * W register. If ’d’ is 1 the result is placed
     * back in register ’f’.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void rrf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        int carry = obj.ram.getSpecificStatusBit(0);

        if (obj.ram.getNthBitOfValue(0, valueOnAdress) == 1) {
            if (carry == 1) {
                obj.ram.setCarryBit(false);
            }
            obj.ram.setCarryBit(true);
        } else {
            obj.ram.setCarryBit(false);
        }
        valueOnAdress >>= 1;
        valueOnAdress = obj.alu.and(valueOnAdress, 0xFF);
        valueOnAdress = obj.ram.setBit(7, valueOnAdress, carry);

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }

        //System.out.println(Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("rrf wRegister: " + String.format("0x%02X", Ram.wRegister));
    }

    /**
     * The contents of register ’f’ are decremented. If ’d’ is 0 the result is placed in the
     * W register. If ’d’ is 1 the result is placed
     * back in register ’f’.
     * If the result is not 0, the next instruction, is
     * executed. If the result is 0, then a NOP is
     * executed instead making it a 2TCY instruction.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void decfsz(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);
        valueOnAdress--;
        if (valueOnAdress != 0) {

            // creates assembler for loop
            if (obj.programMemory.checkCycle(Ram.programmCounter)) {
                obj.programMemory.cycleList.remove(obj.programMemory.cycleList.size() - 1);
            }
        }

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }

        System.out.println("decfsz");
    }

    /**
     * TODO bits richtig setzen
     * he contents of register ’f’ are incremented. If ’d’ is 0 the result is placed in
     * the W register. If ’d’ is 1 the result is
     * placed back in register ’f’.
     * If the result is not 0, the next instruction is
     * executed. If the result is 0, a NOP is executed instead making it a 2TCY instruction.
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void incfsz(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);
        valueOnAdress++;
        if (valueOnAdress > 0xFF) {
            valueOnAdress = 0;
        }
        if (valueOnAdress != 0) {

            // creates assembler for loop
            if (obj.programMemory.checkCycle(Ram.programmCounter)) {
                obj.programMemory.cycleList.remove(obj.programMemory.cycleList.size() - 1);
            }
        }

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }

        System.out.println("incfsz");
    }

    /**
     * Bit ’b’ in register ’f’ is set.
     *
     * @param f <0:6> register <7:9> selected bit to set
     */
    public void bsf(Integer f) {
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);
        int bitToSet = obj.alu.getIntValFromBitToBit(8, 10, f);

        if (obj.ram.getNthBitOfValue(bitToSet, valueOnAdress) != 1) {
            valueOnAdress = obj.ram.setBit(bitToSet, valueOnAdress, 1);
        }
        obj.ram.setRamAt(addressInRam, valueOnAdress);
        System.out.println("bsf newVal: " + String.format("0x%02X", valueOnAdress));
    }

    /**
     * Bit ’b’ in register ’f’ is cleared.
     *
     * @param f <0:6> register <7:9> selected bit to set
     */
    public void bcf(Integer f) {
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);
        int bitToClear = obj.alu.getIntValFromBitToBit(8, 10, f);

        if (obj.ram.getNthBitOfValue(bitToClear, valueOnAdress) == 1) {
            valueOnAdress = obj.ram.clearBit(bitToClear, valueOnAdress, 1);
        }
        obj.ram.setRamAt(addressInRam, valueOnAdress);
        System.out.println("bcf newVal: " + String.format("0x%02X", valueOnAdress));
    }

    /**
     * If bit ’b’ in register ’f’ is ’1’ then the next
     * instruction is executed.
     * If bit ’b’, in register ’f’, is ’0’ then the next
     * instruction is discarded, and a NOP is
     * executed instead, making this a 2TCY
     * instruction.
     *
     * @param f <0:6> register <7:9> selected bit to set
     */
    public void btfsc(Integer f) {
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);
        int bitToClear = obj.alu.getIntValFromBitToBit(8, 10, f);

        if (obj.ram.getNthBitOfValue(bitToClear, valueOnAdress) != 1) {
            obj.programMemory.skipNextInstruction();

        }

    }

    /**
     * If bit ’b’ in register ’f’ is ’0’ then the next
     * instruction is executed.
     * If bit ’b’ is ’1’, then the next instruction is
     * discarded and a NOP is executed
     * instead, making this a 2TCY instruction.
     *
     * @param f <0:6> register <7:9> selected bit to set
     */
    public void btfss(Integer f) {
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);
        int bitToClear = obj.alu.getIntValFromBitToBit(8, 10, f);

        if (obj.ram.getNthBitOfValue(bitToClear, valueOnAdress) == 1) {
            obj.programMemory.skipNextInstruction();

        }
    }

    /**
     * no operation
     */
    public void nop() {
        System.out.println("nop");
    }

}
