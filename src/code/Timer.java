package code;

public class Timer {
    public static int timerIncrementCount = 0;
    public static int initPrescalerVal = 0;
    public static boolean disableTimer = false;
    public static int cycle = 0;

    public static boolean timerInterrupt = false;

    public void incrementTimer0(int prescalerValue) {

        if (disableTimer == false) {
            if (initPrescalerVal != prescalerValue) {
                Decoder.obj.ram.setTMR0(0);
                initPrescalerVal = prescalerValue;
                timerIncrementCount = prescalerValue;
            }
            timerIncrementCount--;

            if (timerIncrementCount == 0) {
                Decoder.obj.ram.incrementTMR0();
                timerIncrementCount = initPrescalerVal;
                timerInterrupt = true;
            } else if (timerIncrementCount < 0) {
                int ilocal = timerIncrementCount;
                if (ilocal < 0) {
                    ilocal *= (-1);
                }
                timerIncrementCount = initPrescalerVal;
                timerIncrementCount -= ilocal;
                timerInterrupt = true;
                Decoder.obj.ram.incrementTMR0();
            }
        }
    }

    public boolean checkForInterrupt() {
        boolean ret = false;
        if (Decoder.obj.ram.getTMR0() == 0 && Decoder.obj.ram.getSpecificStatusBit(2) == 1) {
            return true;
        }
        return ret;
    }


}
