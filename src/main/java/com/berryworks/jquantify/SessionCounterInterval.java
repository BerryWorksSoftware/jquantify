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

package com.berryworks.jquantify;

/**
 * This class implements <code>Interval</code> for use by <code>SessionCounter</code>.
 */
public class SessionCounterInterval extends EventCounterInterval {
    private static final long serialVersionUID = 1L;
    /**
     * Number of event terminations (stops) observed within the scope of this interval
     */
    private long eventStops;
    /**
     * Event concurrency level as this interval began
     */
    private long priorConcurrency;
    /**
     * Peak event concurrency observed within the scope of this interval
     */
    private long peakConcurrency;

    public SessionCounterInterval() {
    }

    /**
     * Constructs a new <code>Interval</code>.
     *
     * @param inDuration  - length of this Interval
     * @param inStartTime - point in time corresponding to the beginning of this Interval
     */
    SessionCounterInterval(long inDuration, long inStartTime) {
        super(inDuration, inStartTime);
        peakConcurrency = priorConcurrency = 0;
    }

    long getEventStops() {
        return eventStops;
    }

    void setEventStops(long eventStops) {
        this.eventStops = eventStops;
    }

    long getPriorConcurrency() {
        return priorConcurrency;
    }

    void setPriorConcurrency(long priorConcurrency) {
        this.priorConcurrency = priorConcurrency;
    }

    /**
     * Gets the maximum concurrency level observed during this interval.
     *
     * @return The peakConcurrency value
     */
    public long getPeakConcurrency() {
        return peakConcurrency;
    }

    void setPeakConcurrency(long peakConcurrency) {
        this.peakConcurrency = peakConcurrency;
    }

    /**
     * Copies the state of the <code>Interval</code> argument into this
     * <code>SessionCounterInterval</code>.
     *
     * @param inFrom - SessionCounterInterval to be copied
     * @return the SessionCounterInterval into which values were copied
     */
    public SessionCounterInterval copy(SessionCounterInterval inFrom) {
        super.copy(inFrom);
        eventStops = inFrom.eventStops;
        priorConcurrency = inFrom.priorConcurrency;
        peakConcurrency = inFrom.peakConcurrency;
        return this;
    }

    /**
     * Gets the number of concurrent events underway at the present time.
     *
     * @return The concurrency value
     */
    public long getConcurrency() {
        return priorConcurrency - eventStops + events;
    }

    @Override
    public void add(int inCount) {
        super.add(inCount);
        long c = getConcurrency();
        if (c > peakConcurrency) {
            peakConcurrency = c;
        }
    }

    /**
     * Remove a number of sessions
     *
     * @param inCount - number of sessions to be removed
     */
    public void remove(int inCount) {
        eventStops += inCount;
    }

    @Override
    public String toString() {
        return super.toString() + "\nstops=" + eventStops + " priorConcurrency=" + priorConcurrency
                + " peakConcurrency=" + peakConcurrency;
    }

}
