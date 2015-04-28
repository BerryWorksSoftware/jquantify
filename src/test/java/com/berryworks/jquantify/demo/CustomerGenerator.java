package com.berryworks.jquantify.demo;

import com.berryworks.jquantify.SessionCounter;

public class CustomerGenerator implements Runnable {

    private final int maximumNumberOfCustomers;
    private final SystemQueue queue;
    private final float arrivalRate;
    private final String metricName = Simulation.CUSTOMERS_METRIC;

    public CustomerGenerator(int customers, float arrivalRate, SystemQueue systemQueue) {
        this.maximumNumberOfCustomers = customers;
        this.arrivalRate = arrivalRate;
        this.queue = systemQueue;
    }

    @Override
    public void run() {
        System.out.println("Customer generator running");
        int n = 0;
        SessionCounter counter = SessionCounter.getSessionCounter(metricName);
        while (counter.getCount() < maximumNumberOfCustomers) {
            waitForNextCustomer();
            Customer customer = new Customer(++n);
            counter.start();
            queue.arrive(customer);
        }
    }

    private void waitForNextCustomer() {
        try {
            Thread.sleep((long) (1000 * Simulation.timeSampleYieldingMeanRate(arrivalRate)));
        } catch (InterruptedException ignore) {
        }
    }
}
