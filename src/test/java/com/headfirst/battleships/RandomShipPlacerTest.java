package com.headfirst.battleships;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Tom on 3/4/2016.
 */
public class RandomShipPlacerTest {
    private RandomShipPlacer placer;
    int boardSize = 7;
    int shipSize = 4;

    @Before
    public void setUp() {
        placer = new RandomShipPlacer(boardSize, shipSize);
    }

    @Test
    public void comparePoint() {
        int res;
        int[] pointsAndResult = {
                0, 0, 0, 5, 1,
                1, 1, 2, 1, 1,
                4, 3, 3, 8, -1,
                4, 5, 4, 1, -1,
                -1, 4, -1, 4, 0,
        };
        for (int i = 0; i < pointsAndResult.length; i = i + 5) {
            int[] p = Arrays.copyOfRange(pointsAndResult, i, i + 5);
//            System.out.println(Arrays.toString(p));
            res = placer.comparePoints(new Point(p[0], p[1]),
                    new Point(p[2], p[3]));
            Assert.assertEquals("Points comparison", res, p[4]);
        }
    }

    @Test
    public void generateLocation() {
        for (int i = 0; i < 20; i++) {
            RandomShipPlacer placer = new RandomShipPlacer(7, 4);
            Assert.assertEquals("Ship size does not match", 4,
                    placer.generateLocation().size());
        }

        placer = new RandomShipPlacer(5, 26);
        assertThat("Ship length bigger than the box",
                placer.generateLocation().size(), is(0));
    }


    @Test
    public void pointWithinBox() {
        int[] pointsAndResult = {
                0, 0, 1,
                5, -1, 0,
                -1, 5, 0,
                boardSize, boardSize, 0,
                boardSize, 0, 0,
                boardSize - 1, boardSize - 1, 1,
        };
        for (int i = 0; i < pointsAndResult.length; i = i + 3) {
            Point p = new Point(pointsAndResult[i], pointsAndResult[i + 1]);
            boolean result = pointsAndResult[i + 2] == 1;
            assertThat(String.format("<%s> in or out board", p),
                    placer.isWithinBox(p), is(result));
        }
    }
}
