package com.berryworks.jquantify;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventCounterTest {

    EventCounter mCounter;

    @Before
    public void setUp() {
        mCounter = new EventCounter("testCounter");
    }

    @Test
    public void testDefaultConstructor() {
        new EventCounter();
    }

    @Test
    public void testAdd() {
        assertEquals(0, mCounter.getCumulativeEvents());

        mCounter.add();
        assertEquals(1, mCounter.getCumulativeEvents());

        mCounter.add();
        assertEquals(2, mCounter.getCumulativeEvents());

        mCounter.add();
        assertEquals(3, mCounter.getCumulativeEvents());

        mCounter.add(4);
        assertEquals(7, mCounter.getCumulativeEvents());
    }

    @Test
    public void testFrequency() throws InterruptedException {
        assertEquals(0.0, mCounter.getCumulativeFreq(), 0.01);
        assertEquals(0.0, mCounter.getCurrentFreq(), 0.01);
        assertEquals(0, mCounter.getPeakEvents());

        Thread.sleep(10);
        mCounter.add();
        // 1 event in 0.1 second is a rate of 100 events per second
        assertEquals(100.0, mCounter.getCumulativeFreq(), 80.0);
        assertEquals(100.0, mCounter.getCurrentFreq(), 80.0);
        assertEquals(0, mCounter.getPeakEvents());

        mCounter.add();
        // 2 per 0.1 second is 200 per second
        assertEquals(200.0, mCounter.getCumulativeFreq(), 160.0);
        assertEquals(200.0, mCounter.getCurrentFreq(), 160.0);
        assertEquals(0, mCounter.getPeakEvents());

        mCounter.add();
        assertEquals(300.0, mCounter.getCumulativeFreq(), 240.0);
        assertEquals(300.0, mCounter.getCurrentFreq(), 240.0);
        assertEquals(0, mCounter.getPeakEvents());

        mCounter.add(4);
        assertEquals(700.0, mCounter.getCumulativeFreq(), 560.0);
        assertEquals(700.0, mCounter.getCurrentFreq(), 560.0);
        assertEquals(0, mCounter.getPeakEvents());
    }

    @Test
    public void testReset() throws InterruptedException {
        assertEquals(0, mCounter.getCumulativeEvents());

        mCounter.add();
        mCounter.add();
        mCounter.add();
        assertEquals(3, mCounter.getCumulativeEvents());
        assertEquals(mCounter.getCurrentFreq(), mCounter.getCumulativeFreq(), 0.01);
        assertEquals(0, mCounter.getPeakEvents());

        Thread.sleep(1500);
        mCounter.add();
        assertEquals(4, mCounter.getCumulativeEvents());
        // 4 events in 1.5 seconds is a rate of 2.66 events per second
        assertEquals(2.66, mCounter.getCumulativeFreq(), 0.1);
//    assertEquals(100.0, mCounter.getCurrentFreq(), 80.0);
        assertEquals(3, mCounter.getPeakEvents());

        mCounter.reset();
        assertEquals(0, mCounter.getCumulativeEvents());
        assertEquals(0.0, mCounter.getCumulativeFreq(), 0.01);
        assertEquals(0.0, mCounter.getCurrentFreq(), 0.01);
        assertEquals(0, mCounter.getPeakEvents());
    }

    @Test
    public void canGetEventCounter() {
        assertTrue(EventCounter.getEventCounter("textCounter") instanceof EventCounter);
    }

    @Test
    public void whenOneIntervalPassesWithNoActivity() throws InterruptedException {
        mCounter.add();
        mCounter.add();
        mCounter.add();
        assertEquals(3, mCounter.getCumulativeEvents());
        assertEquals(3, mCounter.getCurrentInterval().getEvents());
        assertEquals(0, mCounter.getPeakEvents());
        assertEquals(0, mCounter.getPeakEventsInterval().getEvents());
        long peakEventsTime = mCounter.getCurrentInterval().getStartTime();
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());

        // Sleep for a bit over 2 seconds. This lets the initial one second interval
        /// complete, and then one fully silent interval go by.
        Thread.sleep(2200);

        mCounter.add();
        mCounter.add();
        assertEquals(5, mCounter.getCumulativeEvents());
        assertEquals(2, mCounter.getCurrentInterval().getEvents());
        assertEquals(3, mCounter.getPeakEvents());
        assertEquals(3, mCounter.getPeakEventsInterval().getEvents());
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
    }

    @Test
    public void whenTwoIntervalsPassWithNoActivity() throws InterruptedException {
        mCounter.add();
        mCounter.add();
        mCounter.add();
        assertEquals(3, mCounter.getCumulativeEvents());
        assertEquals(3, mCounter.getCurrentInterval().getEvents());
        assertEquals(0, mCounter.getPeakEvents());
        assertEquals(0, mCounter.getPeakEventsInterval().getEvents());
        long peakEventsTime = mCounter.getCurrentInterval().getStartTime();
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());

        // Sleep for a bit over 3 seconds. This lets the initial one second interval
        /// complete, and then two fully silent intervals go by.
        Thread.sleep(3200);

        mCounter.add();
        mCounter.add();
        assertEquals(5, mCounter.getCumulativeEvents());
        assertEquals(2, mCounter.getCurrentInterval().getEvents());
        assertEquals(3, mCounter.getPeakEvents());
        assertEquals(3, mCounter.getPeakEventsInterval().getEvents());
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
    }

    @Test
    public void whenActivityResumesInNextInterval() throws InterruptedException {
        mCounter.add();
        mCounter.add();
        mCounter.add();
        assertEquals(3, mCounter.getCumulativeEvents());
        assertEquals(3, mCounter.getCurrentInterval().getEvents());
        assertEquals(0, mCounter.getPeakEvents());
        assertEquals(0, mCounter.getPeakEventsInterval().getEvents());
        long peakEventsTime = mCounter.getCurrentInterval().getStartTime();
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());

        // Sleep for a bit over 1 second. This lets the initial one second interval
        /// complete, and then continues in the next interval.
        Thread.sleep(1200);

        mCounter.add();
        mCounter.add();
        assertEquals(5, mCounter.getCumulativeEvents());
        assertEquals(2, mCounter.getCurrentInterval().getEvents());
        assertEquals(3, mCounter.getPeakEvents());
        assertEquals(3, mCounter.getPeakEventsInterval().getEvents());
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
    }
}

