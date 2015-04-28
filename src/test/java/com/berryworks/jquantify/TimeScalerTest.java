package com.berryworks.jquantify;

import com.berryworks.jquantify.util.TimeScaler;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeScalerTest {
    private static final float K = 1000.0f;
    private static final float M = 1000 * K;

    private TimeScaler mTimeScaler;

    @Test
    public void testTimeScaler10f() {
        TimeScaler timeScaler = new TimeScaler(10.0f);
        assertEquals("ms", timeScaler.getUnits());
        assertEquals(0.0f, timeScaler.scale(0.0f), 0.01f);
        assertEquals(1.0f, timeScaler.scale(1.0f), 0.01f);
        assertEquals(2.0f, timeScaler.scale(2.0f), 0.01f);
        assertEquals(3.0f, timeScaler.scale(3.0f), 0.01f);
        assertEquals(4.0f, timeScaler.scale(4.0f), 0.01f);
        assertEquals(5.0f, timeScaler.scale(5.0f), 0.01f);
        assertEquals(6.0f, timeScaler.scale(6.0f), 0.01f);
        assertEquals(7.0f, timeScaler.scale(7.0f), 0.01f);
        assertEquals(8.0f, timeScaler.scale(8.0f), 0.01f);
        assertEquals(9.0f, timeScaler.scale(9.0f), 0.01f);
        assertEquals(10.0f, timeScaler.scale(10.0f), 0.01f);
        assertEquals(20.0f, timeScaler.scale(20.0f), 0.01f);
        assertEquals(30.0f, timeScaler.scale(30.0f), 0.01f);
        assertEquals(40.0f, timeScaler.scale(40.0f), 0.01f);
        assertEquals(50.0f, timeScaler.scale(50.0f), 0.01f);
        assertEquals(60.0f, timeScaler.scale(60.0f), 0.01f);
        assertEquals(70.0f, timeScaler.scale(70.0f), 0.01f);
        assertEquals(80.0f, timeScaler.scale(80.0f), 0.01f);
        assertEquals(90.0f, timeScaler.scale(90.0f), 0.01f);
        assertEquals(100.0f, timeScaler.scale(100.0f), 0.01f);
        assertEquals(200.0f, timeScaler.scale(200.0f), 0.01f);
    }

    @Test
    public void testTimeScaler100f() {
        TimeScaler timeScaler = new TimeScaler(100.0f);
        assertEquals("ms", timeScaler.getUnits());
        assertEquals(0.0f, timeScaler.scale(0.0f), 0.01f);
        assertEquals(1.0f, timeScaler.scale(1.0f), 0.01f);
        assertEquals(2.0f, timeScaler.scale(2.0f), 0.01f);
        assertEquals(3.0f, timeScaler.scale(3.0f), 0.01f);
        assertEquals(4.0f, timeScaler.scale(4.0f), 0.01f);
        assertEquals(5.0f, timeScaler.scale(5.0f), 0.01f);
        assertEquals(6.0f, timeScaler.scale(6.0f), 0.01f);
        assertEquals(7.0f, timeScaler.scale(7.0f), 0.01f);
        assertEquals(8.0f, timeScaler.scale(8.0f), 0.01f);
        assertEquals(9.0f, timeScaler.scale(9.0f), 0.01f);
        assertEquals(10.0f, timeScaler.scale(10.0f), 0.01f);
        assertEquals(20.0f, timeScaler.scale(20.0f), 0.01f);
        assertEquals(30.0f, timeScaler.scale(30.0f), 0.01f);
        assertEquals(40.0f, timeScaler.scale(40.0f), 0.01f);
        assertEquals(50.0f, timeScaler.scale(50.0f), 0.01f);
        assertEquals(60.0f, timeScaler.scale(60.0f), 0.01f);
        assertEquals(70.0f, timeScaler.scale(70.0f), 0.01f);
        assertEquals(80.0f, timeScaler.scale(80.0f), 0.01f);
        assertEquals(90.0f, timeScaler.scale(90.0f), 0.01f);
        assertEquals(100.0f, timeScaler.scale(100.0f), 0.01f);
        assertEquals(200.0f, timeScaler.scale(200.0f), 0.01f);
    }

    @Test
    public void testTimeScaler1000f() {
        TimeScaler timeScaler = new TimeScaler(1000.0f);
        assertEquals("ms", timeScaler.getUnits());
        assertEquals(0.0f, timeScaler.scale(0.0f), 0.01f);
        assertEquals(1.0f, timeScaler.scale(1.0f), 0.01f);
        assertEquals(2.0f, timeScaler.scale(2.0f), 0.01f);
        assertEquals(3.0f, timeScaler.scale(3.0f), 0.01f);
        assertEquals(4.0f, timeScaler.scale(4.0f), 0.01f);
        assertEquals(5.0f, timeScaler.scale(5.0f), 0.01f);
        assertEquals(6.0f, timeScaler.scale(6.0f), 0.01f);
        assertEquals(7.0f, timeScaler.scale(7.0f), 0.01f);
        assertEquals(8.0f, timeScaler.scale(8.0f), 0.01f);
        assertEquals(9.0f, timeScaler.scale(9.0f), 0.01f);
        assertEquals(10.0f, timeScaler.scale(10.0f), 0.01f);
        assertEquals(20.0f, timeScaler.scale(20.0f), 0.01f);
        assertEquals(30.0f, timeScaler.scale(30.0f), 0.01f);
        assertEquals(40.0f, timeScaler.scale(40.0f), 0.01f);
        assertEquals(50.0f, timeScaler.scale(50.0f), 0.01f);
        assertEquals(60.0f, timeScaler.scale(60.0f), 0.01f);
        assertEquals(70.0f, timeScaler.scale(70.0f), 0.01f);
        assertEquals(80.0f, timeScaler.scale(80.0f), 0.01f);
        assertEquals(90.0f, timeScaler.scale(90.0f), 0.01f);
        assertEquals(100.0f, timeScaler.scale(100.0f), 0.01f);
        assertEquals(200.0f, timeScaler.scale(200.0f), 0.01f);
    }

    @Test
    public void testTimeScaler10000f() {
        TimeScaler timeScaler = new TimeScaler(10000.0f);
        assertEquals("seconds", timeScaler.getUnits());
        assertEquals(0.0f, timeScaler.scale(0.0f), 0.01f);
        assertEquals(0.001f, timeScaler.scale(1.0f), 0.01f);
        assertEquals(0.002f, timeScaler.scale(2.0f), 0.01f);
        assertEquals(0.003f, timeScaler.scale(3.0f), 0.01f);
        assertEquals(0.004f, timeScaler.scale(4.0f), 0.01f);
        assertEquals(0.005f, timeScaler.scale(5.0f), 0.01f);
        assertEquals(0.006f, timeScaler.scale(6.0f), 0.01f);
        assertEquals(0.007f, timeScaler.scale(7.0f), 0.01f);
        assertEquals(0.008f, timeScaler.scale(8.0f), 0.01f);
        assertEquals(0.009f, timeScaler.scale(9.0f), 0.01f);
        assertEquals(0.01f, timeScaler.scale(10.0f), 0.01f);
        assertEquals(0.02f, timeScaler.scale(20.0f), 0.01f);
        assertEquals(0.03f, timeScaler.scale(30.0f), 0.01f);
        assertEquals(0.04f, timeScaler.scale(40.0f), 0.01f);
        assertEquals(0.05f, timeScaler.scale(50.0f), 0.01f);
        assertEquals(0.06f, timeScaler.scale(60.0f), 0.01f);
        assertEquals(0.07f, timeScaler.scale(70.0f), 0.01f);
        assertEquals(0.08f, timeScaler.scale(80.0f), 0.01f);
        assertEquals(0.09f, timeScaler.scale(90.0f), 0.01f);
        assertEquals(0.1f, timeScaler.scale(100.0f), 0.01f);
        assertEquals(0.2f, timeScaler.scale(200.0f), 0.01f);
    }

    @Test
    public void testGetUnits() {
        assertEquals("ms", new TimeScaler(0.0f).getUnits());
        assertEquals("ms", new TimeScaler(1.0f).getUnits());
        assertEquals("ms", new TimeScaler(10.0f).getUnits());
        assertEquals("ms", new TimeScaler(100.0f).getUnits());
        assertEquals("ms", new TimeScaler(1000.0f).getUnits());
        assertEquals("ms", new TimeScaler(1999.9f).getUnits());

        assertEquals("seconds", new TimeScaler(2000.0f).getUnits());
        assertEquals("seconds", new TimeScaler(2 * K).getUnits());
        assertEquals("seconds", new TimeScaler(8 * K).getUnits());
        assertEquals("seconds", new TimeScaler(10 * K).getUnits());
        assertEquals("seconds", new TimeScaler(100 * K).getUnits());
        assertEquals("seconds", new TimeScaler(119999.9f).getUnits());

        assertEquals("minutes", new TimeScaler(120 * K).getUnits());
        assertEquals("minutes", new TimeScaler(200 * K).getUnits());
        assertEquals("minutes", new TimeScaler(400 * K).getUnits());
        assertEquals("minutes", new TimeScaler(800 * K).getUnits());
        assertEquals("minutes", new TimeScaler(1 * M).getUnits());
        assertEquals("minutes", new TimeScaler(10 * M).getUnits());
        assertEquals("minutes", new TimeScaler(100 * M).getUnits());
        assertEquals("minutes", new TimeScaler(1000 * M).getUnits());
        assertEquals("minutes", new TimeScaler(10000 * M).getUnits());
        assertEquals("minutes", new TimeScaler(100000 * M).getUnits());
        assertEquals("minutes", new TimeScaler(1000000 * M).getUnits());
    }

    @Test
    public void testScaleToMilliseconds() {
        mTimeScaler = new TimeScaler(0);
        assertEquals("ms", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1000.0f, mTimeScaler.scale(1000.0f), 0.01);

        mTimeScaler = new TimeScaler(1);
        assertEquals("ms", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1000.0f, mTimeScaler.scale(1000.0f), 0.01);

        mTimeScaler = new TimeScaler(2);
        assertEquals("ms", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1000.0f, mTimeScaler.scale(1000.0f), 0.01);

        mTimeScaler = new TimeScaler(1999.9f);
        assertEquals("ms", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1000.0f, mTimeScaler.scale(1000.0f), 0.01);
    }

    @Test
    public void testScaleToSeconds() {
        mTimeScaler = new TimeScaler(2000);
        assertEquals("seconds", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(0.001f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1000.0f), 0.01);
        assertEquals(10.0f, mTimeScaler.scale(10000.0f), 0.01);

        mTimeScaler = new TimeScaler(5000);
        assertEquals("seconds", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(0.001f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1000.0f), 0.01);
        assertEquals(10.0f, mTimeScaler.scale(10000.0f), 0.01);

        mTimeScaler = new TimeScaler(10000);
        assertEquals("seconds", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(0.001f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1000.0f), 0.01);
        assertEquals(10.0f, mTimeScaler.scale(10000.0f), 0.01);

        mTimeScaler = new TimeScaler(100000);
        assertEquals("seconds", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(0.001f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1000.0f), 0.01);
        assertEquals(10.0f, mTimeScaler.scale(10000.0f), 0.01);

        mTimeScaler = new TimeScaler(119999.9f);
        assertEquals("seconds", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(0.001f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(1000.0f), 0.01);
        assertEquals(10.0f, mTimeScaler.scale(10000.0f), 0.01);
    }

    @Test
    public void testScaleToMinutes() {
        mTimeScaler = new TimeScaler(120000);
        assertEquals("minutes", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(0.001f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(0.1f, mTimeScaler.scale(6000.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(60000.0f), 0.01);
        assertEquals(2.0f, mTimeScaler.scale(120000.0f), 0.01);

        mTimeScaler = new TimeScaler(1000000);
        assertEquals("minutes", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(0.001f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(0.1f, mTimeScaler.scale(6000.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(60000.0f), 0.01);
        assertEquals(2.0f, mTimeScaler.scale(120000.0f), 0.01);

        mTimeScaler = new TimeScaler(1000000000);
        assertEquals("minutes", mTimeScaler.getUnits());
        assertEquals(0.0f, mTimeScaler.scale(0.0f), 0.01);
        assertEquals(0.001f, mTimeScaler.scale(1.0f), 0.01);
        assertEquals(0.1f, mTimeScaler.scale(6000.0f), 0.01);
        assertEquals(1.0f, mTimeScaler.scale(60000.0f), 0.01);
        assertEquals(2.0f, mTimeScaler.scale(120000.0f), 0.01);
    }

}
