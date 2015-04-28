package com.berryworks.jquantify;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClockTest {

    @Test
    public void testClock() throws InterruptedException {
        long now = System.currentTimeMillis();
        assertTrue(now <= Clock.now());
        Thread.sleep(2);
        assertTrue(now < Clock.now());
    }
}
