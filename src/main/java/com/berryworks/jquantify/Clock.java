package com.berryworks.jquantify;

abstract class Clock {
    public static long now() {
        return System.currentTimeMillis();
    }
}