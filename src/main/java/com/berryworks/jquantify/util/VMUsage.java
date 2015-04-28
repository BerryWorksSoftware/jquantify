/*
 * Copyright 2005-2015 by BerryWorks Software, LLC. All rights reserved.
 *
 *  This file is part of EDIReader. You may obtain a license for its use directly from
 *  BerryWorks Software, and you may also choose to use this software under the terms of the
 *  GPL version 3. Other products in the EDIReader software suite are available only by licensing
 *  with BerryWorks. Only those files bearing the GPL statement below are available under the GPL.
 *
 *  EDIReader is free software: you can redistribute it and/or modify it under the terms of the
 *  GNU General Public License as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  EDIReader is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with EDIReader.  If not, see <http://www.gnu.org/licenses/
 */

package com.berryworks.jquantify.util;

import com.berryworks.jquantify.Metric;

/**
 * A subclass of <code>Metric</code> that observes memory usage and number of
 * threads for the VM.
 */
public class VMUsage extends Metric {
    private static final long serialVersionUID = 1L;
    private int activeThreads;
    private int activeThreadsMinimum;
    private int activeThreadsMaximum;
    private float activeThreadsSum;
    private long freeMemory;
    private long freeMemoryMinimum;
    private long freeMemoryMaximum;
    private double freeMemorySum;
    private long totalMemory;
    private long totalMemoryMinimum;
    private long totalMemoryMaximum;
    private double totalMemorySum;
    private int samples;

    public VMUsage(String inLabel) {
        super(inLabel);
        freeMemory = totalMemory = 0;
        freeMemoryMaximum = totalMemoryMaximum = 0;
        freeMemorySum = totalMemorySum = 0.0f;

        activeThreads = activeThreadsMaximum = 0;
        activeThreadsSum = 0.0f;
        samples = 0;
        // Take a sample immediately. This guarantees that nSamples gets incremented
        // beyond zero avoiding the possibility of divide by zero on taking means.
        // It also gives us a good chance to initialize minimums.
        add();
        activeThreadsMinimum = activeThreads;
        freeMemoryMinimum = freeMemory;
        totalMemoryMinimum = totalMemory;
    }

    @Override
    public void reset() {
    }

    /**
     * Gets the sampleCount attribute of the VMUsage object
     *
     * @return The sampleCount value
     */
    public int getSampleCount() {
        return samples;
    }

    /**
     * Gets the activeThreads attribute of the VMUsage object
     *
     * @return The activeThreads value
     */
    public int getActiveThreads() {
        return activeThreads;
    }

    /**
     * Gets the activeThreadsMinimum attribute of the VMUsage object
     *
     * @return The activeThreadsMinimum value
     */
    public int getActiveThreadsMinimum() {
        return activeThreadsMinimum;
    }

    /**
     * Gets the activeThreadsMaximum attribute of the VMUsage object
     *
     * @return The activeThreadsMaximum value
     */
    public int getActiveThreadsMaximum() {
        return activeThreadsMaximum;
    }

    /**
     * Gets the activeThreadsMean attribute of the VMUsage object
     *
     * @return The activeThreadsMean value
     */
    public float getActiveThreadsMean() {
        return activeThreadsSum / samples;
    }

    /**
     * Gets the freeMemory attribute of the VMUsage object
     *
     * @return The freeMemory value
     */
    public long getFreeMemory() {
        return freeMemory;
    }

    /**
     * Gets the freeMemoryMinimum attribute of the VMUsage object
     *
     * @return The freeMemoryMinimum value
     */
    public long getFreeMemoryMinimum() {
        return freeMemoryMinimum;
    }

    /**
     * Gets the freeMemoryMaximum attribute of the VMUsage object
     *
     * @return The freeMemoryMaximum value
     */
    public long getFreeMemoryMaximum() {
        return freeMemoryMaximum;
    }

    /**
     * Gets the freeMemoryMean attribute of the VMUsage object
     *
     * @return The freeMemoryMean value
     */
    public double getFreeMemoryMean() {
        return freeMemorySum / samples;
    }

    /**
     * Gets the totalMemory attribute of the VMUsage object
     *
     * @return The totalMemory value
     */
    public long getTotalMemory() {
        return totalMemory;
    }

    /**
     * Gets the totalMemoryMinimum attribute of the VMUsage object
     *
     * @return The totalMemoryMinimum value
     */
    public long getTotalMemoryMinimum() {
        return totalMemoryMinimum;
    }

    /**
     * Gets the totalMemoryMaximum attribute of the VMUsage object
     *
     * @return The totalMemoryMaximum value
     */
    public long getTotalMemoryMaximum() {
        return totalMemoryMaximum;
    }

    /**
     * Gets the totalMemoryMean attribute of the VMUsage object
     *
     * @return The totalMemoryMean value
     */
    public double getTotalMemoryMean() {
        return totalMemorySum / samples;
    }

    /**
     * Gets the totalIntervals attribute of the VMUsage object
     *
     * @return 0 since VMUsage does not use intervals
     */
    @Override
    public long getTotalIntervals() {
        return 0;
    }

    /**
     * Take a sample of the current values from the VM.
     *
     * @param inCount is ignored
     */
    @Override
    public void add(int inCount) {
        samples++;

        activeThreads = Thread.activeCount();
        activeThreadsSum += activeThreads;
        if (activeThreads > activeThreadsMaximum) {
            activeThreadsMaximum = activeThreads;
        }
        if (activeThreads < activeThreadsMinimum) {
            activeThreadsMinimum = activeThreads;
        }

        freeMemory = Runtime.getRuntime().freeMemory();
        freeMemorySum += freeMemory / 1000000.0;
        if (freeMemory > freeMemoryMaximum) {
            freeMemoryMaximum = freeMemory;
        }
        if (freeMemory < freeMemoryMinimum) {
            freeMemoryMinimum = freeMemory;
        }

        totalMemory = Runtime.getRuntime().totalMemory();
        totalMemorySum += totalMemory / 1000000.0;
        if (totalMemory > totalMemoryMaximum) {
            totalMemoryMaximum = totalMemory;
        }
        if (totalMemory < totalMemoryMinimum) {
            totalMemoryMinimum = totalMemory;
        }
    }

    @Override
    public long getCount() {
        return samples;
    }
}
