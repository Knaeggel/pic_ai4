package code;

public class Ram {
    private Integer[][] ram;


    /**
     * decides which bank is chosen
     */
    public static int bank = 0;

    public static int wRegister;


    public Ram() {
        ram = new Integer[2][128];
        setStatus(0b00011000);
    }


    public void setStatus(Integer i) {
        ram[0][3] = i;
        ram[1][3] = i;
    }

    /**
     * TODO
     * @param b
     * @return
     */
    public static Integer setZeroBit(boolean b) {
        int iRet = 0;
        if (b) {

        }
        return iRet;
    }


}
