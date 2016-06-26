package com.headfirst.quizcards;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 6/19/2016.
 */
public class QuizCardPlayer extends JPanel {

    private final JTextArea display = new JTextArea(5, 20);
    private ArrayList<QuizCard> loadedCards = new ArrayList<>();
    private int activeCardId = 0;

    public QuizCardPlayer() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        display.setFont(QuizCardBuilder.GUI_FONT);
        display.setLineWrap(true);
        display.setEditable(false);
        add(display);

        JButton showAnswer = new JButton("Show answer");
        showAnswer.addActionListener(
                (actionEvent) -> this.showActiveAnswer()
        );
        JButton nextCard = new JButton("Show next card");
        nextCard.addActionListener(
                (event) -> showNextCard()
        );
        JPanel bottomBar = new JPanel();
        bottomBar.add(showAnswer);
        bottomBar.add(nextCard);
        add(bottomBar);

        JFrame frame = new JFrame("Quick Card Player");
        frame.setJMenuBar(getMenu());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(300, 200);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        QuizCardPlayer player = new QuizCardPlayer();
    }

    private JMenuBar getMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(
                (actionEvent) -> loadFromFile());
        menu.add(load);
        bar.add(menu);
        return bar;
    }

    private void moveToNextCard() {
        activeCardId++;
        if (activeCardId >= loadedCards.size()) {
            activeCardId = 0;
        }
    }

    private QuizCard getActiveCard() {
        if (loadedCards.size() == 0) {
            return null;
        }
        return loadedCards.get(activeCardId);
    }

    private void showNextCard() {
        moveToNextCard();
        showActiveQuestion();
    }

    public void showActiveAnswer() {
        QuizCard card = getActiveCard();
        if (card != null) {
            display.setText(card.getAnswer());
        }
    }

    private void showActiveQuestion() {
        QuizCard card = getActiveCard();
        if (card != null) {
            display.setText(card.getQuestion());
        }
    }

    private void loadFromFile() {
        JFileChooser chooser = new JFileChooser();
        int status = chooser.showOpenDialog(this);
        if (status == JFileChooser.CANCEL_OPTION) {
            return;
        }
        try {
            List<Object> objects = Serialize.deserializeAll(
                    chooser.getSelectedFile()
            );
            loadedCards.clear();
            for (Object obj : objects) {
                loadedCards.add((QuizCard) obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        activeCardId = 0;
        showActiveQuestion();
    }
}
