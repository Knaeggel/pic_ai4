package gui;

import code.Decoder;
import code.LSTFileReader;
import code.Stack;
import code.MyThread;
import code.Ram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private JButton btnStart;
    private JPanel panel1;
    private JButton btnReset;
    private JButton btnStopp;
    private JButton btnOneStep;
    private JPanel bedienemelente;
    private JPanel Ports;
    private JPanel PortA;
    private JPanel PortB;
    private JPanel StackPanel;
    private JLabel Stack1;
    private JLabel Stack2;
    private JLabel Stack3;
    private JLabel Stack4;
    private JLabel Stack5;
    private JLabel Stack6;
    private JLabel Stack7;
    private JLabel Stack8;
    private JCheckBox checkBoxA0;
    private JCheckBox checkBoxA1;
    private JCheckBox checkBoxA2;
    private JCheckBox checkBoxA4;
    private JCheckBox checkBoxA5;
    private JCheckBox checkBoxA6;
    private JCheckBox checkBoxA3;
    private JLabel TrisA;
    private JCheckBox checkBoxA7;
    private JLabel PinA;
    private JCheckBox pinA0;
    private JCheckBox pinA1;
    private JCheckBox pinA2;
    private JCheckBox pinA3;
    private JCheckBox pinA5;
    private JLabel TrisB;
    private JCheckBox checkBoxB3;
    private JCheckBox checkBoxB2;
    private JCheckBox checkBoxB0;
    private JCheckBox checkBoxB1;
    private JLabel pinB;
    private JCheckBox checkBoxB4;
    private JCheckBox checkBoxB5;
    private JCheckBox checkBoxB6;
    private JCheckBox checkBoxB7;
    private JCheckBox pinB0;
    private JCheckBox pinB1;
    private JCheckBox pinB2;
    private JCheckBox pinB3;
    private JCheckBox pinB4;
    private JCheckBox pinB5;
    private JCheckBox pinB6;
    private JCheckBox pinB7;
    private JScrollPane ProtgrammLST;
    private JList lstList;
    private JLabel wRegister;
    private JLabel wValue;
    private JLabel pclValue;
    private JLabel pclathValue;
    private JLabel statusValue;
    private JLabel fsrValue;
    private JLabel optionValue;
    private JLabel timer0Value;
    private ArrayList<String> allLST = LSTFileReader.getAllLines();
    private static ArrayList<Integer> selectedLST = new ArrayList<>();
    private static ArrayList<String> allCommands = LSTFileReader.getCommands();

    private static ArrayList<Object> valueList = new ArrayList<>();

    MyThread t1;


    public MainFrame() {

        setContentPane(panel1);
        setTitle("PIC GUI");
        setSize(1150, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        /**
         * stop button
         */
        btnStopp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t1.stop();
            }
        });


        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        /**
         * next Step
         */
        btnOneStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Decoder.obj.decoder.nextStep();
                //Decoder.obj.ram.printZDCC();
                //Decoder.obj.ram.printGeneralAndMapped();


                if (!selectedLST.isEmpty()) {
                    System.out.println(Ram.programmCounter);
                    if (!vergleichen(selectedLST, Ram.programmCounter)) {
                        t1.stop();
                    }
                }
                updateGui();
            }
        });

        /**
         * start button
         */
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                t1 = new MyThread();
            }
        });

        /**
         * Soll die LST File einlesen und Breakpoints setzten
         */
        lstList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Color color = Color.red;

                if (vergleichen(selectedLST,lstList.getSelectedIndex())) {

                    selectedLST.add(lstList.getSelectedIndex());
                    lstList.setSelectionForeground(color);
                    lstList.setOpaque(true);
                    int[] arr = selectedLST.stream().mapToInt(i -> i).toArray();
                    lstList.setSelectedIndices(arr);


                    //
                      valueList.add(lstList.getSelectedValue());
                   // valueList.add(e.getPoint());

                } else {

                    selectedLST.remove(position(selectedLST, lstList.getSelectedIndex()));

                    int[] arr = selectedLST.stream().mapToInt(i -> i).toArray();
                    lstList.clearSelection();
                    lstList.setSelectedIndices(arr);

                    //
                    valueList.remove(lstList.getSelectedValue());
                }


            }
        });

    }

    public void updateGui() {
        updateLstList();
        updateSFR();
    }

    public void updateSFR() {
        wValue.setText("0x" + Integer.toHexString(Ram.wRegister));
        pclValue.setText(Decoder.obj.ram.getPCL() + "");
        pclathValue.setText("0x" + Integer.toHexString(Decoder.obj.ram.getPCLATH()));
        statusValue.setText("0b" + Integer.toBinaryString(Decoder.obj.ram.getStatus()));
        fsrValue.setText("0x" + Integer.toHexString(Decoder.obj.ram.getFSR()));
        optionValue.setText("0x" + Integer.toHexString(Decoder.obj.ram.getOption()));
        timer0Value.setText("0x" + Integer.toHexString(Decoder.obj.ram.getTMR0()));

    }

    /**
     * Aktualisiert den Stack in der GUI
     *
     * @param stack
     */
    public void updateStack(Stack stack) {
        Integer[] aiStack = stack.getStack();

        int count = 0;


        for (int i = 0; i < aiStack.length; i++) {
            if (aiStack[i] != null) {
                count++;
                switch (count) {
                    case 1:
                        Stack8.setText(String.format("0x%04X", aiStack[i]));
                        break;
                    case 2:
                        Stack7.setText(String.format("0x%04X", aiStack[i]));
                        break;
                    case 3:
                        Stack6.setText(String.format("0x%04X", aiStack[i]));
                        break;
                    case 4:
                        Stack5.setText(String.format("0x%04X", aiStack[i]));
                        break;
                    case 5:
                        Stack4.setText(String.format("0x%04X", aiStack[i]));
                        break;
                    case 6:
                        Stack3.setText(String.format("0x%04X", aiStack[i]));
                        break;
                    case 7:
                        Stack2.setText(String.format("0x%04X", aiStack[i]));
                        break;
                    case 8:
                        Stack1.setText(String.format("0x%04X", aiStack[i]));
                        break;
                    default:
                        System.out.println("Default updateStack");
                        break;
                }
            }
        }
    }

    public void updateLstList() {
        lstList.setListData(allLST.toArray());
        int[] arr = selectedLST.stream().mapToInt(i -> i).toArray();
        lstList.setSelectedIndices(arr);
    }

    public boolean vergleichen(ArrayList<Integer> arrList, int value) {

        if (!arrList.isEmpty()) {
            for (int i = 0; i < arrList.size(); i++) {
                if (arrList.get(i) == value) {
                    return false;
                }
            }
        }
        return true;
    }

    public int position(ArrayList<Integer> arrList, int value) {

        for (int i = 0; i < arrList.size(); i++) {
            if (arrList.get(i) == value) {
                return i;
            }
        }

        return 0;
    }

    public void breakpoints(){
        allCommands = LSTFileReader.getCommands();
        if(Ram.programmCounter < allCommands.size()) {
            if (!selectedLST.isEmpty()) {
                if (valueList.contains(allCommands.get(Ram.programmCounter))) {
                    t1.stop();
                }
            }
        }
    }
}
