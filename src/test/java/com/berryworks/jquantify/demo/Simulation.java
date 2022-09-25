package com.berryworks.jquantify.demo;

import com.berryworks.jquantify.SessionCounter;

import java.util.Random;

public class Simulation implements Runnable {

    public static final String CUSTOMERS_METRIC = "customers";
    public static final String SERVICE_METRIC = "service";
    private static final float ARRIVAL_RATE_PER_SECOND = 100.0f;
    private static final float SERVICE_RATE_PER_SECOND = 100.0f;
    private static final int CUSTOMERS = 1000;
    private static final Random random = new Random();

    public static void main(String[] args) {
        new Simulation().run();
    }

    public static double timeSampleYieldingMeanRate(float rate) {
        double d = (-1.0f / rate) * Math.log(1 - random.nextFloat());
        return d;
    }

    @Override
    public void run() {
        SystemQueue systemQueue = new SystemQueue();
        CustomerGenerator customerGenerator = new CustomerGenerator(CUSTOMERS, ARRIVAL_RATE_PER_SECOND, systemQueue);

        Thread serverA = new Thread(new Server("A", CUSTOMERS, SERVICE_RATE_PER_SECOND, systemQueue));
        serverA.start();
        Thread serverB = new Thread(new Server("B", CUSTOMERS, SERVICE_RATE_PER_SECOND, systemQueue));
        serverB.start();

        Thread arrivalsThread = new Thread(customerGenerator);
        arrivalsThread.start();
        float arrivalRate = 0;

        try {
            arrivalsThread.join();
            // We need to note the rate now instead of waiting until later,
            // so that the average will be over the actual time period.
            arrivalRate = SessionCounter.getSessionCounter(CUSTOMERS_METRIC).getCumulativeFreq();
            serverA.join();
            serverB.join();
        } catch (InterruptedException ignore) {
        }

        report(arrivalRate);
    }

    private void report(float arrivalRate) {
        SessionCounter customers = SessionCounter.getSessionCounter(CUSTOMERS_METRIC);
        SessionCounter service = SessionCounter.getSessionCounter(SERVICE_METRIC);

        System.out.println();
        System.out.println("Customers served:      " + customers.getCount());
        System.out.println("Arrival rate:          " +
                arrivalRate + " arrivals per second (predicted value: " + ARRIVAL_RATE_PER_SECOND + ")");
        System.out.println("Average service time:  " +
                service.getSessionTimeMean() / 1000.0f + " seconds (predicted value: " + (1.0 / SERVICE_RATE_PER_SECOND) + ")");
        System.out.println("Average customer time: " + customers.getSessionTimeMean() / 1000.0f + " seconds (including time in queue)");
        System.out.println("Peak queue size:      " + customers.getPeakConcurrency());
        System.out.println();
    }

}
