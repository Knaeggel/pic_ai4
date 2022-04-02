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
}
