package com.headfirst.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Created by Tom on 4/27/2016.
 */
public class Gui1 implements ActionListener {
    JButton btn;
    JFrame win;

    public static void main(String[] args) {
        Gui1 gui = new Gui1();
        gui.doBasics();
    }

    void doBasics() {
        win = new JFrame("Basic GUI");
        btn = new JButton("Click me");

        win.getContentPane().add(btn);
//        strangely ,default close is to hide the window
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        class implements ActionListener interface, many avail. interfaces
        btn.addActionListener(this);

        win.getContentPane().add(new MyWidget());

        win.setSize(500, 500);
        win.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        btn.setText("Well done! " + ev.getActionCommand());
    }

    //    make your own widget to draw whatever you like
    class MyWidget extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.orange);
            g.fillOval(this.getWidth() / 2, this.getHeight() / 2, 50, 50);

//            draw image file
            URL file = getClass().getResource("/magic_box.jpg");
            if (file == null) {
                System.out.println("Image file not found!");
                return;
            }
            Image im = new ImageIcon(file).getImage();
            g.drawImage(im, 0, 0, this);

//            polymorphism in action
            Graphics2D g2D = (Graphics2D) g;
//            draw rectangle with raised outline
            g2D.fill3DRect(250, 250, 500, 500, true);
        }
    }
}



