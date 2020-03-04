package com.berryworks.jquantify;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SessionCounterTest {
    public static final double DELTA = 0.0001;
    public static final int BIG_DELTA = 10;
    SessionCounter mCounter;

    @Before
    public void setUp() {
        mCounter = new SessionCounter("testCounter", 60);
    }

    @Test
    public void testStartAndStop() {
        assertEquals(0, mCounter.getCumulativeEvents());

        mCounter.start();
        mCounter.stop();
        assertEquals(1, mCounter.getCumulativeEvents());

        mCounter.start();
        mCounter.stop();
        assertEquals(2, mCounter.getCumulativeEvents());

        mCounter.start();
        assertEquals(3, mCounter.getCumulativeEvents());
        mCounter.stop();
        assertEquals(3, mCounter.getCumulativeEvents());
    }

    @Test
    public void testReset() {
        mCounter.start();
        assertEquals(1, mCounter.getConcurrency());

        mCounter.reset();
        assertEquals(0, mCounter.getConcurrency());

    }

    @Test
    public void constructWithDefaultIntervalSize() {
        mCounter = new SessionCounter("x");
        assertEquals(1, mCounter.getIntervalSeconds());
    }

    @Test
    public void testConcurrency() {
        assertEquals(0, mCounter.getConcurrency());

        mCounter.start();
        assertEquals(1, mCounter.getConcurrency());

        mCounter.stop();
        assertEquals(0, mCounter.getConcurrency());

        mCounter.start();
        mCounter.start();
        assertEquals(2, mCounter.getConcurrency());

        mCounter.stop();
        assertEquals(1, mCounter.getConcurrency());

        mCounter.start();
        mCounter.start();
        mCounter.start();
        mCounter.start();
        mCounter.start();
        mCounter.start();
        assertEquals(7, mCounter.getConcurrency());

        mCounter.stop();
        assertEquals(6, mCounter.getConcurrency());

        mCounter.stop();
        mCounter.stop();
        mCounter.stop();
        mCounter.stop();
        mCounter.stop();
        mCounter.stop();
        assertEquals(0, mCounter.getConcurrency());
    }

    @Test
    public void testConcurrencyOverMultipleIntervals() throws InterruptedException {
        mCounter = new SessionCounter("testCounter");
        mCounter.start();
        assertEquals(1, mCounter.getConcurrency());
        assertEquals(1, mCounter.getPeakConcurrency());
        mCounter.start();
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(2, mCounter.getPeakConcurrency());
        mCounter.start();
        assertEquals(3, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());
        mCounter.start();
        assertEquals(4, mCounter.getConcurrency());
        assertEquals(4, mCounter.getPeakConcurrency());

        // Sleep for over a second to make sure we are in another interval
        Thread.sleep(1500);
        assertEquals(4, mCounter.getConcurrency());
        assertEquals(4, mCounter.getPeakConcurrency());

        mCounter.stop();
        assertEquals(3, mCounter.getConcurrency());
        assertEquals(4, mCounter.getPeakConcurrency());
        assertEquals(4, mCounter.getPeakConcurrencyInterval().getPeakConcurrency());
    }

    @Test
    public void testSessionTime() throws InterruptedException {
        assertEquals(0, mCounter.getMaximumSessionTime());
        assertEquals(0.0, mCounter.getSessionTime(), 0.00);
        assertEquals(0.0, mCounter.getSessionTimeMean(), 0.00);

        // start/stop a session lasting .1 second
        long sessionStartTime = Clock.now();
        mCounter.start();
        Thread.sleep(100);
        mCounter.stop();
        assertEquals(100, mCounter.getMaximumSessionTime(), 50);
        assertEquals(sessionStartTime, mCounter.getMaximumSessionStartTime(), 50);
        assertEquals(100, mCounter.getSessionTime(), 50);
        assertEquals(100.0, mCounter.getSessionTimeMean(), 50.0);

        // do it again
        mCounter.start();
        Thread.sleep(100);
        mCounter.stop();
        assertEquals(100, mCounter.getMaximumSessionTime(), 50);
        assertEquals(sessionStartTime, mCounter.getMaximumSessionStartTime(), 100);
        assertEquals(200, mCounter.getSessionTime(), 200);
        assertEquals(100.0, mCounter.getSessionTimeMean(), 50.0);

        // again, with a duration of .2 second this time
        sessionStartTime = Clock.now();
        mCounter.start();
        Thread.sleep(200);
        mCounter.stop();
        assertEquals(200, mCounter.getMaximumSessionTime(), 50);
        assertEquals(sessionStartTime, mCounter.getMaximumSessionStartTime(), 50);
        assertEquals(400, mCounter.getSessionTime(), 200);
        assertEquals(400 / 3.0, mCounter.getSessionTimeMean(), 50.0);
    }

    @Test
    public void testSessionTimeWithConcurrency() throws InterruptedException {

        // Time   action
        // ----   ------
        //  0     start A
        //  +10   start B
        //  +20   start C
        //  +40   stop (assumed to be A)
        //  +60   stop (assumed to be B)
        //  +70   start D
        //  +80   stop (assumed to be C)
        //  +100  stop (assumed to be D)
        //  +110  stop (ignored)
        //  +130  start E
        //  +140  stop (assumed to be E)


        mCounter.start(); // A
        assertEquals(0, mCounter.getMaximumSessionTime());
        assertEquals(0, mCounter.getSessionTime(), DELTA);
        assertEquals(0.0, mCounter.getSessionTimeMean(), 0.00);

        Thread.sleep(10);
        mCounter.start(); // B
        assertEquals(0, mCounter.getMaximumSessionTime());
        assertEquals(0, mCounter.getSessionTime(), DELTA);
        assertEquals(0.0, mCounter.getSessionTimeMean(), 0.00);

        Thread.sleep(10);
        // time is now +20
        mCounter.start(); // C
        assertEquals(0, mCounter.getMaximumSessionTime());
        assertEquals(0, mCounter.getSessionTime(), DELTA);
        assertEquals(0.0, mCounter.getSessionTimeMean(), 0.00);

        Thread.sleep(20);
        // time is now +40
        mCounter.stop(); // A
        // The session times reported should reflect the one session that has completed
        assertEquals(40, mCounter.getMaximumSessionTime(), BIG_DELTA);
        assertEquals(40, mCounter.getSessionTime(), BIG_DELTA);
        assertEquals(40.0, mCounter.getSessionTimeMean(), 10.00);

        Thread.sleep(20);
        // time is now +60
        mCounter.stop(); // B
        // We have 2 completed sessions, one of duration 40 and the other of duration 50
        assertEquals(50, mCounter.getMaximumSessionTime(), BIG_DELTA);
        assertEquals(90, mCounter.getSessionTime(), 2 * BIG_DELTA);
        assertEquals((40 + 50) / 2.0, mCounter.getSessionTimeMean(), 10.00);

        Thread.sleep(10);
        // time is now +70
        mCounter.start(); // D
        assertEquals(50, mCounter.getMaximumSessionTime(), BIG_DELTA);
        assertEquals(90, mCounter.getSessionTime(), 2 * BIG_DELTA);
        assertEquals((40 + 50) / 2.0, mCounter.getSessionTimeMean(), BIG_DELTA);

        Thread.sleep(10);
        // time is now +80
        mCounter.stop(); // C
        assertEquals(60, mCounter.getMaximumSessionTime(), BIG_DELTA);
        assertEquals(150, mCounter.getSessionTime(), 3 * BIG_DELTA);
        assertEquals((40 + 50 + 60) / 3.0, mCounter.getSessionTimeMean(), BIG_DELTA);

        Thread.sleep(20);
        mCounter.stop(); // D
        assertEquals(60, mCounter.getMaximumSessionTime(), BIG_DELTA);
        assertEquals(180, mCounter.getSessionTime(), 4 * BIG_DELTA);
        assertEquals((40 + 50 + 60 + 30) / 4.0, mCounter.getSessionTimeMean(), BIG_DELTA);

        Thread.sleep(10);
        mCounter.stop(); // ignored
        assertEquals(60, mCounter.getMaximumSessionTime(), BIG_DELTA);
        assertEquals(180, mCounter.getSessionTime(), 4 * BIG_DELTA);
        assertEquals((40 + 50 + 60 + 30) / 4.0, mCounter.getSessionTimeMean(), BIG_DELTA);

        Thread.sleep(20);
        mCounter.start(); // E
        assertEquals(60, mCounter.getMaximumSessionTime(), BIG_DELTA);
        assertEquals(180, mCounter.getSessionTime(), 4 * BIG_DELTA);
        assertEquals((40 + 50 + 60 + 30) / 4.0, mCounter.getSessionTimeMean(), BIG_DELTA);

        Thread.sleep(10);
        mCounter.stop(); // E
        assertEquals(60, mCounter.getMaximumSessionTime(), BIG_DELTA);
        assertEquals(190, mCounter.getSessionTime(), 5 * BIG_DELTA);
        assertEquals((40 + 50 + 60 + 30 + 10) / 5.0, mCounter.getSessionTimeMean(), BIG_DELTA);
    }

    @Test
    public void whenActivityResumesInNextInterval() throws InterruptedException {
        mCounter = new SessionCounter("testCounter");

        mCounter.start();
        mCounter.start();
        mCounter.start();
        assertEquals(3, mCounter.getCumulativeEvents());
        assertEquals(3, mCounter.getCurrentInterval().getEvents());
        assertEquals(0, mCounter.getPeakEvents());
        assertEquals(0, mCounter.getPeakEventsInterval().getEvents());
        long peakEventsTime = mCounter.getCurrentInterval().getStartTime();
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
        assertEquals(3, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        mCounter.remove();
        assertEquals(3, mCounter.getCumulativeEvents());
        assertEquals(3, mCounter.getCurrentInterval().getEvents());
        assertEquals(0, mCounter.getPeakEvents());
        assertEquals(0, mCounter.getPeakEventsInterval().getEvents());
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        // Sleep for a bit over 1 second. This lets the initial one second interval
        /// complete, and then continues in the next interval.
        Thread.sleep(1200);

        mCounter.start();
        mCounter.start();
        assertEquals(5, mCounter.getCumulativeEvents());
        assertEquals(2, mCounter.getCurrentInterval().getEvents());
        assertEquals(3, mCounter.getPeakEvents());
        assertEquals(3, mCounter.getPeakEventsInterval().getEvents());
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
        assertEquals(4, mCounter.getConcurrency());
        assertEquals(4, mCounter.getPeakConcurrency());
    }

    @Test
    public void whenOneIntervalPassesWithNoActivity() throws InterruptedException {
        mCounter = new SessionCounter("testCounter");
        mCounter.start();
        mCounter.start();
        mCounter.start();
        mCounter.remove();
        assertEquals(3, mCounter.getCumulativeEvents());
        assertEquals(3, mCounter.getCurrentInterval().getEvents());
        assertEquals(0, mCounter.getPeakEvents());
        assertEquals(0, mCounter.getPeakEventsInterval().getEvents());
        long peakEventsTime = mCounter.getCurrentInterval().getStartTime();
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        // Sleep for a bit over 2 seconds. This lets the initial one second interval
        /// complete, and then one fully silent interval go by.
        Thread.sleep(2200);

        mCounter.start();
        mCounter.start();
        assertEquals(5, mCounter.getCumulativeEvents());
        assertEquals(2, mCounter.getCurrentInterval().getEvents());
        assertEquals(3, mCounter.getPeakEvents());
        assertEquals(3, mCounter.getPeakEventsInterval().getEvents());
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
        assertEquals(4, mCounter.getConcurrency());
        assertEquals(4, mCounter.getPeakConcurrency());
    }

    @Test
    public void whenTwoIntervalsPassWithNoActivity() throws InterruptedException {
        mCounter = new SessionCounter("testCounter");
        mCounter.start();
        mCounter.start();
        mCounter.start();
        mCounter.remove();
        assertEquals(3, mCounter.getCumulativeEvents());
        assertEquals(3, mCounter.getCurrentInterval().getEvents());
        assertEquals(0, mCounter.getPeakEvents());
        assertEquals(0, mCounter.getPeakEventsInterval().getEvents());
        long peakEventsTime = mCounter.getCurrentInterval().getStartTime();
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        // Sleep for a bit over 3 seconds. This lets the initial one second interval
        /// complete, and then two fully silent intervals go by.
        Thread.sleep(3200);

        mCounter.start();
        mCounter.start();
        assertEquals(5, mCounter.getCumulativeEvents());
        assertEquals(2, mCounter.getCurrentInterval().getEvents());
        assertEquals(3, mCounter.getPeakEvents());
        assertEquals(3, mCounter.getPeakEventsInterval().getEvents());
        assertEquals(peakEventsTime, mCounter.getPeakEventsInterval().getStartTime());
        assertEquals(4, mCounter.getConcurrency());
        assertEquals(4, mCounter.getPeakConcurrency());
    }

    @Test
    public void testPeakConcurrency() throws InterruptedException {
        mCounter = new SessionCounter("testCounter");
        assertEquals(0, mCounter.getConcurrency());
        assertEquals(0, mCounter.getPeakConcurrency());

        mCounter.start();
        assertEquals(1, mCounter.getConcurrency());
        assertEquals(1, mCounter.getPeakConcurrency());

        mCounter.start();
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(2, mCounter.getPeakConcurrency());

        mCounter.start();
        assertEquals(3, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        mCounter.remove();
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        // Sleep for a bit over 1 second, forcing us into the next interval
        Thread.sleep(1100);
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        mCounter.start();
        assertEquals(3, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        mCounter.remove();
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        mCounter.remove();
        assertEquals(1, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        // Sleep for a bit over 2 seconds. This lets the initial one second interval
        /// complete, and then one fully silent interval go by.
        Thread.sleep(2200);
        assertEquals(1, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        mCounter.start();
        assertEquals(2, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        mCounter.start();
        assertEquals(3, mCounter.getConcurrency());
        assertEquals(3, mCounter.getPeakConcurrency());

        mCounter.start();
        assertEquals(4, mCounter.getConcurrency());
        assertEquals(4, mCounter.getPeakConcurrency());

        mCounter.start();
        assertEquals(5, mCounter.getConcurrency());
        assertEquals(5, mCounter.getPeakConcurrency());
    }
}
