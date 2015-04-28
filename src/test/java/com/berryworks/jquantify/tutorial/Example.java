package com.berryworks.jquantify.tutorial;

import java.util.Random;

public class Example implements Runnable {

    public static void main(String[] args) {
        runSimulation(new Example());
    }

    static void runSimulation(Example example) {
        System.out.println("Running simulation using " + example.getClass());
        example.run();
        System.out.println("Simulation complete");
    }

    public void signon() {
        performSignonWork();
    }

    public void signoff() {
    }

    public void summarize() {
        System.out.println("finished");
    }

    @Override
    public void run() {
        for (int i = 100; i > 0; i--) {
            signon();
            signoff();
        }
        summarize();
    }

    protected void performSignonWork() {
        pause();
    }

    private void pause() {
        Random random = new Random();
        try {
            Thread.sleep((long) (200 * random.nextFloat()));
        } catch (InterruptedException ignore) {
        }
    }
}
