package code;

/**
 * Der PIC-Controller besitzt einen 8-stufigen Stack. Dieser kann nicht per Befehl
 * beeinflusst werden. Er dient lediglich für die Speicherung der Rückkehradresse
 * bei Unterprogrammaufrufen. Der Stack wird über einen unsichtbaren Stackpointer verwaltet.
 * Auch auf dieses Register ist kein direkter Zugriff möglich.
 * Der Stackpointer selbst umfasst lediglich 3 Bits, was für die oben genannten 8
 * Einträge ausreicht.
 */
public class Stack {

    private Integer[] stack;
    static int pointer = 0;

    public Stack() {
         stack = new Integer[8];
    }

    public void printStack(){
        for (Integer integer : stack) {
            System.out.println(String.format("0x%02X",integer));
        }
    }

    public Integer[] getStack() {
        return stack;
    }

    /**
     *
     * @param iVal value to push on stack
     */
    public void pushOnStack(Integer iVal) {
        stack[pointer] = iVal;
        pointer++;
        if (pointer == 8) {
            pointer = 0;
        }
    }

    public Integer pop() {
        Integer iRet = 0;
        pointer--;
        if (pointer < 0) {
            pointer = 7;
        }
        iRet = stack[pointer];
        stack[pointer] = null;


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
