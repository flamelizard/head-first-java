package com.headfirst.battleships;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Tom on 3/2/2016.
 */
public class Board {
    private ArrayList<Ship> ships = new ArrayList<>();
    private int numPlayerTurns;
    private int size;

    public Board(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Board size must be non-zero");
        }
        this.size = size;
    }

    public Board() {
        this(8);
    }
    public int getNumPlayerTurns() {
        return numPlayerTurns;
    }
    public ArrayList<Ship> getShips() {
        return ships;
    }

    public void makeShips(int n) {
        ships.clear();
        RandomShipPlacer placer = new RandomShipPlacer(size);
        for (int i = 0; i < n; i++) {
            Ship ship = new Ship("Rosol rules");
            ArrayList<String> loc = new ArrayList<>();
            for (Point p: placer.generateLocation()) {
                String coord = "" + convertToLetter(p.x) + p.y;
                loc.add(coord);
            }
            ship.setLocation(loc);
            ships.add(ship);
        }
    }

    public void playerTurn(String loc) {
        boolean beenHit = false;
        for (Ship sh : ships) {
            if (sh.isHit(loc)) {
                System.out.println("hit");
                if (sh.isDown()) {
                    System.out.println("<" + sh.getName() + "> goes down");
                }
                beenHit = true;
            }
        }
        if (!beenHit) {
            System.out.println("miss");
        }
        numPlayerTurns++;
    }

    /*
Make single random point and grow it in x axis until number of coordinates
equals size.
This method has been replaced by class "RandomShipPlacer"
*/
    public String[] generateRandLocation(int size) {
        String[] cells = new String[size];
        String alphabet = "abcdefqhijklmnopqrstuvwxyz";
        int x = (int) (Math.random() * 7 + 1);
        char y = alphabet.charAt((int) (Math.random() * 7 + 1));

        for (int i = 0; i < cells.length; i++) {
            cells[i] = y + String.valueOf(x);
            x += 1;
//            System.out.println("[cell] " + cells[i]);
        }
        return cells;
    }

    public int convertFromLetter(String loc) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        char letter = loc.toLowerCase().charAt(0);
        for (int i = 0; i < alphabet.length(); i++) {
            if (letter == alphabet.charAt(i)) {
                return i + 1;
            }
        }
        return -1;
    }

    public String convertToLetter(int loc) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        if (alphabet.length() < loc) {
            throw new IndexOutOfBoundsException("Location index is beyond " +
                    "alphabet");
        }
        return String.valueOf(alphabet.charAt(loc));
    }

    public boolean allShipsDown() {
        int downed = 0;
        for (Ship sh: ships) {
            if (sh.isDown()) {
                downed++;
            }
        }
        return ships.size() == downed;
    }
}
