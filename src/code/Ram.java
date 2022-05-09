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
    }

    public void printZDCC() {
        System.out.print("DC=" + Decoder.obj.ram.getSpecificStatusBit(1));
        System.out.print(" C=" + Decoder.obj.ram.getSpecificStatusBit(0));

        System.out.println(" Z=" + Decoder.obj.ram.getSpecificStatusBit(2));
    }

    public void printGeneralAndMapped() {
        if (ram[0][0] != 0) {
            System.out.println("Indirect addr. bank 0: " +
                    String.format("0x%02X", ram[0][0]));
        }
        if (ram[1][0] != 0) {
            System.out.println("Indirect addr. bank 1: " +
                    String.format("0x%02X", ram[1][0]));
        }
        if (ram[0][0x4] != 0) {
            System.out.println("FSR bank 0: " +
                    String.format("0x%02X = ", 4) + String.format("0x%02X", ram[0][4]));
        }
        if (ram[1][0x4] != 0) {
            System.out.println("FSR bank 1: " +
                    String.format("0x%02X = ", 4) + String.format("0x%02X", ram[1][4]));
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
                System.out.print( String.format(" 0x%02X = ", i) + String.format("0x%02X,", ram[1][i]));
            }
        }
        System.out.println();
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
    public void updateBank(){
        bank = getSpecificStatusBit(5);
    }

    public int getRamAt(int pos) {
        if (pos <= 0x7F) {
            return ram[0][pos];
        } else {
            int newPos = Decoder.obj.alu.and(pos, 0b0111_1111);
            return ram[1][newPos];
        }
    }

    public int getPrescalerValue() {
        return prescalerValue;
    }

    public void setPrescalerValue(int prescalerValue) {
        Ram.prescalerValue = prescalerValue;
    }

    public void setStatus(Integer i) {
        ram[0][3] = i;
        ram[1][3] = i;
    }

    public Integer getStatus() {
        return ram[bank][3];
    }

    /**
     * cant be more than 31
     * TODO should be set on the selected bank?
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

    public int getSpecificOptionBit(int n) {
        return ((ram[1][0x01] >> (n /*-1*/)) & 1);

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

    public int getFSR(){
        return ram[bank][0x04];
    }
    public int getPCL() {
        return ram[bank][0x02];
    }
    public int getOption() {
        return ram[1][0x01];
    }
    public int getTMR0() {
        return ram[0][0x01];
    }

    public void incrementTMR0() {
        ram[0][0x01]++;
    }

    public void setIntcon(int i) {
        ram[0][0x0B] = i;
        ram[1][0x0B] = i;
    }



}
