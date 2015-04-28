package com.berryworks.jquantify;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class SessionCounterIntervalTest {

    private SessionCounterInterval interval, priorInterval, nextInterval;

    @Test
    public void testConstructors() {
        interval = new SessionCounterInterval();

        interval = new SessionCounterInterval(123, 456);
        assertEquals(123, interval.getDuration());
        assertEquals(456, interval.getStartTime());
    }

//    @Test
//    public void testAdvance() {
//        long justOverOneSecondAgo = Clock.now() - 1100;
//        priorInterval = new SessionCounterInterval(1000, justOverOneSecondAgo);
//        priorInterval.setClosed(true);
//        priorInterval.add(7);
//        priorInterval.remove(3);
//        assertTrue(priorInterval.isPast());
//        assertEquals(7, priorInterval.getEvents());
//        assertEquals(4, priorInterval.getConcurrency());
//        assertEquals(7, priorInterval.getPeakConcurrency());
//
//        nextInterval = new SessionCounterInterval();
//        nextInterval.advance(priorInterval);
//        assertEquals(1000, nextInterval.getDuration());
//        assertEquals(justOverOneSecondAgo + 1000, nextInterval.getStartTime());
//        assertFalse(nextInterval.isPast());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(7, nextInterval.getCumulativeEvents());
//        assertEquals(4, nextInterval.getConcurrency());
//        assertEquals(4, nextInterval.getPeakConcurrency());
//    }
//
//    @Test
//    public void testAdvance1() {
//        long justOverOneSecondAgo = Clock.now() - 1100;
//        priorInterval = new SessionCounterInterval(1000, justOverOneSecondAgo);
//        priorInterval.setClosed(true);
//        priorInterval.add(7);
//        priorInterval.remove(3);
//        assertTrue(priorInterval.isPast());
//        assertEquals(7, priorInterval.getEvents());
//        assertEquals(4, priorInterval.getConcurrency());
//        assertEquals(7, priorInterval.getPeakConcurrency());
//
//        nextInterval = new SessionCounterInterval();
//        nextInterval.advance(priorInterval, 1);
//        assertEquals(1000, nextInterval.getDuration());
//        assertEquals(justOverOneSecondAgo + 1000, nextInterval.getStartTime());
//        assertFalse(nextInterval.isPast());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(7, nextInterval.getCumulativeEvents());
//        assertEquals(4, nextInterval.getConcurrency());
//        assertEquals(4, nextInterval.getPeakConcurrency());
//    }
//
//    @Test
//    public void testAdvance5() {
//        long justOverOneSecondAgo = Clock.now() - 1100;
//        priorInterval = new SessionCounterInterval(1000, justOverOneSecondAgo);
//        priorInterval.setClosed(true);
//        priorInterval.add(7);
//        priorInterval.remove(3);
//        assertTrue(priorInterval.isPast());
//        assertEquals(7, priorInterval.getEvents());
//        assertEquals(4, priorInterval.getConcurrency());
//        assertEquals(7, priorInterval.getPeakConcurrency());
//
//        nextInterval = new SessionCounterInterval();
//        nextInterval.advance(priorInterval, 5);
//        assertEquals(1000, nextInterval.getDuration());
//        assertEquals(justOverOneSecondAgo + 5000, nextInterval.getStartTime());
//        assertFalse(nextInterval.isPast());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(7, nextInterval.getCumulativeEvents());
//        assertEquals(4, nextInterval.getConcurrency());
//        assertEquals(4, nextInterval.getPeakConcurrency());
//    }

    @Test
    public void testCopy() {
        long justOverOneSecondAgo = Clock.now() - 1100;
        priorInterval = new SessionCounterInterval(1000, justOverOneSecondAgo);
        priorInterval.setClosed(true);
        priorInterval.add(7);
        priorInterval.remove(3);
        assertTrue(priorInterval.isPast());
        assertEquals(7, priorInterval.getEvents());
        assertEquals(4, priorInterval.getConcurrency());
        assertEquals(7, priorInterval.getPeakConcurrency());

        nextInterval = new SessionCounterInterval();
        nextInterval.copy(priorInterval);
        assertEquals(1000, nextInterval.getDuration());
        assertEquals(justOverOneSecondAgo, nextInterval.getStartTime());
        assertTrue(nextInterval.isPast());
        assertEquals(7, nextInterval.getEvents());
        assertEquals(7, nextInterval.getCumulativeEvents());
        assertEquals(4, nextInterval.getConcurrency());
        assertEquals(7, nextInterval.getPeakConcurrency());
    }

    @Test
    public void testEventRate() {
        // First use an interval from the past
        interval = new SessionCounterInterval(1000, Clock.now() - 1100);
        assertEquals(0.0, interval.getEventRatePerSecond(), 0.00001);
        interval.add(1);
        assertEquals(1.0, interval.getEventRatePerSecond(), 0.00001);
        interval.add(1);
        assertEquals(2.0, interval.getEventRatePerSecond(), 0.00001);
        interval.add(4);
        assertEquals(6.0, interval.getEventRatePerSecond(), 0.00001);
        interval.add(1000);
        assertEquals(1006.0, interval.getEventRatePerSecond(), 0.00001);

        // Now use a one-second interval that started one half second ago
        interval = new SessionCounterInterval(1000, Clock.now() - 500);
        assertEquals(0.0, interval.getEventRatePerSecond(), 0.00001);
        interval.add(1);
        assertEquals(2.0, interval.getEventRatePerSecond(), 0.1);
        interval.add(1);
        assertEquals(4.0, interval.getEventRatePerSecond(), 0.1);
        interval.add(4);
        assertEquals(12.0, interval.getEventRatePerSecond(), 1.0);
        interval.add(1000);
        assertEquals(2012.0, interval.getEventRatePerSecond(), 10.0);

        // Now use a one-second interval that started right now
        interval = new SessionCounterInterval(1000, Clock.now());
        assertEquals(0.0, interval.getEventRatePerSecond(), 0.00001);
        interval.add(1);
        assertEquals(1000.0, interval.getEventRatePerSecond(), 0.1);
        interval.add(1);
        assertEquals(2000.0, interval.getEventRatePerSecond(), 0.1);
        interval.add(4);
        assertEquals(6000.0, interval.getEventRatePerSecond(), 0.1);
        interval.add(1000);
        assertEquals(1006000.0, interval.getEventRatePerSecond(), 0.1);
    }

    @Test
    public void testConcurrency() {
        long justOverOneSecondAgo = Clock.now() - 1100;
        priorInterval = new SessionCounterInterval(1000, justOverOneSecondAgo);
        assertEquals(0, priorInterval.getConcurrency());
        assertEquals(0, priorInterval.getPeakConcurrency());

        priorInterval.add(1);
        assertEquals(1, priorInterval.getConcurrency());
        assertEquals(1, priorInterval.getPeakConcurrency());

        priorInterval.remove(1);
        assertEquals(0, priorInterval.getConcurrency());
        assertEquals(1, priorInterval.getPeakConcurrency());

        priorInterval.add(7);
        assertEquals(7, priorInterval.getConcurrency());
        assertEquals(7, priorInterval.getPeakConcurrency());

        priorInterval.remove(3);
        assertEquals(4, priorInterval.getConcurrency());
        assertEquals(7, priorInterval.getPeakConcurrency());

        priorInterval.add(1);
        assertEquals(5, priorInterval.getConcurrency());
        assertEquals(7, priorInterval.getPeakConcurrency());

        priorInterval.remove(1);
        assertEquals(4, priorInterval.getConcurrency());
        assertEquals(7, priorInterval.getPeakConcurrency());

        priorInterval.add(100);
        assertEquals(104, priorInterval.getConcurrency());
        assertEquals(104, priorInterval.getPeakConcurrency());

        priorInterval.remove(1);
        assertEquals(103, priorInterval.getConcurrency());
        assertEquals(104, priorInterval.getPeakConcurrency());

        priorInterval.remove(1);
        assertEquals(102, priorInterval.getConcurrency());
        assertEquals(104, priorInterval.getPeakConcurrency());

        priorInterval.remove(102);
        assertEquals(0, priorInterval.getConcurrency());
        assertEquals(104, priorInterval.getPeakConcurrency());

        priorInterval.remove(10);
        // Perhaps this should be zero?
        assertEquals(-10, priorInterval.getConcurrency());
        assertEquals(104, priorInterval.getPeakConcurrency());
    }

    @Test
    public void testToString() {
        interval = new SessionCounterInterval(1000, Clock.now());
        interval.add(3);
        assertEquals("Interval \n" + "closed=false events=3 priorEvents=0\n" +
                "stops=0 priorConcurrency=0 peakConcurrency=3", interval.toString());
    }
}
