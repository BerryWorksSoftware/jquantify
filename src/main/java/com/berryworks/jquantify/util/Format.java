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

import com.berryworks.jquantify.*;

public abstract class Format {

    public static String asCSV(EventCounter inEventCounter) {
        StringBuilder sb = new StringBuilder(1000);
        sb.append(metricAsCSV(inEventCounter));

        sb.append(", ").append(inEventCounter.getCount());
        sb.append(", ").append(Format.toDecimalFormat(inEventCounter.getCumulativeFreq()));
        sb.append(", ").append(Format.toDecimalFormat(inEventCounter.getCurrentFreq()));

        EventCounterInterval i = inEventCounter.getPeakEventsInterval();
        sb.append(", ").append(Format.toDecimalFormat(i.getEventRatePerSecond()));
        sb.append(", ").append(i.getStartTime());

        return sb.toString();
    }

    public static String asCSV(SessionCounter inSessionCounter) {
        StringBuilder sb = new StringBuilder(1000);
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
        return inMetric.getLabel() + ", " + Format.toDecimalFormat(inMetric.getAge());
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
