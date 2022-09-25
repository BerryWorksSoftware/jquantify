package com.berryworks.jquantify.tutorial;

import com.berryworks.jquantify.EventCounterInterval;
import com.berryworks.jquantify.SessionCounter;

import java.util.Date;

public class Example4 extends Example {

    private final SessionCounter signonCounter = new SessionCounter("signon");

    public static void main(String[] args) {
        runSimulation(new Example4());
    }

    @Override
    public void signon() {
        signonCounter.start();
        performSignonWork();
    }

    @Override
    public void signoff() {
        signonCounter.stop();
    }

    @Override
    public void summarize() {
        System.out.println("total signons: " + signonCounter.getCount());

        System.out.println("signon frequency (overall): " + signonCounter.getCumulativeFreq());

        EventCounterInterval peakEventsInterval = signonCounter.getPeakEventsInterval();
        System.out.println("signon frequency (peak): " + peakEventsInterval.getEventRatePerSecond() +
                " at " + new Date(peakEventsInterval.getStartTime()));

        System.out.println("signon frequency (recent): " + signonCounter.getCurrentFreq());

        System.out.println("session length (mean): " + signonCounter.getSessionTimeMean() + " milliseconds");
    }
}
