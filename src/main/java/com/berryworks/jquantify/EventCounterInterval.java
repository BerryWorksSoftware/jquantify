/*
 * Copyright 2005-2022 by BerryWorks Software, LLC. All rights reserved.
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
 * This class implements <code>Interval</code> for use by <code>EventCounter</code>.
 */
public class EventCounterInterval implements Interval {
    private static final long serialVersionUID = 1L;
    /**
     * Number of events that have been counted in this interval
     */
    protected volatile long events;
    private boolean closed;
    /**
     * Time stamp of the first millisecond contained within the interval
     */
    private long startTime;
    /**
     * Number of milliseconds contained within the interval
     */
    private long duration;
    /**
     * Number of counted events that occurred before the beginning of this
     * interval
     */
    private long priorEvents;

    public EventCounterInterval() {
    }

    /**
     * Constructs a new <code>EventCounterInterval</code>.
     * <p>
     * This constructor is not public because <code>Interval</code>s are
     * constructed only via <code>Metric</code> according to the Abstract Factory
     * design pattern.
     *
     * @param inDuration  - length of this interval
     * @param inStartTime - time stamp of the first millisecond of the interval
     */
    public EventCounterInterval(long inDuration, long inStartTime) {
        priorEvents = 0;
        startTime = inStartTime;
        duration = inDuration;
    }

    public EventCounterInterval copy(EventCounterInterval inFrom) {
        startTime = inFrom.startTime;
        duration = inFrom.duration;
        events = inFrom.events;
        priorEvents = inFrom.priorEvents;
        closed = inFrom.closed;
        return this;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public long getStartTime() {
        return startTime;
    }

    void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public float getEventRatePerSecond() {
        float denominator = (float) duration;
        if (!isPast()) {
            // Take the average over only through the current time
            long elapsedMilliseconds = Clock.now() - startTime;
            denominator = (elapsedMilliseconds <= 0) ? 1.0F : (float) elapsedMilliseconds;
        }
        return (events * 1000) / denominator;
    }

    @Override
    public long getEvents() {
        return events;
    }

    void setEvents(long events) {
        this.events = events;
    }

    long getPriorEvents() {
        return priorEvents;
    }

    void setPriorEvents(long priorEvents) {
        this.priorEvents = priorEvents;
    }

    @Override
    public long getCumulativeEvents() {
        return priorEvents + events;
    }

    @Override
    public void add(int inCount) {
        events += inCount;
    }

    @Override
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public boolean isPast() {
        if (closed) {
            // Since this interval is closed, we must be past its end time.
            return true;
        } else {
            boolean b = Clock.now() > startTime + duration;
            setClosed(b);
            return b;
        }
    }

    /**
     * Returns the number of intervals that have elapsed between this
     * <code>Interval</code> and the time specified by the argument.
     * <p>
     * The argument is typically a recently-noted timestamp.
     *
     * @param inTime - reference point in time
     * @return long - number of intervals
     */
    @Override
    public long intervalsBefore(long inTime) {
        long n = inTime - startTime;
        if (n < duration) {
            return 0;
        } else {
            setClosed(true);
            return n / duration;
        }
    }

    /**
     * Summarizes this <code>Interval</code> as a String for display purposes.
     *
     * @return String summary
     */
    @Override
    public String toString() {
        return "Interval " + "\nclosed=" + closed + " events=" + events + " priorEvents=" + priorEvents;
    }

}

