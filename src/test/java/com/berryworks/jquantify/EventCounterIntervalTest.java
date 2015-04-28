package com.berryworks.jquantify;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class EventCounterIntervalTest {

    private EventCounterInterval interval;

    @Test
    public void testConstructors() {
        interval = new EventCounterInterval();

        interval = new EventCounterInterval(123, 456);
        assertEquals(123, interval.getDuration());
        assertEquals(456, interval.getStartTime());
    }

//    @Test
//    public void testAdvance() {
//        long justOverOneSecondAgo = Clock.now() - 1100;
//        EventCounterInterval priorInterval = new EventCounterInterval(1000, justOverOneSecondAgo);
//        priorInterval.setClosed(true);
//        priorInterval.add(7);
//        assertTrue(priorInterval.isPast());
//        assertEquals(7, priorInterval.getEvents());
//
//        EventCounterInterval nextInterval = new EventCounterInterval();
//        nextInterval.advance(priorInterval);
//        assertEquals(1000, nextInterval.getDuration());
//        assertEquals(justOverOneSecondAgo + 1000, nextInterval.getStartTime());
//        assertFalse(nextInterval.isPast());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(7, nextInterval.getCumulativeEvents());
//    }
//
//    @Test
//    public void testAdvance1() {
//        long justOverOneSecondAgo = Clock.now() - 1100;
//        EventCounterInterval priorInterval = new EventCounterInterval(1000, justOverOneSecondAgo);
//        priorInterval.setClosed(true);
//        priorInterval.add(7);
//        assertTrue(priorInterval.isPast());
//        assertEquals(7, priorInterval.getEvents());
//
//        EventCounterInterval nextInterval = new EventCounterInterval();
//        nextInterval.advance(priorInterval, 1);
//        assertEquals(1000, nextInterval.getDuration());
//        assertEquals(justOverOneSecondAgo + 1000, nextInterval.getStartTime());
//        assertFalse(nextInterval.isPast());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(7, nextInterval.getCumulativeEvents());
//    }
//
//    @Test
//    public void testAdvance5() {
//        long justOverOneSecondAgo = Clock.now() - 1100;
//        EventCounterInterval priorInterval = new EventCounterInterval(1000, justOverOneSecondAgo);
//        priorInterval.setClosed(true);
//        priorInterval.add(7);
//        assertTrue(priorInterval.isPast());
//        assertEquals(7, priorInterval.getEvents());
//
//        EventCounterInterval nextInterval = new EventCounterInterval();
//        nextInterval.advance(priorInterval, 5);
//        assertEquals(1000, nextInterval.getDuration());
//        assertEquals(justOverOneSecondAgo + 5000, nextInterval.getStartTime());
//        assertFalse(nextInterval.isPast());
//        assertEquals(0, nextInterval.getEvents());
//        assertEquals(7, nextInterval.getCumulativeEvents());
//    }

    @Test
    public void testCopy() {
        long justOverOneSecondAgo = Clock.now() - 1100;
        EventCounterInterval priorInterval = new EventCounterInterval(1000, justOverOneSecondAgo);
        priorInterval.setClosed(true);
        priorInterval.add(7);
        assertTrue(priorInterval.isPast());
        assertEquals(7, priorInterval.getEvents());

        EventCounterInterval nextInterval = new EventCounterInterval();
        nextInterval.copy(priorInterval);
        assertEquals(1000, nextInterval.getDuration());
        assertEquals(justOverOneSecondAgo, nextInterval.getStartTime());
        assertTrue(nextInterval.isPast());
        assertEquals(7, nextInterval.getEvents());
        assertEquals(7, nextInterval.getCumulativeEvents());
    }

    @Test
    public void testEventRate() {
        // First use an interval from the past
        interval = new EventCounterInterval(1000, Clock.now() - 1100);
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
        interval = new EventCounterInterval(1000, Clock.now() - 500);
        assertEquals(0.0, interval.getEventRatePerSecond(), 0.00001);
        interval.add(1);
        assertEquals(2.0, interval.getEventRatePerSecond(), 0.1);
        interval.add(1);
        assertEquals(4.0, interval.getEventRatePerSecond(), 0.1);
        interval.add(4);
        assertEquals(12.0, interval.getEventRatePerSecond(), 0.1);
        interval.add(1000);
        assertEquals(2012.0, interval.getEventRatePerSecond(), 0.1);

        // Now use a one-second interval that started right now
        interval = new EventCounterInterval(1000, Clock.now());
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
    public void testToString() {
        interval = new EventCounterInterval(1000, Clock.now());
        interval.add(3);
        assertEquals("Interval \nclosed=false events=3 priorEvents=0", interval.toString());
    }
}
