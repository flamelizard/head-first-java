package com.headfirst.servicebrowser.services;

import com.headfirst.servicebrowser.GuiUtils;

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

BUGS
1. When a month has less than 31 days, selecting 31sth day will show day of
the week for the following day
 */
public class DayOfTheWeek extends JPanel {
    private JComboBox<Integer> year;
    private JComboBox<String> month;
    private JComboBox<Integer> day;
    private JTextField textDOTW;

    private Calendar calc = Calendar.getInstance();

    public DayOfTheWeek() {
        JPanel mainArea = new JPanel();
        mainArea.setLayout(new BoxLayout(mainArea, BoxLayout.PAGE_AXIS));

        JLabel question = new JLabel("When were you born?");
        question.setHorizontalAlignment(JLabel.LEFT);

        JButton send = new JButton("Send");
        send.addActionListener((event) -> calculateDayOfTheWeek());
        textDOTW = new JTextField(10);
        textDOTW.setFont(new Font("Helvetica", Font.PLAIN, 14));

        initBirthDateComboBoxes();

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
        calculateDayOfTheWeek();
    }

    public static void main(String[] args) {
        GuiUtils.showInFrame("DOTW", new DayOfTheWeek());
    }

    public static void printDate(Calendar c) {
        String month = c.getDisplayName(
                Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        System.out.printf("%s-%s %s",
                month, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
    }

    private void initBirthDateComboBoxes() {
        int border = 3;
        DateFormatSymbols dateFmt = new DateFormatSymbols();

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
    }

    private LayoutManager getCustomLayout() {
        int gap = 5;
        int rows = 0;
        int cols = 3;
        GridLayout layout = new GridLayout(rows, cols);
        layout.setHgap(gap);
        layout.setVgap(gap);
        return layout;
    }

    private void calculateDayOfTheWeek() {
        Map<String, Integer> monthNames = calc.getDisplayNames(
                Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        calc.set((int) year.getSelectedItem(),
                monthNames.get(month.getSelectedItem()),
                (int) day.getSelectedItem());

        textDOTW.setText(calc.getDisplayName(
                Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
    }

}
