
import code.Decoder;
import code.Ram;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class Sim1Test {
    Decoder decoder = new Decoder();

    @Before
    public void setUp() {

        decoder.decodeString(".\\res\\Sim1.txt");
        for (int i = 0; i < 20; i++) {
            decoder.nextStep();
        }
    }

    @Test
    public void testWreg() {
        Assertions.assertEquals(0x25, Ram.wRegister);
    }


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


}