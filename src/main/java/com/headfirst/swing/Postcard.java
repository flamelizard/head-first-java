package com.headfirst.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 5/20/2016.
 */
/*
LESSONS LEARNT
JPanel setSize() has no effect on panel's size. Panel grows or shrinks with
the components it has to accommodate. Better to use .pack() on JFrame that will
make the window fit all components.

Swing is a tool for making GUI with buttons, labels and text fields. It is
NOT a good fit for making graphics like this postcard.

GridBag is unnecessary complicated layout manager based on StackOverflow
comments. Better sticking with simpler managers and structuring layout
through heavy use of panels and thus grouping widgets having close relationship.

Warning, pitfalls
Cannot reuse JPanel object in multiple JFrames. I've tried to see difference
in layout managers through using Desktop and JInternalFrame but only last one
 has shown widgets.  In situation like this, you might try to clone object if
 supported (not in this case).

IDE GUI Builder
At first glance, it does not seem to speed up the process by using IDE GUI
Builder. You still have to define various parameters and the final source
code is not a Java but very dense XML. People seem to prefer own code for
simple GUIs unless you have a killer commercial builder.
 */

// Status: done
public class Postcard extends JPanel {

    private JPanel fromWrapper = new JPanel();
    private JPanel toWrapper = new JPanel();

    Postcard() {
        makeGui();

//        Select different layout implementations
//        addToFlowLayout();
//        addToBorderLayout();
        addToGridBagLayout();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Postcard GUI");

        Postcard postcard = new Postcard();
        frame.setContentPane(postcard);
//        frame.setSize(800, 800);
        frame.pack();
        frame.setLocation(300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    void makeGui() {
        JPanel fromArea = getAddressSection();
        JPanel stampRelief = new StampRelief();

        makePanelStandOut(fromWrapper, "from");
        fromWrapper.setLayout(new BoxLayout(fromWrapper, BoxLayout.PAGE_AXIS));
        fromWrapper.add(fromArea);
        fromWrapper.add(Box.createVerticalStrut(50));   //empty space separator
        fromWrapper.add(stampRelief);

        makePanelStandOut(toWrapper, "to");
        JPanel toArea = getAddressSection();
        JPanel stamp = new StampRegular();
        makePanelStandOut(stamp, "Stamp");
        toWrapper.setLayout(new BoxLayout(toWrapper, BoxLayout.PAGE_AXIS));
        toWrapper.add(stamp);
        toWrapper.add(Box.createVerticalStrut(30));
        toWrapper.add(toArea);
    }

    void addToFlowLayout() {
//        Postcard class represent main pane, extends JPanel
        setLayout(new FlowLayout());
        add(fromWrapper);
        add(toWrapper);
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Flow layout"),
                        BorderFactory.createDashedBorder(Color.blue)
                ));
    }

    /*
    LINE_START equals EAST
     */
    void addToBorderLayout() {
        setLayout(new BorderLayout());
        add(fromWrapper, BorderLayout.LINE_START);
        add(toWrapper, BorderLayout.LINE_END);
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Border layout"),
                        BorderFactory.createDashedBorder(Color.blue)
                ));
    }

    /*
    GridBag is better replaced by simpler layout managers. While GridBag is
    flexible and powerful, it is also obscure and difficult to read / write.
    */
    void addToGridBagLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints constr = new GridBagConstraints();

        constr.gridx = 0;
        add(fromWrapper, constr);
        constr.gridx = 1;
        add(toWrapper, constr);
        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("GridBag layout"),
                        BorderFactory.createDashedBorder(Color.blue)
                ));

    }

    public JPanel getAddressSection() {
        JPanel addressSection = new JPanel();
//        addressSection.setSize(sectionSize, sectionSize);

        JPanel labels = new JPanel();
//        addressSection.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        labels.setLayout(new BoxLayout(labels, BoxLayout.PAGE_AXIS));
        labels.add(new JLabel("Name:"));
        labels.add(new JLabel("Street:"));
        labels.add(new JLabel("City:"));

        JPanel textInput = new JPanel();
        textInput.setLayout(new BoxLayout(textInput, BoxLayout.PAGE_AXIS));
        textInput.add(new JTextField(10));
        textInput.add(new JTextField(10));
        textInput.add(new JTextField(10));

        addressSection.add(labels);
        addressSection.add(textInput);
        return addressSection;
    }

    class StampRelief extends JPanel {
        //        Must !! Otherwise drawing is shown only as couple of pixels
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(100, 100);
        }

        @Override
        public void paintComponent(Graphics g) {
/*
concentric circles drawn in rectangle box
their coordinates spread is 1/2 of difference between its diameter and the
first circle
*/
            g.setColor(Color.red);
            g.drawOval(0, 0, 80, 80);
            g.fillOval(5, 5, 70, 70);
            g.setColor(Color.white);
            g.fillOval(25, 25, 30, 30);
        }
    }

    class StampRegular extends StampRelief {
        int sizeX = 50;
        int sizeY = 30;

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.blue);
//            draw rect in top right corner
            g.fillRect(getWidth() - sizeX, 0, 50, 30);
        }
    }

    /*
    Super useful to display border of individual panels !!
    Helps to see how panels are laid out in the frame
     */
    void makePanelStandOut(JPanel panel, String title) {
//        panel.setBorder(
//                BorderFactory.createTitledBorder(title)
//        );
    }

//    To show multiple frames at once, Desktop and JInternalFrame can be used
//    void makeContentFrames() {
//        desktop = new JDesktopPane();
//        int numFrames = 3;
//        int x = 0;
//        int y = 0;
//        for (int i = 0; i < numFrames; i++) {
////            enable frame to maximize, minimize etc.
//            JInternalFrame frame = new JInternalFrame(
//                    "Frame" + i, true, true, true, true
//            );
//            frame.setLocation(x, y);
////            frame.pack();     // set size when JPanel has been added
//            frame.setVisible(true);
//            desktop.add(frame);
//            x += 20;
//            y += 20;
//        }
//    }
//
//    void addComponentToGridBag(Component comp, int x, int y) {
//        GridBagConstraints c = new GridBagConstraints();
//        c.gridx = x;
//        c.gridy = y;
//        add(comp, c);
//    }
//
//    void makeGuiWithObscureGridBag() {
//        setLayout(new GridBagLayout());
//        setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createTitledBorder("Postcard sample"),
//                BorderFactory.createEmptyBorder(10, 10, 10, 10)
//        ));
//
//        JLabel stamp = new JLabel("1.0 USD");
//        stamp.setHorizontalAlignment(JLabel.CENTER);
//        stamp.setBackground(Color.cyan);
//        stamp.setBorder(
//                BorderFactory.createLineBorder(Color.blue)
//        );
//
//        JLabel From = new JLabel("From:");
//        JLabel FromAddress = new JLabel("Address:");
//        JTextField FromText = new JTextField(10);
//        JTextField FromAddressText = new JTextField(10);
//
//
//        GridBagConstraints constr = new GridBagConstraints();
//
//        constr.ipadx = 5;
//        constr.gridx = 0;
//        constr.gridy = 0;
//        add(From, constr);
//
//        constr = new GridBagConstraints();
//        constr.gridx = 1;
//        constr.gridy = 0;
//        add(FromText, constr);
//
//        constr = new GridBagConstraints();
//        constr.anchor = GridBagConstraints.FIRST_LINE_END;
//        constr.ipadx = 15;
//        constr.ipady = 15;
//        constr.gridx = 8;
//        add(stamp, constr);
//
//    }
}
