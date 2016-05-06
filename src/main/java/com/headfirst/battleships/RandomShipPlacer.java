package com.headfirst.battleships;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Tom on 3/4/2016.
 */
/*
Generate random location where to place a ship of given size
status: done

Board starts at 0, 0
Axis: x horizontal, y vertical
*/
public class RandomShipPlacer {
    private int boxSize;    // size of battle board side
    private ArrayList<Point> shipLoc = new ArrayList<>();
    private int shipSize;
    private Random rand = new Random(); // init once to get unique sequence

    public RandomShipPlacer(int boxSize, int shipSize) {
        this.boxSize = boxSize-1;
        this.shipSize = shipSize;
    }

    public RandomShipPlacer(int boxSize) {
        this(boxSize, 2);
    }

    public void setBoxSize(int size) {
        boxSize = size-1;
    }

    public static void main(String[] args) {
        RandomShipPlacer placer = new RandomShipPlacer(7, 100);
        for (int i = 0; i < 10; i++) {
            placer.generateLocation();
            placer.drawShip();
        }
    }

//    returns zero size collection for box smaller than ship size
    public ArrayList<Point> generateLocation() {
        int retry = 3;
        for (int i = 0; i < retry; i++) {
            Point p = pickRandomPoint();
            shipLoc.add(p);

            while (shipLoc.size() < shipSize) {
                p = growPointBounded(p);
                if (p == null) {
                    System.out.println("[point could not grow] " + shipLoc
                            .toString());
                    break; // retry from new random point
                }
                shipLoc.add(p);
            }

            if (shipLoc.size() == shipSize) {
                break;
            }
            shipLoc.clear();
        }
        System.out.println("[loc] " + shipLoc.toString());
        return shipLoc;
    }

    Point pickRandomPoint() {
        return new Point(rand.nextInt(boxSize+1), rand.nextInt(boxSize+1));
    }

//    Grow point in random direction but respect box boundary
    Point growPointBounded(Point p1) {
        Point p2;
        ArrayList<Integer> directions = new ArrayList<>(Arrays.asList(0, 90,
                180, 270));
        Collections.shuffle(directions);
        for (Integer direct : directions) {
            p2 = growPoint(p1, direct);
            if (isWithinBox(p2) && !shipLoc.contains(p2)) {
                return p2;
            }
        }
        return null;
    }

    Point growPoint(Point p1, int direction) {
        Point p2 = new Point(p1.x, p1.y);
        switch (direction) {
            case 0: p2.translate(1, 0);
                break;
            case 90: p2.translate(0, 1);
                break;
            case 180: p2.translate(-1, 0);
                break;
            case 270: p2.translate(0, -1);
                break;
        }
        return p2;
    }

    public boolean isWithinBox(Point p) {
        return p.x >= 0 && p.x <= boxSize && p.y >= 0 && p.y <= boxSize;
    }

//    Unused method
    public int comparePoints(Point p1, Point p2) {
        if (p1.x > p2.x) {
            return -1;
        } else if (p1.x < p2.x) {
            return 1;
        } else {
            if (p1.y > p2.y) {
                return -1;
            } else if (p1.y < p2.y) {
                return 1;
            }
        }
        return 0;
    }

    void drawShip() {
//        x axis legend
        for (int i = 0; i <= boxSize ; i++) {
            if (i == 0) {
                System.out.print("  ");
            }
            System.out.print(i + " ");
        }
        System.out.println();
        for (int y = 0; y <= boxSize; y++) {
            System.out.print(y + " ");
            for (int x = 0; x <= boxSize; x++) {
                if (shipLoc.contains(new Point(x, y))) {
                    System.out.print("@ ");
                } else {
                    System.out.print("~ ");
                }
            }
            System.out.println();
        }
    }
}
