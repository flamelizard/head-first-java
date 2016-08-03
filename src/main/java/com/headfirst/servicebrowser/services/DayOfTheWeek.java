package com.headfirst.servicebrowser.services;

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

GridLayout
Set number of rows and columns, 0 means no limit

Spacing between cells setHgap, setVgap. Change at will during runtime
directly on layout manager.

Quite nice and esy manager.
 */
public class DayOfTheWeek extends JPanel {
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

        JButton send = new JButton("Send");
        send.addActionListener((event) -> calculateDayOfTheWeek());
        textDOTW = new JTextField(10);
        textDOTW.setFont(new Font("Helvetica", Font.PLAIN, 14));

        mainArea.setLayout(getCustomLayout());
        mainArea.add(question);
        mainArea.add(Box.createHorizontalBox());
        mainArea.add(Box.createHorizontalBox());
        mainArea.add(year);
        mainArea.add(month);
        mainArea.add(day);
        mainArea.add(Box.createHorizontalBox());
        mainArea.add(send);
        mainArea.add(textDOTW);

        add(mainArea);
        setPreferredSize(new Dimension(500, 200));
    }

    public static void main(String[] args) {
        DayOfTheWeek dotw = new DayOfTheWeek();

        JFrame frame = new JFrame("Day of the week");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(dotw);
        frame.pack();
        frame.setLocation(300, 200);
        frame.setVisible(true);
    }

    public static void printDate(Calendar c) {
        String month = c.getDisplayName(
                Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        System.out.printf("%s-%s %s",
                month, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
    }

    private LayoutManager getCustomLayout() {
        int gap = 5;
        int cols = 0;
        int rows = 3;
        GridLayout layout = new GridLayout(cols, rows);
        layout.setHgap(gap);
        layout.setVgap(gap);
        return layout;
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

}
