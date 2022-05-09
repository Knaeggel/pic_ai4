package code;

public class Timer {
    public static int timerIncrementCount = 0;
    public static int initPrescalerVal = 0;

    public static boolean endLoop = false;


    public void incrementTimer0(int prescalerValue) {

        if (initPrescalerVal != prescalerValue) {
            initPrescalerVal = prescalerValue;
            timerIncrementCount = prescalerValue;
        }
        if (!endLoop) {
            timerIncrementCount--;
        }

        if (timerIncrementCount == 0) {
            Decoder.obj.ram.incrementTMR0();
            timerIncrementCount = initPrescalerVal;
        }
    }
    public boolean changeOnPrescaler() {

        return false;
    }
}
