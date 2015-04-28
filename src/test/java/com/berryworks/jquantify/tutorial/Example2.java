package com.berryworks.jquantify.tutorial;

import com.berryworks.jquantify.EventCounter;

public class Example2 extends Example {

    private EventCounter signonCounter = new EventCounter("signon");

    public static void main(String[] args) {
        runSimulation(new Example2());
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
        System.out.println("signon frequency (peak): " + signonCounter.getPeakEventsInterval().getEventRatePerSecond());
        System.out.println("signon frequency (recent): " + signonCounter.getCurrentFreq());
    }
}
