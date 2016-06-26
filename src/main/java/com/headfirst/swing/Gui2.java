package com.headfirst.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Created by Tom on 4/28/2016.
 */
/*
Swing basics continued
 */
public class Gui2 {
    private JFrame win;
    private JButton colorChangeBtn;
    private JButton labelChangeBtn;
    private JLabel lbl;
    private int size = 500;
    private int xCircle = 0;
    private int yCircle = 0;

    public Gui2() {
        win = new JFrame("GUI playground");
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Gui2 gui = new Gui2();
        gui.makeWidgets();
        gui.show();
        gui.animateCircle();
    }

    public void show() {
        win.setSize(size, size);
        win.setVisible(true);
    }

    void makeWidgets() {
        colorChangeBtn = new JButton("Change color");
        labelChangeBtn = new JButton("Change label");
        lbl = new JLabel("I'm a label");

        colorChangeBtn.addActionListener(new ColorChangeListener());
        labelChangeBtn.addActionListener(new LabelChangeListener());

        win.getContentPane().add(BorderLayout.SOUTH, colorChangeBtn);
        win.getContentPane().add(BorderLayout.EAST, labelChangeBtn);
        win.getContentPane().add(BorderLayout.WEST, lbl);
        win.getContentPane().add(BorderLayout.CENTER ,new Circle1());
    }

    class Circle1 extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
//            GradientPaint gradient = new GradientPaint(20, 20, Color.blue,
//                    150, 150, Color.green);
            g.setColor(Color.magenta);
            g.fillOval(xCircle, yCircle, 70, 70);
        }
    }
    void changeBtnColorToRandom() {
        Random rand = new Random(System.currentTimeMillis());
        Color randomColor = new Color(rand.nextInt(256), rand.nextInt(256),
                rand.nextInt(256));
        colorChangeBtn.setBackground(randomColor);
    }

/*
inner class facts:
- access instance vars of outer class
- provide more implementations for single interface
- de-facto support multiple inheritance through inner classes
*/
    class LabelChangeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String text = lbl.getText();
            if (text.contains("label")) {
                lbl.setText("I'm a cop");
            } else {
                lbl.setText("I'm a label");
            }
            win.repaint();
        }

}
    class ColorChangeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            changeBtnColorToRandom();
            win.repaint();
        }

    }

    void animateCircle() {
        for (int i = 0; i < 100; i++) {
            xCircle++;
            yCircle++;
            win.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
    }
}
