
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
        for (int i = 0; i < 20; i++) {
            decoder.nextStep();
        }
    }

    @Test
    public void testWreg() {
        Assertions.assertEquals(0x36, Ram.wRegister);
    }

/*
    @Test
    public void testStatusRegister() {
        //System.out.println(Integer.toBinaryString(Decoder.obj.ram.getStatus()));

        //System.out.println("C: " + Decoder.obj.ram.getSpecificBit(0));
        //System.out.println("DC: "+Decoder.obj.ram.getSpecificBit(1));
        //System.out.println("Z: "+Decoder.obj.ram.getSpecificBit(2));
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificBit(0));
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificBit(1));
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificBit(2));
    }
*/

    @Test
    public void testCarry() {
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificBit(0));
    }

    @Test
    public void testDigitCarry() {
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificBit(1));
    }

    @Test
    public void testZero() {
        Assertions.assertEquals(0, Decoder.obj.ram.getSpecificBit(2));
    }

}