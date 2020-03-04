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
 * This class is used to count and observe the frequency of events.
 * <p/>
 * An <code>EventCounter</code> treats events as instantaneous,
 * happening at a specific point in time and with no observable duration.
 * For observing events that have both frequency and duration, consider
 * a <code>SessionCounter</code>.
 */
public class EventCounter extends Metric {
    private static final long serialVersionUID = 1L;
    protected EventCounterInterval mCurrentInterval;
    protected EventCounterInterval mPriorInterval;
    protected EventCounterInterval mPeakEvents;
    protected long mNow;

    public EventCounter() {
    }

    /**
     * Constructs an EventCounter with a specific interval duration.
     *
     * @param inLabel           descriptive String used in reporting
     * @param inIntervalSeconds int duration of each interval in seconds
     */
    public EventCounter(String inLabel, int inIntervalSeconds) {
        super(inLabel, inIntervalSeconds);
        createIntervals();
    }

    /**
     * Constructs a new EventCounter with an interval duration of 1 second.
     *
     * @param inLabel descriptive String used in reporting
     */
    public EventCounter(String inLabel) {
        this(inLabel, 1);
    }

    /**
     * Get an instance of an EventCounter with a particular label from the
     * MetricRepository, creating one if necessary.
     *
     * @param inLabel Description of the Parameter
     * @return The eventCounter value
     */
    public static synchronized EventCounter getEventCounter(String inLabel) {
        EventCounter metric = (EventCounter) MetricRepository.get(inLabel);
        if (metric == null) {
            metric = new EventCounter(inLabel);
            MetricRepository.put(metric);
        }
        return metric;
    }

    /**
     * Gets the current Interval, the one to which current events are added.
     *
     * @return Interval - the current interval
     */
    public Interval getCurrentInterval() {
        return mCurrentInterval;
    }

    /**
     * Gets the interval in which the greatest number of events were added.
     *
     * @return Interval - having the greatest getEvents()
     */
    public EventCounterInterval getPeakEventsInterval() {
        return mPeakEvents;
    }

    /**
     * Gets the number of events that occurred during the interval in which the
     * most events occurred. A convenience method equivalent to
     * <code>getPeakEventsInterval().getEvents()</code>
     *
     * @return The peakEvents value
     */
    public long getPeakEvents() {
        return getPeakEventsInterval().getEvents();
    }

    @Override
    public long getCount() {
        return getCumulativeEvents();
    }

    /**
     * Equivalent to getCount(), gets the total number of events added to this <code>EventCounter</code>.
     *
     * @return total number of events
     */
    public long getCumulativeEvents() {
        return mCurrentInterval.getCumulativeEvents();
    }

    /**
     * Gets the overall event frequency for this <code>EventCounter</code>
     *
     * @return float - events per second
     */
    public float getCumulativeFreq() {
        float age = getAge();
        return age < 0.001 ? 0.0f : (getCumulativeEvents() / age);
    }

    /**
     * Gets the current event frequency for this <code>EventCounter</code>. The
     * calculation includes the current incomplete interval. In order to reduce
     * excessive fluctuations when the current interval has just begun, the
     * immediately preceding interval, if one exists, is also included in the
     * calculation.
     *
     * @return float - events per second
     */
    public float getCurrentFreq() {
        long events = mCurrentInterval.getEvents();
        long elapsedMillis = Clock.now() - mCurrentInterval.getStartTime();
        if (elapsedMillis <= 0) {
            return 0.0f;
        } else if (isPriorIntervalRelevant()) {
            events += mPriorInterval.getEvents();
            elapsedMillis += mPriorInterval.getDuration();
        }
        return (events * 1000) / (float) elapsedMillis;
    }

    /**
     * Adds a number events.
     * <p/>
     * All events are considered to have happened now simultaneously.
     *
     * @param inCount number of events counted by this call
     */
    public synchronized void add(int inCount) {
        normalize();
        mCurrentInterval.add(inCount);
    }

    /**
     * Normalizes this EventCounter if necessary. An EventCounter is considered to
     * be normalized if the current time falls within the time span implied by the
     * currentInterval. Once enough time has elapsed for the currentInterval to be
     * outdated, then normalize() closes it and opens up a new currentInterval.
     */
    public void normalize() {
        mNow = Clock.now();
        long i = mCurrentInterval.intervalsBefore(mNow);

        // First check for the common case where the current time, now,
        // falls within the current interval. There is nothing to do.
        if (i <= 0)
            return;

        // At this point, we know that enough time has elapsed that the current
        // interval referenced by mCurrentInterval is no longer current and therefore
        // needs to be updated, along with mPriorInterval.  But first, it is the right
        // time to consider if the number of events that occurred in this current
        // interval -- a total that is now complete -- represents a new peak.
        if (mCurrentInterval.getEvents() > mPeakEvents.getEvents()) {
            mPeakEvents.copy(mCurrentInterval);
        }

        if (i == 1) {
            // Scenario A: now is in the time interval immediately
            // following currentInterval.
            normalizePrior_ScenarioA();
            normalizeCurrent_ScenarioA();
            totalIntervals++;

        } else if (i > 1) {
            // Scenario B: one or more "empty" time intervals lie between
            // currentInterval and now.
            normalizePrior_ScenarioB(i - 1);
            normalizeCurrent_ScenarioB();
            totalIntervals += i;
        }
    }

    protected void normalizeCurrent_ScenarioA() {
        mCurrentInterval.setClosed(false);
        mCurrentInterval.setStartTime(mCurrentInterval.getStartTime() + mCurrentInterval.getDuration());
        mCurrentInterval.setPriorEvents(mCurrentInterval.getPriorEvents() + mCurrentInterval.getEvents());
        mCurrentInterval.setEvents(0);
    }

    protected void normalizePrior_ScenarioA() {
        mPriorInterval.setClosed(true);
        mPriorInterval.setStartTime(mCurrentInterval.getStartTime());
        mPriorInterval.setEvents(mCurrentInterval.getEvents());
        mPriorInterval.setPriorEvents(mCurrentInterval.getPriorEvents());
    }

    protected void normalizeCurrent_ScenarioB() {
        mCurrentInterval.setClosed(false);
        mCurrentInterval.setStartTime(mPriorInterval.getStartTime() + mCurrentInterval.getDuration());
        mCurrentInterval.setPriorEvents(mPriorInterval.getPriorEvents());
        mCurrentInterval.setEvents(0);
    }

    protected void normalizePrior_ScenarioB(long nEmptyIntervals) {
        mPriorInterval.setClosed(true);
        mPriorInterval.setStartTime(mCurrentInterval.getStartTime() + mCurrentInterval.getDuration() * nEmptyIntervals);
        mPriorInterval.setPriorEvents(mCurrentInterval.getPriorEvents() + mCurrentInterval.getEvents());
        mPriorInterval.setEvents(0);
    }

    /**
     * Reset this <code>EventCounter</code> to its "zero" condition so that it can
     * be reused, avoiding the overhead of constructing a new instance.
     */
    public void reset() {
        super.reset();
        createIntervals();
    }

    protected void createIntervals() {
        mCurrentInterval = new EventCounterInterval(intervalSeconds * 1000, startTime);
        mPriorInterval = new EventCounterInterval(intervalSeconds * 1000, startTime);
        mPeakEvents = new EventCounterInterval(intervalSeconds * 1000, startTime);
    }

    private boolean isPriorIntervalRelevant() {
        return (mPriorInterval != null) && mPriorInterval.getStartTime() != mCurrentInterval.getStartTime();
    }
}
