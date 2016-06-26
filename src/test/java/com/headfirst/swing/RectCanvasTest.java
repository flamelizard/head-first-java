package com.headfirst.swing;

import org.junit.Test;

import java.awt.*;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Tom on 5/8/2016.
 */
public class RectCanvasTest {

    @Test
    public void returnsPointsWithinGivenBounds() throws Exception {
        BeatBoxGui gui = new BeatBoxGui();
        BeatBoxGui.RectCanvas canvas = gui.new RectCanvas();

        int[] testVals = {
                100, 10,
                100, 2,
                10, 20
        };

        int min, variance;
        Point p;
        for (int i = 0; i < testVals.length; i += 2) {
            min = testVals[i];
            variance = testVals[i + 1];
            p = canvas.getRandomPointBounded(min, variance);

            System.out.println("[point " + p);
            assertThat(p.getX(), greaterThanOrEqualTo((double) min - variance));
            assertThat(p.getX(), lessThanOrEqualTo((double) min + variance));

            assertThat(p.getY(), greaterThanOrEqualTo((double) min - variance));
            assertThat(p.getY(), lessThanOrEqualTo((double) min + variance));
        }
    }

}