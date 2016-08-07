package com.headfirst.servicebrowser.services;

import com.headfirst.servicebrowser.Service;

import javax.swing.*;

/**
 * Created by Tom on 8/3/2016.
 */
/*
Any class inside Serializable class becomes serializable.

When changes to code seems not to impact the exception, think twice if the
running code is the one changed.
Confused once by
- server daemon serving objects not up to date

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
