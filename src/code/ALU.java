package code;

public class ALU {

    /**
     * @param wRegBeforeSub value of the wRegister before any operation
     * @param i             operation value
     * @return boolen value if you have to set the DigitCarry bit or not
     */
    public boolean isDigitCarry(int wRegBeforeSub, int i) {
        boolean bRet;
        if ((((wRegBeforeSub >> (3 )) & 1) == 1) && (((i >> (3 )) & 1) == 1)) {
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

    /**
     * 8Bit compliments of two
     *
     * @param i
     * @return
     */
    public static int getTwoCompliment(int i) {
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
    public static int getCompliment(int i) {
        return ~i & 0xFF;
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
