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

import java.util.LinkedList;

/**
 * This class is used to count and observe the frequency, duration, and concurrency
 * of events that have distinct start/stop times.
 * <p>
 * <code>SessionCounter</code> events may overlap. In other words, a second event may start before
 * the first one stops. For a given <code>SessionCounter</code>, its <i>concurrency</i> is the number of events that
 * have started and not yet stopped.
 */
public class SessionCounter extends EventCounter implements AutoCloseable {
    private static final long serialVersionUID = 1L;
    // queue of session start times for sessions that have started but not yet stopped.
    private LinkedList<Long> startTimes;
    private SessionCounterInterval peakConcurrency;
    private long recentSessionTime;
    private long maxSessionTime;
    private long maxSessionStartTime;
    private float cumulativeSessionTime;

    public SessionCounter() {
    }

    /**
     * Constructor for the SessionCounter object
     *
     * @param inLabel           Descriptive label for this SessionCounter
     * @param inIntervalSeconds number of seconds in each interval of this SessionCounter
     */
    public SessionCounter(String inLabel, int inIntervalSeconds) {
        super(inLabel, inIntervalSeconds);
        reset();
    }

    /**
     * Constructs a new SessionCounter.
     *
     * @param inLabel Description of the Parameter
     */
    public SessionCounter(String inLabel) {
        this(inLabel, 1);
    }

    /**
     * Get an instance of a SessionCounter with a particular label from the
     * MetricRepository, creating one if necessary.
     *
     * @param inLabel - descriptive label for this SessionCounter
     * @return The sessionCounter value
     */
    public static synchronized SessionCounter getSessionCounter(String inLabel) {
        SessionCounter metric = (SessionCounter) MetricRepository.get(inLabel);
        if (metric == null) {
            metric = new SessionCounter(inLabel);
            MetricRepository.put(metric);
        }
        return metric;
    }

    /**
     * Get an instance of a SessionCounter with a particular label from the
     * MetricRepository. If that SessionCounter does not already exist, one is
     * created with the given label and the designated interval size.
     *
     * @param inLabel           - descriptive label for this SessionCounter
     * @param inIntervalSeconds number of seconds in each interval of this SessionCounter
     * @return The sessionCounter value
     */
    public static synchronized SessionCounter getSessionCounter(String inLabel, int inIntervalSeconds) {
        SessionCounter metric = (SessionCounter) MetricRepository.get(inLabel);
        if (metric == null) {
            metric = new SessionCounter(inLabel, inIntervalSeconds);
            MetricRepository.put(metric);
        }
        return metric;
    }

    /**
     * Reset this <code>SessionCounter</code> to its "zero" condition so that it
     * can be reused, avoiding the overhead of constructing a new instance.
     */
    @Override
    public void reset() {
        super.reset();
        cumulativeSessionTime = recentSessionTime = maxSessionTime = 0;
        startTimes = new LinkedList<>();
    }

    /**
     * Gets the current concurrency, the number of sessions that have been added
     * (started) without having yet been removed (stopped).
     *
     * @return The concurrency value
     */
    public long getConcurrency() {
        return ((SessionCounterInterval) mCurrentInterval).getConcurrency();
    }

    /**
     * Gets the peak concurrency observed over the life of the metric. A
     * convenience method equivalent to
     * <code>getPeakConcurrencyInterval().getPeakConcurrency()</code>
     *
     * @return The peakConcurrency value
     */
    public long getPeakConcurrency() {
        return getPeakConcurrencyInterval().getPeakConcurrency();
    }

    /**
     * Gets the interval in which the highest concurrency was noted.
     *
     * @return Interval - having the highest concurrency
     */
    public SessionCounterInterval getPeakConcurrencyInterval() {
        return peakConcurrency;
    }

    /**
     * Gets the number of milliseconds of elapsed time spent within sessions
     * observed by this <code>SessionCounter</code>. Incomplete sessions
     * -- those that have been started and not stopped -- are not included.
     *
     * @return sum of session times for all completed sessions
     */
    public float getSessionTime() {
        return cumulativeSessionTime;
    }

    /**
     * Gets the number of milliseconds that a recent completed session took. If
     * sessions are happening concurrently, then this implementation may not be
     * able to give a correct value; if this is the case, 0 is returned.
     *
     * @return The recentSessionTime value
     */
    public long getRecentSessionTime() {
        return recentSessionTime;
    }

    /**
     * Gets the number of milliseconds that elapsed during the longest completed
     * session. If sessions are happening concurrently, then this implementation
     * cannot give a correct value and 0 is returned.
     *
     * @return The maximumSessionTime value
     */
    public long getMaximumSessionTime() {
        return maxSessionTime;
    }

    public long getMaximumSessionStartTime() {
        return maxSessionStartTime;
    }

    /**
     * Gets the mean session time in milliseconds. In other words, the average
     * amount of time spent within a session. Sessions that have not completed are
     * not included in the computation.
     *
     * @return The sessionTimeMean value
     */
    public float getSessionTimeMean() {
        long i = getCount() - startTimes.size();
        return i == 0 ? 0.0f : getSessionTime() / (float) i;
    }

    /**
     * Counts the beginning of a session.
     * <p>
     * The implementation supports adding only one session at a time. Hence, the
     * argument is ignored and treated as 1.
     *
     * @param inIgnored - ignored for SessionCounter
     */
    @Override
    public void add(int inIgnored) {
        this.start();
    }

    /**
     * Functionally identical to add(), supporting the start/stop terminology
     * which is more natural then add/remove when the emphasis is on measuring
     * elapsed time within critical sections.
     */
    public synchronized void start() {
        super.add(1);
        checkPeakConcurrency();
        startTimes.add(mNow);
    }

    public SessionCounter autoCloseableStart() {
        start();
        return this;
    }

    private void checkPeakConcurrency() {
        SessionCounterInterval currentInterval = (SessionCounterInterval) mCurrentInterval;
        if (currentInterval.getPeakConcurrency() > peakConcurrency.getPeakConcurrency()) {
            // This is a new peak
            peakConcurrency.copy(currentInterval);
        }
    }

    /**
     * Functionally identical to remove(). See start().
     */
    public void stop() {
        remove(1);
    }

    /**
     * Informs this EventCounter that a previously added event is "finished" and
     * no longer among the concurrent events. The argument is ignored.
     *
     * @param inCount - number of events to be removed
     */
    public synchronized void remove(int inCount) {
        if (startTimes.size() == 0) {
            // Ignore stops without starts
            return;
        }

        normalize();
        ((SessionCounterInterval) mCurrentInterval).remove(1);

        long duration = mNow - startTimes.remove();
        cumulativeSessionTime += duration;

        recentSessionTime = duration;
        if (recentSessionTime > maxSessionTime) {
            maxSessionTime = recentSessionTime;
            maxSessionStartTime = mNow - maxSessionTime;
        }
    }

    /**
     * Removes a single event. Equivalent to remove(1).
     */
    public void remove() {
        remove(1);
    }

    @Override
    protected void createIntervals() {
        mPeakEvents = new EventCounterInterval(intervalSeconds * 1000, startTime);
        mCurrentInterval = new SessionCounterInterval(intervalSeconds * 1000, startTime);
        mPriorInterval = new SessionCounterInterval(intervalSeconds * 1000, startTime);
        peakConcurrency = new SessionCounterInterval(intervalSeconds * 1000, startTime);
    }

    @Override
    protected void normalizeCurrent_ScenarioA() {
        SessionCounterInterval currentInterval = (SessionCounterInterval) mCurrentInterval;
        currentInterval.setPriorConcurrency(currentInterval.getConcurrency());
        currentInterval.setEventStops(0);
        currentInterval.setPeakConcurrency(currentInterval.getPriorConcurrency());
        super.normalizeCurrent_ScenarioA();
    }

    @Override
    protected void normalizePrior_ScenarioA() {
        SessionCounterInterval priorInterval = (SessionCounterInterval) mPriorInterval;
        SessionCounterInterval currentInterval = (SessionCounterInterval) mCurrentInterval;
        priorInterval.setPriorConcurrency(currentInterval.getPriorConcurrency());
        priorInterval.setEventStops(currentInterval.getEventStops());
        priorInterval.setPeakConcurrency(0);
        super.normalizePrior_ScenarioA();
    }

    @Override
    protected void normalizeCurrent_ScenarioB() {
        SessionCounterInterval currentInterval = (SessionCounterInterval) mCurrentInterval;
        currentInterval.setPriorConcurrency(currentInterval.getConcurrency());
        currentInterval.setEventStops(0);
        currentInterval.setPeakConcurrency(currentInterval.getPriorConcurrency());
        super.normalizeCurrent_ScenarioB();
    }

    @Override
    protected void normalizePrior_ScenarioB(long nEmptyIntervals) {
        SessionCounterInterval priorInterval = (SessionCounterInterval) mPriorInterval;
        SessionCounterInterval currentInterval = (SessionCounterInterval) mCurrentInterval;
        priorInterval.setPriorConcurrency(currentInterval.getConcurrency());
        priorInterval.setEventStops(0);
        priorInterval.setPeakConcurrency(0);
        super.normalizePrior_ScenarioB(nEmptyIntervals);
    }

    @Override
    public void close() {
        stop();
    }
}
