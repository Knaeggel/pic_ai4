package gui;

import code.Decoder;
import code.Stack;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


    boolean stopFlag = true;
    boolean statusSchleife = false;

    int block = 0;



    public MainFrame() {
        setContentPane(panel1);
        setTitle("PIC GUI");
        setSize(1150, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Start");


            }
        });

        /**
         * stop button
         */
        btnStopp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Decoder.obj.decoder.stopT1();
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
                System.out.println("C: "+Decoder.obj.ram.getSpecificStatusBit(0));
                System.out.println("DC: "+Decoder.obj.ram.getSpecificStatusBit(1));
                System.out.println("Z: "+Decoder.obj.ram.getSpecificStatusBit(2));
            }
        });

        /**
         * start button
         */
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Decoder.obj.decoder.startT1();
            }
        });
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



}
