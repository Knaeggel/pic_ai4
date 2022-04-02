package gui;

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
    private JPanel Stack;
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

        btnStopp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }

    /**
     * Aktualisiert den Stack in der GUI
     * @param aiStack
     */
    public void updateStack(Integer[] aiStack){
        int count = 0;

        for(int i = 0; i < aiStack.length; i++){
            if(aiStack[i] != null) {
                count++;
                switch (count) {
                    case 1:
                        Stack8.setText(aiStack[i].toString());
                        break;
                    case 2:
                        Stack7.setText(aiStack[i].toString());
                        break;
                    case 3:
                        Stack6.setText(aiStack[i].toString());
                        break;
                    case 4:
                        Stack5.setText(aiStack[i].toString());
                        break;
                    case 5:
                        Stack4.setText(aiStack[i].toString());
                        break;
                    case 6:
                        Stack3.setText(aiStack[i].toString());
                        break;
                    case 7:
                        Stack2.setText(aiStack[i].toString());
                        break;
                    case 8:
                        Stack1.setText(aiStack[i].toString());
                        break;
                    default:
                        System.out.println("Default updateStack");
                        break;
                }
            }
        }
    }


}
