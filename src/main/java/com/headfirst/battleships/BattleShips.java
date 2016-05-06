package com.headfirst.battleships;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tom on 3/2/2016.
 */
public class BattleShips {
    int boardSize = 8;
    int numShips = 2;

    public static void main(String[] args) {
        BattleShips battle = new BattleShips();
        battle.play();
    }

    public void play() {
        String reply = "";
        Board board = new Board(boardSize);
        board.makeShips(numShips);

        while (!reply.equals("q")) {
            System.out.println("Take a guess");
            reply = getUserInput();
            board.playerTurn(reply);
            if (board.allShipsDown()) {
                System.out.println("Congrats ... No ships remain");
                break;
            }
        }
        System.out.println("Number of guesses = " + board.getNumPlayerTurns());
        System.out.println("Quit");
    }

    public static String getUserInput() {
        String reply = "";
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        try {
            reply = keyboard.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return reply;
    }
}
