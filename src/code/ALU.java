package code;

public class ALU {

    /**
     * @param wRegBeforeSub value of the wRegister before any operation
     * @param i             operation value
     * @return boolen value if you have to set the DigitCarry bit or not
     */
    public boolean isDigitCarry(int wRegBeforeSub, int i) {
        boolean bRet;
        if ((((wRegBeforeSub >> (3)) & 1) == 1) && (((i >> (3)) & 1) == 1)) {
            bRet = true;
        } else {
            bRet = false;
        }

        /*
        wRegBeforeSub = xor(wRegBeforeSub, 0xFF) + 1;
        if (((i & 0xF) + (wRegBeforeSub & 0xF) ) > 15) {
            bRet = true;
        } else {
            bRet = false;
        }
        */
        return bRet;
    }

    public static boolean isDigitCarry2(int firstStart, int secondStart) {
        int carryCheck = (firstStart & 0xF) + (secondStart & 0xF);
        return carryCheck > 0xF;
    }

    public static boolean isCarry(int firstStart, int secondStart) {
        return firstStart + secondStart > 0xFF;
    }

    /**
     * 8Bit compliments of two
     *
     * @param i
     * @return
     */
    public int getTwoCompliment(int i) {
        i = getCompliment(i);
        i += 1;
        i &= 0xFF; //safety for 8bit
        return i;
    }

    /**
     * Returns 8-Bit compliment of f
     *
     * @param i
     * @return
     */
    public int getCompliment(int i) {
        return ~i & 0xFF;
    }

    public int get7BitCompliment(int i) {
        return ~i & 0x7F;
    }

    /**
     *
     * @param startBit startbit
     * @param endBit includingEndbit
     * @param inputVal instructioonVal
     * @return added number
     */
    public int getIntValFromBitToBit(int startBit, int endBit, int inputVal) {
        int ret = 0;
        int counter = 0;
        for (int i = startBit - 1; i < endBit; i++, counter++) {
            if (Decoder.obj.ram.getNthBitOfValue(i, inputVal) == 1) {
                ret += (int) Math.pow(2, counter);
            }
        }

        return ret;
    }


    public Integer and(Integer firstVal, Integer secondVal) {
        return firstVal &= secondVal;
    }

    public Integer or(Integer firstVal, Integer secondVal) {
        return firstVal |= secondVal;
    }

    public Integer xor(Integer firstVal, Integer secondVal) {
        return firstVal ^= secondVal;
    }

}
