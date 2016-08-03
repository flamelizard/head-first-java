package com.headfirst.servicebrowser.services;

import com.headfirst.servicebrowser.Service;

import javax.swing.*;

/**
 * Created by Tom on 8/3/2016.
 */
public class DayOfTheWeekService implements Service {
    private DayOfTheWeek dotw;

    public DayOfTheWeekService() {
        dotw = new DayOfTheWeek();
    }

    @Override
    public JPanel getGuiPanel() {
        return dotw;
    }
}
