
import code.Decoder;
import code.Ram;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    Decoder decoder = new Decoder();
    @Before
    public void setUp(){

        decoder.decodeString(".\\res\\Sim1.txt");
    }


    @Test
    public void testWreg() {
        Assertions.assertEquals(0x25, Ram.wRegister);
    }

}