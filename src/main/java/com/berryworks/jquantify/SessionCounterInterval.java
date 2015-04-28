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
        String result =
                super.toString() + "\nstops=" + eventStops + " priorConcurrency=" + priorConcurrency
                        + " peakConcurrency=" + peakConcurrency;
        return result;
    }

}
