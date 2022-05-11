package code;

public class Timer {
    public static int timerIncrementCount = 0;
    public static int initPrescalerVal = 0;


    public void incrementTimer0(int prescalerValue) {

        if (initPrescalerVal != prescalerValue) {
            Decoder.obj.ram.setTMR0(0);
            initPrescalerVal = prescalerValue;
            timerIncrementCount = prescalerValue;
        }
        timerIncrementCount--;

        if (timerIncrementCount == 0) {
            Decoder.obj.ram.incrementTMR0();
            timerIncrementCount = initPrescalerVal;
        }
    }
    public boolean changeOnPrescaler() {

        return false;
    }
}
