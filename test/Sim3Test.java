import code.Decoder;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class Sim3Test {
    Decoder decoder = new Decoder();

    @Before
    public void setUp() {

        decoder.decodeString(".\\res\\Sim3.txt");
        for (int i = 0; i < 1000; i++) {
            decoder.nextStep();
        }
        do {

        } while (decoder.obj.programMemory.checkCycle(21) == false);

    }




    @Test
    public void testCarry() {
        Assertions.assertEquals(1, Decoder.obj.ram.getSpecificStatusBit(0));
    }

    @Test
    public void testDigitCarry() {
        Assertions.assertEquals(1, Decoder.obj.ram.getSpecificStatusBit(1));
    }

    @Test
    public void testZero() {
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificStatusBit(2));
    }

    @Test
    public void testW1W2(){
        Assertions.assertEquals(0xD9, Decoder.obj.ram.getRamAt(0x0C));
        Assertions.assertEquals(0x60, Decoder.obj.ram.getRamAt(0x0D));
    }
    @Test
    public void testWreg() {
        Assertions.assertEquals(0x79, Decoder.obj.ram.wRegister);
    }
}