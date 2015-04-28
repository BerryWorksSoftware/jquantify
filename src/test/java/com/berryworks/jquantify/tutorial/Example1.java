package com.berryworks.jquantify.tutorial;

public class Example1 extends Example {

    private int signonCounter;

    public static void main(String[] args) {
        runSimulation(new Example1());
    }

    @Override
    public void signon() {
        signonCounter++;
        performSignonWork();
    }

    @Override
    public void summarize() {
        System.out.println("total signons: " + signonCounter);
    }

}
