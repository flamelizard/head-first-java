package com.headfirst.quizcards;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.headfirst.quizcards.Serialize.serialize;

/**
 * Created by Tom on 6/19/2016.
 */
/*
QuickCard format saved to text file:
q1, a1,, q2, a2,,
 */
public class QuizCardBuilder extends JPanel {

    public static final Font GUI_FONT = new Font("Sherif", Font.BOLD, 20);
    private final JTextArea question;
    private final JTextArea answer;
    private ArrayList<QuizCard> createdCards = new ArrayList<>();

    public QuizCardBuilder() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(new JLabel("Question:"));
        question = new JTextArea(5, 20);
        question.setFont(GUI_FONT);
        question.setLineWrap(true);
        add(question);
        add(new JLabel("Answer:"));
        answer = new JTextArea(5, 20);
        answer.setFont(GUI_FONT);
        answer.setLineWrap(true);
        add(answer);
        JButton nextCard = new JButton("Next card");
        nextCard.addActionListener(
                (actionEvent) -> moveToNextCard());
        add(nextCard);

        JFrame frame = new JFrame("QuickCardBuilder");
        frame.setJMenuBar(getMenu());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(300, 200);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        QuizCardBuilder builder = new QuizCardBuilder();
    }

    private JMenuBar getMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(
                (actionEvent) -> saveToFile());
        menu.add(save);
        bar.add(menu);
        return bar;
    }

    private void moveToNextCard() {
        String text1 = question.getText().trim();
        String text2 = answer.getText().trim();
        if (text1.length() == 0 || text2.length() == 0) {
            return;
        }
        QuizCard currentCard = new QuizCard(text1, text2);
        createdCards.add(currentCard);

        question.setText("");
        answer.setText("");
        System.out.println("[cards] " + createdCards);
    }

    private void saveToFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File saveFile = chooser.getSelectedFile();
        try {
            serialize(saveFile, createdCards.toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
