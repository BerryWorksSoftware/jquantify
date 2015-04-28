package com.berryworks.jquantify.tutorial;

import com.berryworks.jquantify.EventCounterInterval;
import com.berryworks.jquantify.SessionCounter;
import com.berryworks.jquantify.SessionCounterInterval;

import java.util.Date;

public class Example6 extends Example {

    private SessionCounter signonCounter = new SessionCounter("signon");

    public static void main(String[] args) {
        runSimulation(new Example6());
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
        System.out.println("session length (peak): " + signonCounter.getMaximumSessionTime() + " milliseconds" +
                " at " + new Date(signonCounter.getMaximumSessionStartTime()));

        System.out.println("session concurrency (current): " + signonCounter.getConcurrency());
        SessionCounterInterval peakConcurrencyInterval = signonCounter.getPeakConcurrencyInterval();
        System.out.println("session concurrency (peak): " + peakConcurrencyInterval.getPeakConcurrency() +
                " at " + new Date(peakConcurrencyInterval.getStartTime()));
    }
}
