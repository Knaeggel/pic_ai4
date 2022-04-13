package code;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LSTFileReader {


    public static File myObj;


    private static ArrayList<Integer> decodeList = new ArrayList<>();
    private static ArrayList<Integer> pcList = new ArrayList<>();
    private static ArrayList<Integer> operationValue = new ArrayList<>();
    private static ArrayList<Integer> opcode = new ArrayList<>();
    private static ArrayList<Integer> last11Bits = new ArrayList<>();

    public static void read(String src) {

        // File
        try {
            myObj = new File(src);

            Scanner myReader = new Scanner(myObj);
            myReader.reset();

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.substring(0, 1).isBlank()) {

                    decodeList.add(Integer.decode("0x" + data.substring(5, 9)));
                    opcode.add(Integer.decode("0x" + data.substring(5, 7)));
                    operationValue.add(Integer.decode("0x" + data.substring(7, 9)));
                    pcList.add(Integer.decode("0x" + data.substring(0, 4)));

                    Integer iVal = Integer.decode("0x" + data.substring(6, 9));
                    if (iVal > 0x7FF) {
                        iVal -= 0b1000_0000_0000;
                        last11Bits.add(iVal);
                    } else {
                        last11Bits.add(iVal);
                    }



                }
            }



            myReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the whole operation vlaue 5,9
     */
    public static ArrayList<Integer> getDecodeList() {
        return decodeList;
    }

    /**
     * @return the programmCounter 0,4
     */
    public static ArrayList<Integer> getPcList() {
        return pcList;
    }

    /**
     * @return the operation value 7,9
     */
    public static ArrayList<Integer> getOperationValue() {
        return operationValue;
    }

    public static Integer getOpcode(int i) {
        return opcode.get(i);
    }

    /**
     * @return the operation value 5,7
     */
    public static ArrayList<Integer> getOpcode() {
        return opcode;
    }

    /**
     * no safety if 12th bit is 1
     * @return the last 11 bits
     */
    public static ArrayList<Integer> getLast11Bits() {
        return last11Bits;
    }

}

