package code;

public class Ram {


    private static Integer[][] ram;


    /**
     * decides which bank is chosen
     */
    public static int bank = 0;

    public static int wRegister;
    public static int programmCounter = 0;
    public static int cycles = 0;

    public static int prescalerValue;


    public Ram() {
        cycles = 0;
        prescalerValue = 256;
        ram = new Integer[2][128];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 128; j++) {
                ram[i][j] = 0;
            }
        }
        setStatus(0b0001_1000);
        setPCLATH(0b0_0000);
        setOption(0b1111_1111);
        setIntcon(0b0000_0000);
        setTrisA(0b1_1111);
        setTrisB(0b1111_1111);
    }

    /**
     * debug
     */
    public void printZDCC() {
        System.out.print("DC=" + Decoder.obj.ram.getSpecificStatusBit(1));
        System.out.print(" C=" + Decoder.obj.ram.getSpecificStatusBit(0));

        System.out.println(" Z=" + Decoder.obj.ram.getSpecificStatusBit(2));
    }

    /**
     * prints general and mapped register
     * debug
     */
    public void printGeneralAndMapped() {
        System.out.println("\nwRegister: " + String.format("0x%02X", Ram.wRegister) + "\n");

        if (ram[0][0] != 0) {
            System.out.println("Indirect addr. bank 0: " + String.format("0x%02X", ram[0][0]));
        }
        if (ram[1][0] != 0) {
            System.out.println("Indirect addr. bank 1: " + String.format("0x%02X", ram[1][0]));
        }
        if (ram[0][0x4] != 0) {
            System.out.println("FSR bank 0: " + String.format("0x%02X = ", 4) + String.format("0x%02X", ram[0][4]));
        }
        if (ram[1][0x4] != 0) {
            System.out.println("FSR bank 1: " + String.format("0x%02X = ", 4) + String.format("0x%02X", ram[1][4]));
        }
        System.out.print("Val at bank 0 and adress:");
        for (int i = 0x0C; i < 0x4F; i++) {
            if (ram[0][i] != 0) {
                System.out.print(String.format(" 0x%02X = ", i) + String.format("0x%02X,", ram[0][i]));
            }

        }
        System.out.print("\nVal at bank 1 and adress:");
        for (int i = 0x0C; i < 0x4F; i++) {
            if (ram[1][i] != 0) {
                System.out.print(String.format(" 0x%02X = ", i) + String.format("0x%02X,", ram[1][i]));
            }
        }
        System.out.println("\n");
    }

    public Integer[][] getRam() {
        return ram;
    }

    /**
     * @param position
     * @param value
     */
    public void setRamAt(int position, int value) {
        /*
        if (position <= 0x7F) {
            ram[0][position] = value;
        } else {
            int newPos = Decoder.obj.alu.and(position, 0b0111_1111);
            ram[1][newPos] = value;
        }
         */
        ram[bank][position] = value;
    }

    /**
     * updates bank
     */
    public void updateBank() {
        bank = getSpecificStatusBit(5);
    }


    /**
     * @param pos position in ram
     * @return value at position
     */
    public int getRamAt(int pos) {
        if (pos <= 0x7F) {
            return ram[0][pos];
        } else {
            int newPos = Decoder.obj.alu.and(pos, 0b0111_1111);
            return ram[1][newPos];
        }
    }

    /**
     * @return assigned prescaler value
     */
    public int getPrescalerValue() {
        return prescalerValue;
    }

    public void setPrescalerValue(int prescalerValue) {
        Ram.prescalerValue = prescalerValue;
    }

    /**
     * @param i new value
     */
    public void setStatus(Integer i) {
        ram[0][3] = i;
        ram[1][3] = i;
    }

    /**
     * @return status of selected bank
     */
    public Integer getStatus() {
        return ram[bank][3];
    }

    /**
     * cant be more than 31
     *
     * @param i < 32
     */
    public void setPCLATH(Integer i) {
        ram[0][0xA] = i;
        ram[1][0xA] = i;
    }

    public Integer getPCLATH() {
        return ram[bank][0xA];
    }

    /**
     * sets option register
     *
     * @param i new value
     */
    public void setOption(Integer i) {
        ram[1][0x01] = i;
    }

    /**
     * @param n
     * @param addressOfRegister
     * @return
     */
    public int getSpecificGenericBit(int n, int addressOfRegister) {
        return ((ram[bank][addressOfRegister] >> (n /*-1*/)) & 1);
    }

    public int getSpecificGenericBit(int n, int addressOfRegister, int selectBank) {
        return ((ram[selectBank][addressOfRegister] >> (n /*-1*/)) & 1);
    }

    /**
     * @param n
     * @param value
     * @return
     */
    public int getNthBitOfValue(int n, int value) {
        return ((value >> (n /*-1*/)) & 1);
    }

    /**
     * @param n the specific bits value (starts with 0)
     * @return value of the n-th bit
     * <p>
     * remove the commented part to start with 1 instead of 0
     */
    public int getSpecificStatusBit(int n) {
        //return ((ram[bank][3] >> (n /*-1*/)) & 1);
        return getSpecificGenericBit(n, 3);
    }

    /**
     * @param n bit
     * @return n-th bit of Option
     */
    public int getSpecificOptionBit(int n) {
        return ((ram[1][0x01] >> (n /*-1*/)) & 1);

    }

    /**
     * @param n bit
     * @return n-th bit of intcon
     */
    public int getSpecificIntconBit(int n) {
        return ((ram[bank][0x0B] >> (n /*-1*/)) & 1);

    }

    /**
     * @param n the specific bits value (starts with 0)
     * @return value of the n-th bit
     * <p>
     * remove the commented part to start with 1 instead of 0
     */
    public int getSpecificPCLATHBit(int n) {
        return getSpecificGenericBit(n, 0xA);
    }

    /**
     * Sets a specific bit of an int.
     *
     * @param bit    the bit to set. The least significant bit is bit 0
     * @param target the integer where the bit will be set
     * @param set    decides if you want to set or keep the original
     * @return the updated value of the target
     */
    public int setBit(int bit, int target, int set) {

        if (set == 1) {
            int mask = 1 << bit;
            // Set bit
            return target | mask;
        } else {
            return target;
        }
    }

    /**
     * @param bit    starting with 0
     * @param target the integer where the bit will be set
     * @param clear  decides if you want to set or keep the original
     * @return the updated value of the target
     */
    public int clearBit(int bit, int target, int clear) {
        if (clear == 1) {
            return target & ~(1 << bit);
        } else {
            return target;
        }
    }


    /**
     * @param b if true set Zero bit to 1 if false set Zero bit to 0
     */
    public void setZeroBit(boolean b) {
        Integer iRet = getStatus();
        if (b) {
            if (getSpecificStatusBit(2) != 1) {
                iRet += 0b100;
                //System.out.println(";Z set;");
            }
        } else {
            if (getSpecificStatusBit(2) == 1) {
                iRet -= 0b100;
            }
        }
        Decoder.obj.ram.setStatus(iRet);

    }


    /**
     * @param b if true set Carry bit to 1 if false set Carry bit to 0
     */
    public void setCarryBit(boolean b) {
        Integer iRet = getStatus();
        if (b) {
            if (getSpecificStatusBit(0) != 1) {
                iRet += 0b1;
                //System.out.println(";C set;");
            }
        } else {
            if (getSpecificStatusBit(0) == 1) {
                iRet -= 0b1;
            }

        }
        Decoder.obj.ram.setStatus(iRet);

    }

    /**
     * @param b if true set DigitCarry bit to 1 if false set DigitCarry bit to 0
     */
    public void setDigitCarryBit(boolean b) {
        Integer iRet = getStatus();
        if (b) {
            if (getSpecificStatusBit(1) != 1) {
                iRet += 0b10;
                //System.out.println(";DC set;");
            }
        } else {
            if (getSpecificStatusBit(1) == 1) {
                iRet -= 0b10;
            }

        }
        Decoder.obj.ram.setStatus(iRet);

    }

    /**
     * @return FSR value
     */
    public int getFSR() {
        return ram[bank][0x04];
    }

    /**
     * @return PCL value
     */
    public int getPCL() {
        return ram[bank][0x02];
    }

    /**
     * @return Option value
     */
    public int getOption() {
        return ram[1][0x01];
    }

    /**
     * @return Timer0 value
     */
    public int getTMR0() {
        return ram[0][0x01];
    }

    /**
     * sets Timer0
     *
     * @param i new Value
     */
    public void setTMR0(int i) {
        ram[0][0x01] = i;
    }

    /**
     * increments Timer0
     */
    public void incrementTMR0() {
        ram[0][0x01]++;
        if (ram[0][0x01] >= 0xFF) { //TODO look if >= is right
            ram[0][0x01] = 0;
            Decoder.obj.ram.setZeroBit(true);
        }
    }

    /**
     * sets Intcon value
     *
     * @param i new value
     */
    public void setIntcon(int i) {
        ram[0][0x0B] = i;
        ram[1][0x0B] = i;
    }

    public int getIntcon() {
        return ram[bank][0x0B];
    }

    /**
     * sets tmr0 interrupt flag
     * T0IF: TMR0 overflow interrupt flag bit
     * 1 = TMR0 has overflowed (must be cleared in software)
     * 0 = TMR0 did not overflow
     *
     * @param b flag
     */
    public void setT0IF(boolean b) {
        int set = getIntcon();
        if (b) {
            if (getSpecificIntconBit(2) == 0) {
                set += 0b100;
            }
        } else {
            if (getSpecificIntconBit(2) == 1) {
                set -= 0b100;
            }
        }
        setIntcon(set);
    }

    /**
     * sets rb0 interrupt flag
     * INTF: RB0/INT Interrupt Flag bit
     * 1 = The RB0/INT interrupt occurred
     * 0 = The RB0/INT interrupt did not occur
     *
     * @param b flag
     */
    public void setINTF(boolean b) {
        int set = getIntcon();
        if (b) {
            if (getSpecificIntconBit(1) == 0) {
                set += 0b10;
            }
        } else {
            if (getSpecificIntconBit(1) == 1) {
                set -= 0b10;
            }
        }
        setIntcon(set);
    }

    public void setINTE(boolean b) {
        int set = getIntcon();
        if (b) {
            if (getSpecificIntconBit(4) == 0) {
                set += 0b10000;
            }
        } else {
            if (getSpecificIntconBit(4) == 1) {
                set -= 0b10000;
            }
        }
        setIntcon(set);
    }

    /**
     * RBIF: RB Port Change Interrupt Flag bit
     * 1 = When at least one of the RB7:RB4 pins changed state (must be cleared in software)
     * 0 = None of the RB7:RB4 pins have changed state
     *
     * @param b flag
     */
    public void setRBIF(boolean b) {
        int set = getIntcon();
        if (b) {
            if (getSpecificIntconBit(0) == 0) {
                set += 0b1;
            }
        } else {
            if (getSpecificIntconBit(0) == 1) {
                set -= 0b1;
            }
        }
        setIntcon(set);
    }

    public void setGIE(boolean b) {
        int set = getIntcon();
        if (b) {
            if (getSpecificIntconBit(2) == 0) {
                set += 0b1000_0000;
            }
        } else {
            if (getSpecificIntconBit(2) == 1) {
                set -= 0b1000_0000;
            }
        }
        setIntcon(set);
    }


    /**
     * gets a specific bit from PortB
     *
     * @param n bit
     * @return 1 if set 0 if clear
     */
    public int getSpecificPortBBit(int n) {
        //return ((ram[bank][3] >> (n /*-1*/)) & 1);
        return getSpecificGenericBit(n, 0x06, 0);
    }

    public int getSpecificTrisBBit(int n) {
        //return ((ram[bank][3] >> (n /*-1*/)) & 1);
        //return getSpecificGenericBit(n, 0x06, 1);
        return ram[1][6] >> n & 1;
    }

    public int getSpecificPortABit(int n) {
        //return ((ram[bank][3] >> (n /*-1*/)) & 1);
        //return getSpecificGenericBit(n, 0x06, 1);
        return ram[bank][5] >> n & 1;
    }

    public int getTrisA() {
        return ram[1][0x05];
    }

    public void setTrisA(int val) {
        ram[1][0x05] = val;
    }

    public void setTrisB(int val) {
        ram[1][0x06] = val;
    }

    /**
     * sets the value of PortB
     *
     * @param b0 bit 0 val
     * @param b1 bit 1 val
     * @param b2 bit 2 val
     * @param b3 bit 3 val
     * @param b4 bit 4 val
     * @param b5 bit 5 val
     * @param b6 bit 6 val
     * @param b7 bit 7 val
     */
    public void setPortB(boolean b0, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7) {
        int set = getPortB();
        if (b0) {
            if (getSpecificPortBBit(0) != 1) {
                set += 0b1;
            }
        } else {
            if (getSpecificPortBBit(0) == 1) {
                set -= 0b1;
            }
        }
        if (b1) {
            if (getSpecificPortBBit(1) != 1) {
                set += 0b10;
            }
        } else {
            if (getSpecificPortBBit(1) == 1) {
                set -= 0b10;
            }
        }
        if (b2) {
            if (getSpecificPortBBit(2) != 1) {
                set += 0b100;
            }
        } else {
            if (getSpecificPortBBit(2) == 1) {
                set -= 0b100;
            }
        }
        if (b3) {
            if (getSpecificPortBBit(3) != 1) {
                set += 0b1000;
            }
        } else {
            if (getSpecificPortBBit(3) == 1) {
                set -= 0b1000;
            }
        }
        if (b4) {
            if (getSpecificPortBBit(4) != 1) {
                set += 0b1_0000;
            }
        } else {
            if (getSpecificPortBBit(4) == 1) {
                set -= 0b1_0000;
            }
        }
        if (b5) {
            if (getSpecificPortBBit(5) != 1) {
                set += 0b10_0000;
            }
        } else {
            if (getSpecificPortBBit(5) == 1) {
                set -= 0b10_0000;
            }
        }
        if (b6) {
            if (getSpecificPortBBit(6) != 1) {
                set += 0b100_0000;
            }
        } else {
            if (getSpecificPortBBit(6) == 1) {
                set -= 0b100_0000;
            }
        }
        if (b7) {
            if (getSpecificPortBBit(7) != 1) {
                set += 0b1000_0000;
            }
        } else {
            if (getSpecificPortBBit(7) == 1) {
                set -= 0b1000_0000;
            }
        }
        ram[0][0x06] = set;
    }

    public int getPortB() {
        return ram[0][0x06];
    }

    public void updatePCL() {
        ram[bank][0x02] = Ram.programmCounter & 0xFF;
    }


}
