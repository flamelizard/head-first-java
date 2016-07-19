package com.headfirst.midi.beatboxv2;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 7/8/2016.
 */
/*
Switchboard refactored out from BeatBox since I've noticed it has got many
switchboard only methods.
 */
public class SwitchBoard {
    private JCheckBox[][] board;
    private int[] instruments;  // each instrument index corresponds to the switch row
    private int boardLength;    // sequence length per an instrument

    public SwitchBoard(int boardLength, int[] instruments) {
        this.instruments = instruments;
        this.boardLength = boardLength;
        createBoard();
    }

    private void createBoard() {
        JCheckBox[][] var1 = new JCheckBox[instruments.length][boardLength];
        for (int i = 0; i < instruments.length; i++) {
            for (int j = 0; j < boardLength; j++) {
                var1[i][j] = new JCheckBox();
            }
        }
        this.board = var1;
    }

    public JCheckBox[][] getBoard() {
        return board;   // object reference
    }

    public void resetBoard() {
        for (int i = 0; i < instruments.length; i++) {
            for (int j = 0; j < boardLength; j++) {
                board[i][j].setSelected(false);
            }
        }
    }

    public List<Boolean> getBoardAsBoolean() {
        List<Boolean> var1 = new ArrayList<>();
        for (int i = 0; i < instruments.length; i++) {
            for (int j = 0; j < boardLength; j++) {
                if (board[i][j].isSelected()) {
                    var1.add(true);
                } else {
                    var1.add(false);
                }
            }
        }
        return var1;
    }

    public void setBoardFromBoolean(List<Boolean> var1) {
        int row;
        for (int i = 0; i < var1.size(); i += boardLength) {
            for (int j = 0; j < boardLength; j++) {
                row = i / boardLength;
                board[row][j].setSelected(var1.get(i + j));
            }
        }
    }
}
