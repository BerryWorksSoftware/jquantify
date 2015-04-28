package com.berryworks.jquantify.util;

import com.berryworks.jquantify.*;

public abstract class Format {

    public static String asCSV(EventCounter inEventCounter) {
        StringBuffer sb = new StringBuffer(1000);
        sb.append(metricAsCSV(inEventCounter));

        sb.append(", ").append(inEventCounter.getCount());
        sb.append(", ").append(Format.toDecimalFormat(inEventCounter.getCumulativeFreq()));
        sb.append(", ").append(Format.toDecimalFormat(inEventCounter.getCurrentFreq()));

        EventCounterInterval i = inEventCounter.getPeakEventsInterval();
        sb.append(", " + Format.toDecimalFormat(i.getEventRatePerSecond()));
        sb.append(", " + i.getStartTime());

        return sb.toString();
    }

    public static String asCSV(SessionCounter inSessionCounter) {
        StringBuffer sb = new StringBuffer(1000);
        sb.append(asCSV((EventCounter) inSessionCounter));

        sb.append(", ").append(inSessionCounter.getConcurrency());
        SessionCounterInterval p = inSessionCounter.getPeakConcurrencyInterval();
        sb.append(", ").append(p.getPeakConcurrency());
        sb.append(", ").append(p.getStartTime());
        sb.append(", ").append(inSessionCounter.getSessionTime());
        sb.append(", ").append(Format.toDecimalFormat(inSessionCounter.getSessionTimeMean()));
        sb.append(", ").append(inSessionCounter.getMaximumSessionTime());
        sb.append(", ").append(inSessionCounter.getMaximumSessionStartTime());
        sb.append(", ").append(inSessionCounter.getRecentSessionTime());

        return sb.toString();
    }

    public static String metricAsCSV(Metric inMetric) {
        StringBuffer sb = new StringBuffer(1000);
        sb.append(inMetric.getLabel());
        sb.append(", ").append(Format.toDecimalFormat(inMetric.getAge()));
        return sb.toString();
    }

    public static String percent(double inValue, double inBase) {
        double ratio = (100.0 * inValue) / inBase;
        return String.valueOf(ratio) + '%';
    }

    public static String toDecimalFormat(double inValue) {
        inValue += 0.05;
        long integerPortion = (long) inValue;
        String beforeTheDecimal = String.valueOf(integerPortion);
        String afterTheDecimal = String.valueOf((long) (10.0 * inValue) - integerPortion * 10);
        return beforeTheDecimal + '.' + afterTheDecimal;
    }

}
