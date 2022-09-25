package com.berryworks.jquantify;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MetricTest {

    Metric mMetric;

    @Before
    public void setUp() {
        mMetric = new MyMetricType("testCounter");
    }

    @Test
    public void canGetLabel() {
        assertEquals("testCounter", mMetric.getLabel());
    }

    @Test
    public void testDefaultConstructor() {
        mMetric = new MyMetricType();
    }

    @Test
    public void testBasics() {
        mMetric = new EventCounter("ec");

        float age = mMetric.getAge();
        long ageMillis = mMetric.getAgeMillis();
        int intervalSeconds = mMetric.getIntervalSeconds();
        String label = mMetric.getLabel();
        long totalIntervals = mMetric.getTotalIntervals();

        assertEquals(0f, age, 0.1);
        assertTrue(ageMillis >= 0 && ageMillis < 500);
        assertEquals(1, intervalSeconds);
        assertEquals("ec", label);
        assertEquals(1, totalIntervals);
    }

    static class MyMetricType extends Metric {

        public MyMetricType(String inString) {
            super(inString);
        }

        public MyMetricType() {
        }

        @Override
        public void add(int inCount) {
        }

        @Override
        public long getCount() {
            return 0;
        }
    }
}
