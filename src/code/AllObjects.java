package code;

import com.sun.jdi.request.DuplicateRequestException;
import gui.MainFrame;

public class AllObjects {

    public ALU alu;
    public Stack stack;
    public Ram ram;
    public MainFrame mainFrame;
    public Decoder decoder;
    public ProgramMemory programMemory;

    private static AllObjects obj;

    public AllObjects() {
        alu = new ALU();
        stack = new Stack();
        ram = new Ram();
        mainFrame = new MainFrame();
        decoder = new Decoder();
        programMemory = new ProgramMemory();
    }

    public static AllObjects getAllObjectsInstance() {
        if (obj == null) {
            obj = new AllObjects();
        } else {
            throw new DuplicateRequestException("Multiple instances of AllObjects");
        }
        return obj;
    }


}
