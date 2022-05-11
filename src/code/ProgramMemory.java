package code;

import java.util.ArrayList;

/*
 * Class for decoded lst file
 * */
public class ProgramMemory {
    //static Integer[] memory = new Integer[1024];
    public ArrayList<Integer> memory;
    public ArrayList<Integer> cycleList;


    public static int stopStackoverflow = 0;

    public ProgramMemory() {
        memory = new ArrayList<>();
        cycleList = new ArrayList<>();
    }


    /**
     * checks if there was already a cycle of a specific routine
     *
     * @param i
     * @return false if ther was no cycle yet and true if it already had a cycle
     */
    public boolean checkCycle(int i) {
        boolean bRet = false;

        for (int i1 = 0; i1 < cycleList.size(); i1++) {
            if (i == cycleList.get(i1)) {
                bRet = true;
            }
        }
        return bRet;
    }

    /**
     * skips next instruction
     */
    public void skipNextInstruction() {
        Ram.programmCounter++;
    }
}
