package code;

public class Prescaler {

    /**
     * @param option_reg option register
     * @return calced prescaler val
     */
    public int calcPrescaleValueFromOptionReg(int option_reg) {
        int ret = 256;
        if (isPrescalerAssigned(option_reg) == 0) {
            ret = (int) Math.pow(2, (option_reg & 0b111) + 1);
        }
        return ret;
    }

    /**
     * checks if prescaler is assigned to WDT(1) or TMR0(0)
     *
     * @param option_reg option register
     * @return 1 for WDT and 0 for TMR0
     */
    public int isPrescalerAssigned(int option_reg) {
        return Decoder.obj.ram.getNthBitOfValue(3, option_reg);
    }
}
