package com.headfirst.servicebrowser.services;

import com.headfirst.servicebrowser.Service;

import javax.swing.*;

/**
 * Created by Tom on 8/5/2016.
 */
/*
Test-only service - maximum simplicity
 */
public class BaseService implements Service {

    private JLabel lbl;
    private Dice dice;

    @Override
    public JPanel getGuiPanel() {
        JButton btn = new JButton("Button");
        btn.addActionListener((ev) -> flipText());
        lbl = new JLabel("aaa");

        JPanel controls = new JPanel();
        controls.add(lbl);
        controls.add(btn);

        dice = new Dice();
        JButton btn2 = new JButton("DIce");
        btn2.addActionListener((ev) -> roll());
        controls.add(btn2);
        controls.add(dice);

        JPanel main = new JPanel();
        main.add(controls);
        return main;
    }

    private void roll() {
        dice.rollTheDice();
    }

    private void flipText() {
        if (lbl.getText().equals("aaa")) {
            lbl.setText("bbb");
        } else {
            lbl.setText("aaa");
        }
    }
}
