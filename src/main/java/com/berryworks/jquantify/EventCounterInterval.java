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
     * <p/>
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
     * <p/>
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
        String result =
                "Interval " + "\nclosed=" + closed + " events=" + events + " priorEvents=" + priorEvents;
        return result;
    }

}

