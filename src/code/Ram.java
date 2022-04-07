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
    }

    public Integer[][] getRam() {
        return ram;
    }


    public void setStatus(Integer i) {
        ram[0][3] = i;
        ram[1][3] = i;
    }

    public Integer getStatus() {
        return ram[bank][3];
    }

    /**
     *
     * @param n the specific bits value (starts with 0)
     * @return value of the n-th bit
     *
     * remove the commented part to start with 1 instead of 0
     */
    public int getSpecificBit(int n) {
        return ((ram[bank][3] >> (n /*-1*/ )) & 1);
    }

    /**
     *
     * @param b if true set Zero bit to 1 if false set Zero bit to 0
     */
    public void setZeroBit(boolean b) {
        Integer iRet = getStatus();
        if (b) {
            iRet += 0b100;
        } else {
            iRet -= 0b100;
        }
        Decoder.obj.ram.setStatus(iRet);
    }


    /**
     *
     *
     * @param b if true set Carry bit to 1 if false set Carry bit to 0
     */
    public void setCarryBit(boolean b) {
        Integer iRet = getStatus();
        if (b) {
            iRet +=0b1;
        } else {
            iRet -= 0b1;
        }
        Decoder.obj.ram.setStatus(iRet);
    }

    /**
     *
     *
     * @param b if true set DigitCarry bit to 1 if false set DigitCarry bit to 0
     */
    public void setDigitCarryBit(boolean b) {
        Integer iRet = getStatus();
        if (b) {
            iRet +=0b10;
        } else {
            iRet -= 0b10;
        }
        Decoder.obj.ram.setStatus(iRet);
    }


}
