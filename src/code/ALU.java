package code;

public class ALU {


    public static Integer and(Integer firstVal, Integer secondVal) {
        return firstVal &= secondVal;
    }

    public static Integer or(Integer firstVal, Integer secondVal) {
        return firstVal |= secondVal;
    }

    public static Integer xor(Integer firstVal, Integer secondVal) {
        return firstVal ^= secondVal;
    }

}
