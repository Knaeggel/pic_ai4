package gui;

import code.*;

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
    private JLabel prescalerValue;
    private JLabel irpValue;
    private JLabel rp1Vlaue;
    private JLabel rp0Value;
    private JLabel t0Value;
    private JLabel pdValue;
    private JLabel zValue;
    private JLabel dcValue;
    private JLabel cValue;
    private JLabel rpuValue;
    private JLabel iegVlaue;
    private JLabel tcsValue;
    private JLabel tseVlaue;
    private JLabel psaValue;
    private JLabel ps2Value;
    private JLabel ps1Value;
    private JLabel ps0Value;
    private JPanel Timing;
    private JLabel rifValue;
    private JLabel ifValue;
    private JLabel tifValue;
    private JLabel rieValue;
    private JLabel ieValue;
    private JLabel tieValue;
    private JLabel eieValue;
    private JLabel gieValue;
    private JList indexlist0;
    private JList valuelist0;
    private JScrollPane bank0address;
    private JScrollPane bank0value;
    private JList indexlist1;
    private JList valuelist1;
    private JPanel Bank1;
    private JPanel Bank0;
    private JScrollPane bank1address;
    private JScrollPane bank1value;
    private JLabel timerIncrementCountField;
    private JLabel portB;
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
                Decoder.obj.ram.printGeneralAndMapped();

                lstList.setSelectedIndex(LSTFileReader.lineNumber.get(Ram.programmCounter).intValue());
                lstList.ensureIndexIsVisible(lstList.getSelectedIndex() + 5);

                lstList.setCellRenderer(new CellRenderer());

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

                if (vergleichen(selectedLST, lstList.getSelectedIndex())) {

                    selectedLST.add(lstList.getSelectedIndex());
                    lstList.setSelectionForeground(color);
                    lstList.setOpaque(true);
                    int[] arr = selectedLST.stream().mapToInt(i -> i).toArray();
                    lstList.setSelectedIndices(arr);


                    //
                    valueList.add(lstList.getSelectedValuesList().get(lstList.getSelectedValuesList().size() - 1));


                } else {

                    selectedLST.remove(position(selectedLST, lstList.getSelectedIndex()));

                    int[] arr = selectedLST.stream().mapToInt(i -> i).toArray();
                    lstList.clearSelection();
                    lstList.setSelectedIndices(arr);

                    valueList.remove(lstList.getSelectedValue());
                }


            }
        });
/*
        pinB0.addActionListener(listenerPinPortB);
        pinB1.addActionListener(listenerPinPortB);
        pinB2.addActionListener(listenerPinPortB);
        pinB3.addActionListener(listenerPinPortB);
        pinB4.addActionListener(listenerPinPortB);
        pinB5.addActionListener(listenerPinPortB);
        pinB6.addActionListener(listenerPinPortB);
        pinB7.addActionListener(listenerPinPortB);

 */

        pinA5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pinA5.isSelected() == true)
                Decoder.obj.timer.incrementTimer0WithRa4(Decoder.obj.ram.getPrescalerValue());
            }
        });
    }


    ActionListener listenerPinPortB = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (Decoder.obj.ram.getSpecificOptionBit(7) == 0) {
                Decoder.obj.ram.setPortB(
                        pinB0.isSelected(),
                        pinB1.isSelected(),
                        pinB2.isSelected(),
                        pinB3.isSelected(),
                        pinB4.isSelected(),
                        pinB5.isSelected(),
                        pinB6.isSelected(),
                        pinB7.isSelected());
            }
        }
    };

    /**
     * TODO addidtions need to be made
     */
    public void updateGui() {
        if (checkEnablePortB() == true) {
            Decoder.obj.ram.setPortB(pinB0.isSelected(), pinB1.isSelected(),
                    pinB2.isSelected(), pinB3.isSelected(),
                    pinB4.isSelected(), pinB5.isSelected(),
                    pinB6.isSelected(), pinB7.isSelected());
        }


        updateLstList();
        updateSFR();
        updateSFRBit();
        updateBanks();
        syncronizeScrollbar();
    }

    public boolean RB0Checked() {
        return pinB0.isSelected();
    }

    /**
     * checks if one of the pins from RB4 to RB7 is checked
     *
     * @return true if one is checked
     */
    public boolean RB4toRB7Checked() {
        if (pinB4.isSelected() || pinB5.isSelected() || pinB6.isSelected() || pinB7.isSelected()) {
            return true;
        }
        return false;
    }

    /**
     * resets a selected pin
     *
     * @param pin pin you want to reset
     */
    public void resetUsedPortBPin(int pin) {
        if (pin == 0) {
            pinB0.setSelected(false);
        }
        if (pin == 4) {
            pinB4.setSelected(false);
        }
        if (pin == 5) {
            pinB5.setSelected(false);
        }
        if (pin == 6) {
            pinB6.setSelected(false);
        }
        if (pin == 7) {
            pinB7.setSelected(false);
        }

    }

    /**
     * RBPU: PORTB Pull-up Enable bit
     * 1 = PORTB pull-ups are disabled
     * 0 = PORTB pull-ups are enabled (by individual port latch values)
     *
     * @return if enabled or not
     */
    public boolean checkEnablePortB() {
/*
        if (Decoder.obj.ram.getSpecificOptionBit(7) == 1) {
            pinB0.setEnabled(false);
            pinB1.setEnabled(false);
            pinB2.setEnabled(false);
            pinB3.setEnabled(false);
            pinB4.setEnabled(false);
            pinB5.setEnabled(false);
            pinB6.setEnabled(false);
            pinB7.setEnabled(false);
            return false;
        } else if (Decoder.obj.ram.getSpecificOptionBit(7) == 0) {
            pinB0.setEnabled(true);
            pinB1.setEnabled(true);
            pinB2.setEnabled(true);
            pinB3.setEnabled(true);

            if (Decoder.obj.ram.getSpecificIntconBit(3) == 5) {

                if (Decoder.obj.ram.getSpecificIntconBit(3) == 1) {
                    pinB4.setEnabled(true);
                    pinB5.setEnabled(true);
                    pinB6.setEnabled(true);
                    pinB7.setEnabled(true);
                }
                if (Decoder.obj.ram.getSpecificIntconBit(3) == 0) {
                    pinB4.setEnabled(false);
                    pinB5.setEnabled(false);
                    pinB6.setEnabled(false);
                    pinB7.setEnabled(false);
                }
            }

            return true;

        }

        return false;
*/

        updatePortBByTrisB();
        return true;
    }

    public void updatePortBByTrisB() {
        if (Decoder.obj.ram.getSpecificOptionBit(7) == 0) {
            if (Decoder.obj.ram.getSpecificTrisBBit(0) == 1) {
                pinB0.setEnabled(true);
            } else {
                pinB0.setEnabled(false);
            }
            if (Decoder.obj.ram.getSpecificTrisBBit(1) == 1) {
                pinB1.setEnabled(true);
            } else {
                pinB1.setEnabled(false);
            }
            if (Decoder.obj.ram.getSpecificTrisBBit(2) == 1) {
                pinB2.setEnabled(true);
            } else {
                pinB2.setEnabled(false);
            }
            if (Decoder.obj.ram.getSpecificTrisBBit(3) == 1) {
                pinB3.setEnabled(true);
            } else {
                pinB3.setEnabled(false);
            }
            if (Decoder.obj.ram.getSpecificTrisBBit(4) == 1) {
                pinB4.setEnabled(true);
            } else {
                pinB4.setEnabled(false);
            }
            if (Decoder.obj.ram.getSpecificTrisBBit(5) == 1) {
                pinB5.setEnabled(true);
            } else {
                pinB5.setEnabled(false);
            }
            if (Decoder.obj.ram.getSpecificTrisBBit(6) == 1) {
                pinB6.setEnabled(true);
            } else {
                pinB6.setEnabled(false);
            }
            if (Decoder.obj.ram.getSpecificTrisBBit(7) == 1) {
                pinB7.setEnabled(true);
            } else {
                pinB7.setEnabled(false);
            }
        } else {
            pinB0.setEnabled(false);
            pinB1.setEnabled(false);
            pinB2.setEnabled(false);
            pinB3.setEnabled(false);
            pinB4.setEnabled(false);
            pinB5.setEnabled(false);
            pinB6.setEnabled(false);
            pinB7.setEnabled(false);
        }

    }

    public void updateSFR() {
        wValue.setText(String.format("0x%02X", Ram.wRegister));
        pclValue.setText(String.format("0x%02X", Decoder.obj.ram.getPCL()));
        pclathValue.setText(String.format("0x%02X", Decoder.obj.ram.getPCLATH()));
        statusValue.setText(String.format("0x%02X", Decoder.obj.ram.getStatus()));
        fsrValue.setText(String.format("0x%02X", Decoder.obj.ram.getFSR()));
        optionValue.setText(String.format("0x%02X", Decoder.obj.ram.getOption()));
        timer0Value.setText(String.format("0x%02X", Decoder.obj.ram.getTMR0()));
        prescalerValue.setText("1:" + Decoder.obj.ram.getPrescalerValue());



    }

    public void updateSFRBit() {
        irpValue.setText(Decoder.obj.ram.getSpecificStatusBit(7) + "");
        rp1Vlaue.setText(Decoder.obj.ram.getSpecificStatusBit(6) + "");
        rp0Value.setText(Decoder.obj.ram.getSpecificStatusBit(5) + "");
        t0Value.setText(Decoder.obj.ram.getSpecificStatusBit(4) + "");
        pdValue.setText(Decoder.obj.ram.getSpecificStatusBit(3) + "");
        zValue.setText(Decoder.obj.ram.getSpecificStatusBit(2) + "");
        dcValue.setText(Decoder.obj.ram.getSpecificStatusBit(1) + "");
        cValue.setText(Decoder.obj.ram.getSpecificStatusBit(0) + "");

        rpuValue.setText(Decoder.obj.ram.getSpecificOptionBit(7) + "");
        iegVlaue.setText(Decoder.obj.ram.getSpecificOptionBit(6) + "");
        tcsValue.setText(Decoder.obj.ram.getSpecificOptionBit(5) + "");
        tseVlaue.setText(Decoder.obj.ram.getSpecificOptionBit(4) + "");
        psaValue.setText(Decoder.obj.ram.getSpecificOptionBit(3) + "");
        ps2Value.setText(Decoder.obj.ram.getSpecificOptionBit(2) + "");
        ps1Value.setText(Decoder.obj.ram.getSpecificOptionBit(1) + "");
        ps0Value.setText(Decoder.obj.ram.getSpecificOptionBit(0) + "");

        rifValue.setText(Decoder.obj.ram.getSpecificIntconBit(0) + "");
        ifValue.setText(Decoder.obj.ram.getSpecificIntconBit(1) + "");
        tifValue.setText(Decoder.obj.ram.getSpecificIntconBit(2) + "");
        rieValue.setText(Decoder.obj.ram.getSpecificIntconBit(3) + "");
        ieValue.setText(Decoder.obj.ram.getSpecificIntconBit(4) + "");
        tieValue.setText(Decoder.obj.ram.getSpecificIntconBit(5) + "");
        eieValue.setText(Decoder.obj.ram.getSpecificIntconBit(6) + "");
        gieValue.setText(Decoder.obj.ram.getSpecificIntconBit(7) + "");

    }


    /**
     * Aktualisiert den Stack in der GUI
     *
     * @param stack
     */
    public void updateStack(Stack stack) {
        Integer[] aiStack = stack.getStack();

        Stack8.setText(String.format("0x%04X", aiStack[0]));
        Stack7.setText(String.format("0x%04X", aiStack[1]));
        Stack6.setText(String.format("0x%04X", aiStack[2]));
        Stack5.setText(String.format("0x%04X", aiStack[3]));
        Stack4.setText(String.format("0x%04X", aiStack[4]));
        Stack3.setText(String.format("0x%04X", aiStack[5]));
        Stack2.setText(String.format("0x%04X", aiStack[6]));
        Stack1.setText(String.format("0x%04X", aiStack[7]));

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

    public void breakpoints() {
        allCommands = LSTFileReader.getCommands();
        if (Ram.programmCounter < allCommands.size()) {
            if (!selectedLST.isEmpty()) {
                if (valueList.contains(allCommands.get(Ram.programmCounter))) {
                    t1.stop();
                }
            }
        }
    }

    public void updateBanks() {
        // Bank0
        String[] indexAddress = new String[128];
        String[] indexValue = new String[128];
        String[] indexValueB = new String[128];
        String[] indexAddressB = new String[128];

        Integer[][] ram2 = Decoder.obj.ram.getRam();
        for (int i = 0; i < 128; i++) {
            indexAddress[i] = String.format("0x%02X", i);
            indexValue[i] = String.format("0x%02X", ram2[0][i]);
            indexAddressB[i] = String.format("0x%02X", i);
            indexValueB[i] = String.format("0x%02X", ram2[1][i]);
        }

        indexAddress[0] += " INDF";
        indexAddress[1] += " TMR0";
        indexAddress[2] += " PCL";
        indexAddress[3] += " STATUS";
        indexAddress[4] += " FSR";
        indexAddress[5] += " PORTA";
        indexAddress[6] += " PORTB";
        indexAddress[8] += " EEDATA";
        indexAddress[9] += " EEADR";
        indexAddress[0x0A] += " PCLATH";
        indexAddress[0x0B] += " INTCON";
        indexlist0.setListData(indexAddress);
        valuelist0.setListData(indexValue);
        // Bank1
        indexAddressB[0] += " INDF";
        indexAddressB[1] += " OPTION";
        indexAddressB[2] += " PCL";
        indexAddressB[3] += " STATUS";
        indexAddressB[4] += " FSR";
        indexAddressB[5] += " TRISA";
        indexAddressB[6] += " TRISB";
        indexAddressB[8] += " EECON1";
        indexAddressB[9] += " EECON2";
        indexAddressB[0x0A] += " PCLATH";
        indexAddressB[0x0B] += " INTCON";

        indexlist1.setListData(indexAddressB);
        valuelist1.setListData(indexValueB);

    }

    public void syncronizeScrollbar() {
        // Bank0
        JScrollBar sBar1 = bank0address.getVerticalScrollBar();
        JScrollBar sBar2 = bank0value.getVerticalScrollBar();
        sBar2.setModel(sBar1.getModel());

        // Bank1
        JScrollBar sBar3 = bank1address.getVerticalScrollBar();
        JScrollBar sBar4 = bank1value.getVerticalScrollBar();
        sBar3.setModel(sBar2.getModel());
        sBar4.setModel(sBar3.getModel());
    }



}
