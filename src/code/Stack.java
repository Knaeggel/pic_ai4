package code;

public class Stack {

    public static Integer[] stack = new Integer[8];
    static int pointer = 0;

    public static void printStack(){
        for (Integer integer : stack) {
            System.out.println(String.format("0x%02X",integer));
        }
    }

    /**
     *
     * @param iVal
     */
    public static void pushOnStack(Integer iVal) {
        stack[pointer] = iVal;
        pointer++;
        if (pointer == 8) {
            pointer = 0;
        }
    }
    public static Integer pop() {
        Integer iRet = 0;
        iRet = stack[pointer];
        stack[pointer] = 0;
        pointer--;
        if (pointer < 0) {
            pointer = 7;
        }

        return iRet;
    }

    /*
    public static Integer top() {
        Integer iRet = stack[0];
        Integer aI[] = new Integer[8];
        for (int i = 0; i < 7 ; i++){
            aI[i] = stack[i+1];
        }
        for (int i = 0; i < 8 ; i++){
            stack[i] = aI[i];
        }

        pointer--;
        return iRet;
    }
    */
}
