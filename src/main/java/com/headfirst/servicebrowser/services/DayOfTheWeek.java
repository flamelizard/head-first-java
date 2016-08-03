package com.headfirst.servicebrowser.services;

import com.headfirst.servicebrowser.Service;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormatSymbols;
import java.util.*;
import java.util.List;

/**
 * Created by Tom on 8/1/2016.
 */
/*
Enter any date and show what day of the week it is (sunday, monday...)
 */
public class DayOfTheWeek extends JPanel implements Service {
    private final JComboBox<Integer> year;
    private final JComboBox<String> month;
    private final JComboBox<Integer> day;
    private final JTextField textDOTW;

    private Calendar calc = Calendar.getInstance();

    public DayOfTheWeek() {
        JPanel mainArea = new JPanel();
        mainArea.setLayout(new BoxLayout(mainArea, BoxLayout.PAGE_AXIS));

        JLabel question = new JLabel("When were you born?");
        question.setHorizontalAlignment(JLabel.LEFT);

        DateFormatSymbols dateFmt = new DateFormatSymbols();
        int border = 3;

        year = new JComboBox<>(
                new Integer[]{2010, 2011, 2012, 2013});
        year.setEditable(true);
        year.setBorder(BorderFactory.createEmptyBorder(
                border, border, border, border));

        month = new JComboBox<>(
                Arrays.copyOfRange(dateFmt.getMonths(), 0, 12));
        month.setEditable(true);
        month.setBorder(BorderFactory.createEmptyBorder(
                border, border, border, border));

        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            days.add(i + 1);
        }
        day = new JComboBox<>(days.toArray(new Integer[]{}));
        day.setEditable(true);
        month.setBorder(BorderFactory.createEmptyBorder(
                border, border, border, border));

        JPanel comboBar = new JPanel();
        comboBar.setLayout(new BoxLayout(comboBar, BoxLayout.LINE_AXIS));
        comboBar.add(year);
        comboBar.add(month);
        comboBar.add(day);

        JButton send = new JButton("Send");
        send.addActionListener((event) -> calculateDayOfTheWeek());
        textDOTW = new JTextField(10);
        textDOTW.setFont(new Font("Helvetica", Font.PLAIN, 14));

        JPanel bottomBar = new JPanel();
        bottomBar.add(send);
        bottomBar.add(Box.createHorizontalStrut(50));
        bottomBar.add(textDOTW);

        mainArea.add(question);
        mainArea.add(comboBar);
        mainArea.add(bottomBar);

        add(mainArea);
        setPreferredSize(new Dimension(500, 200));
    }

    public static void main(String[] args) {
        DayOfTheWeek dotw = new DayOfTheWeek();

        JFrame frame = new JFrame("Day of the week");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(dotw.getGuiPanel());
        frame.pack();
        frame.setLocation(300, 200);
        frame.setVisible(true);
    }

    public JPanel getGuiPanel() {
        return this;
    }

    private void calculateDayOfTheWeek() {
        Map<String, Integer> months = calc.getDisplayNames(
                Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        calc.set((int) year.getSelectedItem(),
                months.get(month.getSelectedItem()),
                (int) day.getSelectedItem());

        textDOTW.setText(calc.getDisplayName(
                Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));

    }

    private void printDate(Calendar c) {
        String month = c.getDisplayName(
                Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        System.out.printf("%s-%s %s",
                month, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
    }

}
