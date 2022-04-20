package code;

public class Ram {
    public enum statusBits {Carry, DigitCarry, Zero}

    private static Integer[][] ram;


    /**
     * decides which bank is chosen
     */
    public static int bank = 0;

    public static int wRegister;
    public static int programmCounter = 0;


    public Ram() {
        ram = new Integer[2][128];
        setStatus(0b0001_1000);
        setPCLATH(0b0_0000);
    }

    public void printZDCC() {
        System.out.print("C: " + Decoder.obj.ram.getSpecificStatusBit(0));
        System.out.print(" DC: " + Decoder.obj.ram.getSpecificStatusBit(1));
        System.out.println(" Z: " + Decoder.obj.ram.getSpecificStatusBit(2));
    }

    public Integer[][] getRam() {
        return ram;
    }

    /**
     * @param position
     * @param value
     */
    public void setRamAt(int position, int value) {
        if (position <= 0x7F) {
            ram[0][position] = value;
        } else {
            int newPos = Decoder.obj.alu.and(position, 0b0111_1111);
            ram[1][newPos] = value;
        }
    }

    public int getRamAt(int pos) {
        if (pos <= 0x7F) {
            return ram[0][pos];
        } else {
            int newPos = Decoder.obj.alu.and(pos, 0b0111_1111);
            return ram[1][newPos];
        }
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
     * @param b if true set Zero bit to 1 if false set Zero bit to 0
     */
    public void setZeroBit(boolean b) {
        Integer iRet = getStatus();
        if (b) {
            iRet += 0b100;
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
            iRet += 0b1;
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
            iRet += 0b10;
        } else {
            if (getSpecificStatusBit(1) == 1) {
                iRet -= 0b10;
            }

        }
        Decoder.obj.ram.setStatus(iRet);
    }

    /**
     * needs
     * boolean b = obj.alu.isDigitCarry(Ram.wRegister, i);
     * before wanted operation
     *
     * @param b = obj.alu.isDigitCarry(Ram.wRegister, i);
     */
    public void affectStatusBits(boolean b) {
        if (Ram.wRegister == 0) {
            Decoder.obj.ram.setZeroBit(true);
        } else {
            Decoder.obj.ram.setZeroBit(false);
        }

        if (Ram.wRegister > 255) {
            Ram.wRegister = Decoder.obj.alu.and(Ram.wRegister, 0xFF);
            Decoder.obj.ram.setCarryBit(true);
        } else {
            Decoder.obj.ram.setCarryBit(false);
        }
        Decoder.obj.ram.setDigitCarryBit(b);

    }


}
