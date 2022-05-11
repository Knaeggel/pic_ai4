package code;

import java.util.ArrayList;


@SuppressWarnings({"RedundantIfStatement", "DuplicatedCode"})
public class Decoder {

    public static AllObjects obj = AllObjects.getAllObjectsInstance();

    ArrayList<Integer> decodeList = LSTFileReader.getDecodeList();
    ArrayList<Integer> last11Bits = LSTFileReader.getLast11Bits();
    ArrayList<String> allLines = LSTFileReader.getWholeLine();


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
        // obj.mainFrame.highlightRow();
    }

    /**
     * makes the function calls
     */
    public void functionCalls(Integer i) {

        obj.ram.updateBank();


        if (i < decodeList.size()) {

            int iOpCode = obj.alu.and(decodeList.get(i), 0xFF00);
            int iOpValue = obj.alu.and(decodeList.get(i), 0x00FF);
            int iWholeInstruction = obj.alu.and(decodeList.get(i), 0xFFFF);
            String endlessLoop = allLines.get(i);

            if (iOpValue == 0) {
                iOpValue = obj.ram.getFSR();
            }

            System.out.println("PC: " + String.format("%02X", Ram.programmCounter));


            obj.ram.prescalerValue = obj.prescaler.calcPrescaleValueFromOptionReg(obj.ram.getOption());

            obj.timer.incrementTimer0(obj.ram.prescalerValue);
            Ram.programmCounter++;
            switch (iOpCode) {
                case 0b0011_0000_0000_0000 -> movLW(iOpValue);
                case 0b0011_1001_0000_0000 -> andLW(iOpValue);
                case 0b0011_1000_0000_0000 -> iorLW(iOpValue);
                case 0b0011_1100_0000_0000 -> subLW(iOpValue);
                case 0b0011_1010_0000_0000 -> xorLW(iOpValue);
                case 0b0011_1110_0000_0000 -> addLW(iOpValue);
                case 0b0010_1000_0000_0000 -> goTO(last11Bits.get(i), endlessLoop);
                case 0b0010_0000_0000_0000,
                        0b0010_0001_0000_0000,
                        0b0010_0010_0000_0000,
                        0b0010_0011_0000_0000,
                        0b0010_0100_0000_0000,
                        0b0010_0101_0000_0000,
                        0b0010_0111_0000_0000 -> call(last11Bits.get(i));
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
        System.out.println("movlw");
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
        System.out.println("andlw");
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
        System.out.println("iorlw");
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
        System.out.println("sublw");
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
        System.out.println("xorlw");
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
        System.out.println("addlw");
    }


    /**
     * @param i number of the next code segment
     */
    public void goTO(Integer i, String s) {
        int pcOfThisInstruction = Ram.programmCounter - 1;
        if (!obj.programMemory.checkCycle(pcOfThisInstruction)) {
            Ram.programmCounter = i;
            Ram.programmCounter = obj.ram.setBit(11, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(3));
            Ram.programmCounter = obj.ram.setBit(12, Ram.programmCounter, obj.ram.getSpecificPCLATHBit(4));

            System.out.println("goto " + String.format("0x%02X", i) + " cycle 1");

            //TODO look if ok
            //obj.programMemory.cycleList.add(pcOfThisInstruction);
        } else {
            System.out.println("goto " + String.format("0x%02X", i) + " cycle 2");
        }


    }


    /**
     * Call Subroutine. First, return address
     * (PC+1) is pushed onto the stack. The
     * eleven bit immediate address is loaded
     * into PC bits <10:0>. The upper bits of
     * the PC are loaded from PCLATH. CALL
     * is a two cycle instruction.
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
            //TODO look if ok
            //obj.programMemory.cycleList.add(pcOfThisInstruction);
        } else {
            System.out.println("call " + i + " cycle 2");
        }

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
            obj.programMemory.cycleList.add(pcOfThisInstruction);
        } else {
            System.out.println("retlw goto 0x" + String.format("%02X", Ram.programmCounter) + " cycle 2");
        }


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

        if (f != 0) {
            obj.ram.setRamAt(f, Ram.wRegister);
        } else if (f == 0) {
            //indirect addr.
            obj.ram.setRamAt(obj.ram.getFSR(), Ram.wRegister);
        }
        System.out.println("movwf saved in " + String.format("0x%02X", f));
    }

    /**
     * Add the contents of the W register with the
     * contents of register ’f’. If ’d’ is 0 the result is
     * stored in the W register. If ’d’ is 1 the result is
     * stored back in register ’f’.
     *
     * @param i 7bit literal
     */
    public void addWF(Integer i) {
        int dest = obj.ram.getNthBitOfValue(7, i);
        int addressInRam = obj.alu.and(i, 0b0111_1111);

        int valueOnAdress = 0;
        if (addressInRam != 0) {
            valueOnAdress = obj.ram.getRamAt(addressInRam);
        } else if (addressInRam == 0) {
            //indirect addr.
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }

        boolean b = obj.alu.isDigitCarry(Ram.wRegister, valueOnAdress);

        int resultOfAdd = Ram.wRegister + valueOnAdress;
        //System.out.println(String.format("0x%04X", resultOfAdd));

        if (dest == 0) {
            Ram.wRegister = resultOfAdd;
        } else if (dest == 1) {
            if (addressInRam != 0) {
                obj.ram.setRamAt(addressInRam, resultOfAdd);
            } else if (addressInRam == 0) {
                obj.ram.setRamAt(obj.ram.getFSR(), resultOfAdd);
            }
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

        System.out.println("andwf");
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

        System.out.println("comf");
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
        System.out.println("decf");
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
        int valueOnAdress = 0;
        if (addressInRam != 0) {
            valueOnAdress = obj.ram.getRamAt(addressInRam);

            //indirect addr.
        } else if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }

        valueOnAdress += 1;
        if (valueOnAdress > 0xFF) {
            valueOnAdress = 0;
        }

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            if (addressInRam != 0) {
                obj.ram.setRamAt(addressInRam, valueOnAdress);
                //indirect addr.
            } else if (addressInRam == 0) {
                obj.ram.setRamAt(obj.ram.getFSR(), valueOnAdress);
            }
        }
        if (valueOnAdress == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }
        System.out.println("incf");
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
        System.out.println("movf");
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
        System.out.println("iorwf");
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
        System.out.println("subwf");
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

        //indirect addr.
        if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }

        int lownibble = valueOnAdress & 0x0F;
        int uppernibble = (valueOnAdress & 0XF0) >> 4;

        int result = (lownibble << 4) | uppernibble;


        if (dest == 0) {
            Ram.wRegister = result;
        } else if (dest == 1) {
            if (addressInRam != 0) {
                obj.ram.setRamAt(addressInRam, result);
                //indirect addr.
            } else if (addressInRam == 0) {
                obj.ram.setRamAt(obj.ram.getFSR(), result);
            }

        }
        System.out.println("swapf");
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

        //idirect addr.
        if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }

        int result = obj.alu.xor(Ram.wRegister, valueOnAdress);

        if (dest == 0) {
            Ram.wRegister = result;
        } else if (dest == 1) {
            if (addressInRam != 0) {
                obj.ram.setRamAt(addressInRam, result);

                //idirect addr.
            } else if (addressInRam == 0) {
                obj.ram.setRamAt(obj.ram.getFSR(), result);
            }
        }

        if (result == 0) {
            obj.ram.setZeroBit(true);
        } else {
            obj.ram.setZeroBit(false);
        }

        System.out.println("xorwf");
    }

    /**
     * W register is cleared. Zero bit (Z) is set
     */
    public void clrw() {
        Ram.wRegister = 0;
        obj.ram.setZeroBit(true);
        System.out.println("clrw");
    }

    /**
     * The contents of register ’f’ are rotated
     * one bit to the left through the Carry
     * Flag. If ’d’ is 0 the result is placed in the
     * W register. If ’d’ is 1 the result is stored
     * back in register ’f’.
     * TODO posible error output not right
     *
     * @param f 7bit literal, 8th bit=destination
     */
    public void rlf(Integer f) {
        int dest = obj.ram.getNthBitOfValue(7, f);
        int addressInRam = obj.alu.and(f, 0b0111_1111);
        int valueOnAdress = obj.ram.getRamAt(addressInRam);

        //indirect addr.
        if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }

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
            if (addressInRam != 0) {
                obj.ram.setRamAt(addressInRam, valueOnAdress);
                //indirect addr.
            } else if (addressInRam == 0) {
                obj.ram.setRamAt(obj.ram.getFSR(), valueOnAdress);
            }
        }

        //System.out.println(Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("rlf");
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
        int valueOnAdress = 0;
        if (addressInRam != 0) {
            valueOnAdress = obj.ram.getRamAt(addressInRam);

            //indirect addr.
        } else if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }

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
            if (addressInRam != 0) {
                obj.ram.setRamAt(addressInRam, valueOnAdress);
                //indirect addr.
            } else if (addressInRam == 0) {
                obj.ram.setRamAt(obj.ram.getFSR(), valueOnAdress);
            }

        }

        //System.out.println(Integer.toBinaryString(obj.ram.getStatus()));
        System.out.println("rrf");
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
            /*
            if (obj.programMemory.checkCycle(Ram.programmCounter)) {
                obj.programMemory.cycleList.remove(obj.programMemory.cycleList.size() - 1);
            }
            */

            //TODO remove all cycles between goto and adress of goto change i
            for (int i = Ram.programmCounter; i > 1; i--) {
                if (obj.programMemory.checkCycle(i)) {
                    obj.programMemory.cycleList.remove(Integer.valueOf(i));
                }
            }
        } else {
            obj.programMemory.skipNextInstruction();
        }

        if (dest == 0) {
            Ram.wRegister = valueOnAdress;
        } else if (dest == 1) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }

        System.out.println("decfsz");
    }

    /**
     * The contents of register ’f’ are incremented. If ’d’ is 0 the result is placed in
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
            /*
            if (obj.programMemory.checkCycle(Ram.programmCounter)) {
                obj.programMemory.cycleList.remove(obj.programMemory.cycleList.size() - 1);
            }

             */
            //TODO remove all cycles between goto and adress of goto
            for (int i = Ram.programmCounter; i > 1; i--) {
                if (obj.programMemory.checkCycle(i)) {
                    obj.programMemory.cycleList.remove(Integer.valueOf(i));
                }
            }
        } else {
            obj.programMemory.skipNextInstruction();
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

        //indirect addr.
        if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
            bitToSet = obj.alu.getIntValFromBitToBit(8, 10, f);
        }

        if (obj.ram.getNthBitOfValue(bitToSet, valueOnAdress) != 1) {
            valueOnAdress = obj.ram.setBit(bitToSet, valueOnAdress, 1);
        }
        if (addressInRam != 0) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }

        //indirect addr.
        if (addressInRam == 0) {
            obj.ram.setRamAt(obj.ram.getFSR(), valueOnAdress);
        }

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

        if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }
        if (obj.ram.getNthBitOfValue(bitToClear, valueOnAdress) == 1) {
            valueOnAdress = obj.ram.clearBit(bitToClear, valueOnAdress, 1);
        }

        if (addressInRam != 0) {
            obj.ram.setRamAt(addressInRam, valueOnAdress);
        }

        //indirect addr.
        if (addressInRam == 0) {
            obj.ram.setRamAt(obj.ram.getFSR(), valueOnAdress);
        }
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
        int bitToCheck = obj.alu.getIntValFromBitToBit(8, 10, f);

        //indirect addr.
        if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }

        if ((obj.ram.getNthBitOfValue(bitToCheck, valueOnAdress) == 0)) {
            obj.programMemory.skipNextInstruction();

        }
        System.out.println("btfsc");
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

        //indirecr addr.
        if (addressInRam == 0) {
            valueOnAdress = obj.ram.getRamAt(obj.ram.getFSR());
        }

        if (obj.ram.getNthBitOfValue(bitToClear, valueOnAdress) == 1) {
            obj.programMemory.skipNextInstruction();

        }
        System.out.println("btfss");
    }

    /**
     * no operation
     */
    public void nop() {
        System.out.println("nop");
    }

}
