package code;

public class ALU {


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
