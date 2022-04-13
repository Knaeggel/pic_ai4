
import code.Decoder;
import code.Ram;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class Sim2Test {
    Decoder decoder = new Decoder();

    @Before
    public void setUp() {

        decoder.decodeString(".\\res\\Sim2.txt");
        for (int i = 0; i < 100; i++) {
            decoder.nextStep();
        }
    }

    @Test
    public void testWreg() {
        Assertions.assertEquals(0x36, Ram.wRegister);
    }


    @Test
    public void testCarry() {
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificStatusBit(0));
    }

    @Test
    public void testDigitCarry() {
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificStatusBit(1));
    }

    @Test
    public void testZero() {
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificStatusBit(2));
    }

}