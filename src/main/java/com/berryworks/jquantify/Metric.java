package com.berryworks.jquantify;

import java.io.Serializable;


/**
 * Abstract superclass for <code>EventCounter</code>, <code>SessionCounter</code>, and other metric classes.
 * <p/>
 * Each <code>Metric</code> is associated with a descriptive label that is
 * used in reporting to suggest what is being counted.
 * <p/>
 * A <code>Metric</code> views time as a
 * series of non-overlapping time intervals. The duration of an interval is a
 * number of seconds and does not vary for the intervals within a given
 * <code>Metric</code>. At any point in time, a <code>Metric</code> maintains
 * knowledge of the current interval, as an <code>Interval</code> object, as
 * well as one or more <code>Interval</code>s that have were noted in the
 * history of this metric.
 * <p/>
 * <b>Choosing the duration of an interval</b>.
 * Each instance of a <code>Metric</code> has a fixed duration for each interval.
 * The choice of duration makes no difference in the actual count of event occurrences.
 * It may, however, influence other calculations relevant to a particular subclass
 * of <code>Metric</code>. For example, an <code>EventCounter</code> is able to report
 * the peak frequency of events. If you are counting train departures from a station,
 * it may be reasonable to configure the interval duration at one hour, and learn the peak frequency
 * rate in departures/hour. If you used the default duration of one second, the
 * peak frequency would probably never exceed 1.0 per second during even the busiest part of the day.
 * If, on the other hand, you are counting database commits, then a one second interval
 * may be appropriate, since detecting a spike at 100 per second might be very helpful
 * and could go unnoticed in a commits per hour rate.
 */
public abstract class Metric implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final long LOAD_TIMESTAMP = Clock.now();
    /**
     * When the <code>Metric</code> was constructed or reset
     */
    protected long startTime;
    /**
     * How many seconds are in each interval
     */
    protected int intervalSeconds;
    /**
     * Number of intervals that have occurred so far during the life of this
     * <code>Metric</code>
     */
    protected long totalIntervals;

    private String label;

    /**
     * Constructs a new Metric.
     *
     * @param label                     descriptive label for this Metric
     * @param intervalDurationInSeconds number of seconds in each interval
     */
    public Metric(String label, int intervalDurationInSeconds) {
        this.label = label;
        startTime = Clock.now();
        this.intervalSeconds = intervalDurationInSeconds;
        totalIntervals = 1;
    }

    /**
     * Constructs a new Metric with an interval duration of 1 second.
     *
     * @param label - descriptive label for this Metric
     */
    public Metric(String label) {
        this(label, 1);
    }

    /**
     * No-arg constructor necessary for serialization.
     */
    public Metric() {
    }

    /**
     * Gets the number of seconds in each interval.
     *
     * @return number of seconds in each interval
     */
    public int getIntervalSeconds() {
        return intervalSeconds;
    }

    /**
     * Gets the descriptive label associated with this Metric instance.
     *
     * @return descriptive label for this Metric
     */
    public String getLabel() {
        return label;
    }


    /**
     * Sets the descriptive label associated with this Metric instance.
     *
     * @param label descriptive label for this Metric
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Gets the age of this metric in milliseconds.
     *
     * @return age in milliseconds of this Metric
     */
    public long getAgeMillis() {
        return Clock.now() - startTime;
    }

    /**
     * Gets the age of this metric in seconds.
     *
     * @return age of this Metric in seconds
     */
    public float getAge() {
        return getAgeMillis() / 1000.0f;
    }

    /**
     * Adds a single event.
     * <p/>
     * Equivalent to add(1).
     */
    public void add() {
        add(1);
    }

    /**
     * Adds a number of events.
     * <p/>
     * All events are considered to have happened simultaneously.
     *
     * @param count number of events counted by this call
     */
    public abstract void add(int count);

    /**
     * Gets the total count of events.
     *
     * @return count
     */
    public abstract long getCount();

    /**
     * Gets number of intervals that have occurred during the life of this
     * <code>Metric</code>.
     *
     * @return number of intervals
     */
    public long getTotalIntervals() {
        return totalIntervals;
    }

    /**
     * Reset this <code>Metric</code> to its "zero" condition.
     */
    public void reset() {
        startTime = Clock.now();
    }

    /**
     * Return the uptime in hours, measured from when the Metric class was loaded
     * into the VM by the classloader.
     *
     * @return Description of the Return Value
     */
    public static float uptime() {
        long ageInMillis = Clock.now() - LOAD_TIMESTAMP;
        return ageInMillis / 3600000f;
    }

}
