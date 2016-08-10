package com.headfirst.servicebrowser.services;

import com.headfirst.servicebrowser.Service;

import javax.swing.*;

/**
 * Created by Tom on 7/27/2016.
 */
/*
Box class cannot set background color for the container. Use another JPanel.

getGuiPanel()
For unknown reason, container is always null once it is passed by
getGuiPanel, need to re-init JPanel on each call.

It seems once JPanel added to a frame in Service browser, it disappears
from here.

Init-ing JPanel in constructor cause Dice to not animate.
 */
public class DiceService implements Service {
    private JPanel container;
    private Dice dice;

    public DiceService() {
    }

    public static void main(String[] args) {
        DiceService service = new DiceService();

        JFrame frame = new JFrame("Dice service");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(service.getGuiPanel());
        frame.pack();
        frame.setLocation(300, 200);
        frame.setVisible(true);
    }

    @Override
    public JPanel getGuiPanel() {
        if (container == null) {
            populateDiceContainer();
        }
        return container;
    }

    private void populateDiceContainer() {
        container = new JPanel();
        int border = 50;
        container.setBorder(BorderFactory.createEmptyBorder(
                border, border, border, border));

        dice = new Dice();
        JButton rollBtn = new JButton("Roll'em!");
        rollBtn.addActionListener((event) -> rollTheDice());

//        Box centerColumn = Box.createVerticalBox();
        JPanel centerColumn = new JPanel();
        centerColumn.setLayout(new BoxLayout(centerColumn, BoxLayout.PAGE_AXIS));
        centerColumn.add(dice);
        centerColumn.add(Box.createVerticalStrut(10));
        centerColumn.add(rollBtn);

        container.add(centerColumn);
    }

    private void rollTheDice() {
        dice.rollTheDice();
    }
}
