package com.headfirst.servicebrowser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Tom on 7/27/2016.
 */
/*
Box class cannot set background color for the container. Use another JPanel.
 */
public class DiceService implements Service {
    private JPanel container;

    public DiceService() {
        container = new JPanel();
        int border = 50;
        container.setBorder(BorderFactory.createEmptyBorder(
                border, border, border, border));
//        container.setBackground(Color.DARK_GRAY);

        Dice dice = new Dice();
        JButton rollBtn = new JButton("Roll'em!");
        rollBtn.addActionListener((event) -> dice.rollTheDice());

//        Box centerColumn = Box.createVerticalBox();
        JPanel centerColumn = new JPanel();
        centerColumn.setBackground(Color.red);
        centerColumn.setLayout(new BoxLayout(centerColumn, BoxLayout.PAGE_AXIS));
        centerColumn.add(dice);
        centerColumn.add(Box.createVerticalStrut(10));
        centerColumn.add(rollBtn);

        container.add(centerColumn);
    }

    public static void main(String[] args) {
        DiceService service = new DiceService();

        JFrame frame = new JFrame("Dice service");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(service.getGuiPanel());
        frame.pack();
        frame.setLocation(800, 500);
        frame.setVisible(true);
    }

    @Override
    public JPanel getGuiPanel() {
        return container;
    }
}
