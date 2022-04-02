package code;

public class Ram {
    private static Integer[][] ram;


    /**
     * decides which bank is chosen
     */
    public static int bank = 0;

    public static int wRegister;


    public Ram() {
        ram = new Integer[2][128];
        setStatus(0b0001_1000);
    }

    public static Integer[][] getRam() {
        return ram;
    }


    public void setStatus(Integer i) {
        ram[0][3] = i;
        ram[1][3] = i;
    }

    public static Integer getStatus() {
        return ram[bank][3];
    }

    /**
     * TODO
     *
     * @param b
     * @return
     */
    public static Integer setZeroBit(boolean b) {
        Integer iRet = getStatus();
        if (b) {
            iRet += 0b100;
        }
        return iRet;
    }


}
