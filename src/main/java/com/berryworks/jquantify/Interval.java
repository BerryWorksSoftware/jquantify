package com.berryworks.jquantify;

import java.io.Serializable;

/**
 * An object that counts events occurring during a single time slice
 * within the life of a <code>Metric</code> instance.
 * <p/>
 * The life of a <code>Metric</code> is conceptually a sequence of non-overlapping intervals.
 * Each <code>Interval</code> has a duration that is a whole number of seconds, and all intervals
 * for a given <code>Metric</code> have a uniform duration. Each <code>Interval</code> has a
 * start time, which can be determined from the start time of the previous <code>Interval</code>
 * of the <code>Metric</code> and its duration.
 */
public interface Interval extends Serializable {
    /**
     * Gets a timestamp corresponding to the start of this <code>Interval</code>
     * in <code>System.currentTimeMillis()</code> format.
     *
     * @return start time of this <code>Interval</code>
     */
    public long getStartTime();

    /**
     * Gets the duration, in milliseconds, of this <code>Interval</code>
     *
     * @return long - number of seconds
     */
    public long getDuration();

    /**
     * Gets the number of events counted during this interval.
     *
     * @return number of events
     */
    public long getEvents();

    /**
     * Returns the computed event rate per second during this
     * <code>Interval</code>.
     * <p/>
     * If the current time falls within this interval, consider only the time that has passed.
     * Otherwise, consider the entire duration of the interval.
     *
     * @return events per second
     */
    public float getEventRatePerSecond();

    /**
     * Gets the total number of events, including those that occurred
     * in previous intervals.
     *
     * @return number of events
     */
    public long getCumulativeEvents();

    /**
     * Returns true if the current time is after the end of this interval.
     *
     * @return true if the current time is beyond the end of this interval
     */
    boolean isPast();

    /**
     * Registers the occurrence of n new events.
     * <p/>
     * Intended for internal use only.
     *
     * @param count number of events to be counted
     */
    void add(int count);

    /**
     * Marks the interval as open or closed.
     * <p/>
     * Intended for internal use only.
     * A closed interval is one known to be fully in the past.
     * By flagging an interval as closed, we can calculate such things
     * as the event rate without sampling the clock.
     */
    void setClosed(boolean closed);

    /**
     * Returns the number of intervals that have elapsed between this
     * <code>Interval</code> and the time specified by the argument.
     *
     * @param time typically the current time
     * @return number of intervals
     */
    long intervalsBefore(long time);
}
