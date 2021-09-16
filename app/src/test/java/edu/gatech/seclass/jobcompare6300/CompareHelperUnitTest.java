package edu.gatech.seclass.jobcompare6300;

import org.junit.Assert;
import org.junit.Test;

import edu.gatech.seclass.jobcompare6300.ui.util.CompareHelper;

public class CompareHelperUnitTest {

    /*
     * TEST CASE #34 - Checks that job scores are correct using
     * https://piazza.com/class/kdrfwoqul5fx0?cid=642 as an example.
     */
    @Test
    public void checkCalculateScoreDifferentWeights() {
        CompareHelper compareHelper = new CompareHelper();
        Double result = compareHelper.calculateJobScore(1, 5, 4,
                3, 2,
                3.0, 90000.0, 10000.0,
                3.0, 10, 5);

        Assert.assertEquals(new Double(29847.29), result);
    }

    /*
     * TEST CASE #34 - Checks that job scores returns correct value when weights are default
     */
    @Test
    public void checkCalculateScoreDefaultWeights() {
        CompareHelper compareHelper = new CompareHelper();
        Double result = compareHelper.calculateJobScore(1, 1, 1,
                1, 1,
                3.0, 90000.0, 10000.0,
                3.0, 10, 5);

        Assert.assertEquals(new Double(13758.19), result);
    }

    /*
     * TEST CASE #34 - Checks that job scores returns correct value when weights are upperbound.
     */
    @Test
    public void checkCalculateScoreUpperBoundsWeights() {
        CompareHelper compareHelper = new CompareHelper();
        Double result = compareHelper.calculateJobScore(100, 100, 100,
                100, 100,
                3.0, 90000.0, 10000.0,
                3.0, 10, 5);

        Assert.assertEquals(new Double(13758.19), result);
    }
}
