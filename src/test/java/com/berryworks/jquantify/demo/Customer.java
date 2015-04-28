package com.berryworks.jquantify.demo;

import java.io.Serializable;

public class Customer implements Serializable {
    private final int id;

    public Customer(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer " + id;
    }
}
