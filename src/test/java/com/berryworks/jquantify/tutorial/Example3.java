package com.berryworks.jquantify.tutorial;

import com.berryworks.jquantify.EventCounter;
import com.berryworks.jquantify.EventCounterInterval;

import java.util.Date;

public class Example3 extends Example {

    private final EventCounter signonCounter = new EventCounter("signon");

    public static void main(String[] args) {
        runSimulation(new Example3());
    }

    @Override
    public void signon() {
        signonCounter.add();
        performSignonWork();
    }

    @Override
    public void summarize() {
        System.out.println("total signons: " + signonCounter.getCount());

        System.out.println("signon frequency (overall): " + signonCounter.getCumulativeFreq());

        EventCounterInterval peakEventsInterval = signonCounter.getPeakEventsInterval();
        System.out.println("signon frequency (peak): " + peakEventsInterval.getEventRatePerSecond() +
                " at " + new Date(peakEventsInterval.getStartTime()));

        System.out.println("signon frequency (recent): " + signonCounter.getCurrentFreq());
    }
}
