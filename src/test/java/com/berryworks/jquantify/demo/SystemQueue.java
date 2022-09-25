package com.berryworks.jquantify.demo;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SystemQueue {

    private final LinkedBlockingQueue<Customer> queue = new LinkedBlockingQueue<>();

    public void arrive(Customer customer) {
        queue.add(customer);
        System.out.println("Queued for service:       " + customer);
    }

    public Customer beginToServe(String serverLabel) {
        Customer customer;

        try {
            customer = queue.poll(1, TimeUnit.SECONDS);
            System.out.println("Server " + serverLabel + (customer == null ?
                    " has no customer to serve" :
                    " began serving    " + customer));
        } catch (InterruptedException e) {
            throw new RuntimeException("failure in beginToServe()", e);
        }

        return customer;
    }
}
