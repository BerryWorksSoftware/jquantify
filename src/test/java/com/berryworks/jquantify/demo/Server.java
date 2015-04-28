package com.berryworks.jquantify.demo;

import com.berryworks.jquantify.SessionCounter;

public class Server implements Runnable {

    private final String label;
    private final SystemQueue queue;
    private final int limit;
    private final float serviceRate;

    public Server(String label, int n, float serviceRate, SystemQueue systemQueue) {
        this.label = label;
        this.limit = n;
        this.serviceRate = serviceRate;
        this.queue = systemQueue;
    }

    @Override
    public void run() {
        SessionCounter customerCounter = SessionCounter.getSessionCounter(Simulation.CUSTOMERS_METRIC);
        SessionCounter serviceCounter = SessionCounter.getSessionCounter(Simulation.SERVICE_METRIC);

        System.out.println("Server " + label + " running");

        while (!exitCondition(customerCounter)) {
            Customer customer = queue.beginToServe(label);
            if (customer == null) continue;

            serviceCounter.start();
            waitForServiceToComplete();
            serviceCounter.stop();
            customerCounter.stop();

            long queued = customerCounter.getConcurrency();
            System.out.println("Server " + label + " finished serving " + customer +
                    (queued > 0 ? ", customers remaining in queue: " + queued : ""));
        }

        System.out.println("Server " + label + " run complete");
    }

    private boolean exitCondition(SessionCounter counter) {

        if (counter.getCount() < limit)
            return false;

        if (counter.getConcurrency() > 0)
            return false;

        return true;
    }

    private void waitForServiceToComplete() {
        try {
            Thread.sleep((long) (1000 * Simulation.timeSampleYieldingMeanRate(serviceRate)));
        } catch (InterruptedException ignore) {
        }
    }

}
