/*
 * Copyright 2005-2020 by BerryWorks Software, LLC. All rights reserved.
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

    public static final String NEWLINE = System.lineSeparator();

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

    public static String asHtmlTable(Metric... metrics) {
        StringBuilder sb = new StringBuilder("<table>" + NEWLINE);
        sb.append("" +
                "    <col>" + NEWLINE +
                "    <colgroup span=\"2\"></colgroup>" + NEWLINE +
                "    <colgroup span=\"2\"></colgroup>" + NEWLINE +
                "    <tr>" + NEWLINE +
                "        <td rowspan=\"2\"></td>" + NEWLINE +
                "        <th colspan=\"2\" scope=\"colgroup\">Occurrence</th>" + NEWLINE +
                "        <th colspan=\"3\" scope=\"colgroup\">Duration</th>" + NEWLINE +
                "        <th colspan=\"2\" scope=\"colgroup\">Concurrency</th>" + NEWLINE +
                "    </tr>" + NEWLINE +
                "    <tr>" + NEWLINE +
                "        <th scope=\"col\">Count</th>" + NEWLINE +
                "        <th scope=\"col\">Frequency</th>" + NEWLINE +
                "        <th scope=\"col\">Recent</th>" + NEWLINE +
                "        <th scope=\"col\">Mean</th>" + NEWLINE +
                "        <th scope=\"col\">Peak</th>" + NEWLINE +
                "        <th scope=\"col\">Recent</th>" + NEWLINE +
                "        <th scope=\"col\">Peak</th>" + NEWLINE +
                "    </tr>" + NEWLINE);
        for (Metric m : metrics) {
            if (m instanceof SessionCounter) {
                SessionCounter s = (SessionCounter) m;
                sb.append("    <tr>").append(NEWLINE);
                sb.append("        <th scope=\"row\">").append(s.getLabel()).append("</th>").append(NEWLINE);
                sb.append("        <td>").append(s.getCount()).append("</td>").append(NEWLINE);
                sb.append("        <td>").append(s.getCumulativeFreq()).append("</td>").append(NEWLINE);
                sb.append("        <td>").append(s.getRecentSessionTime()).append("</td>").append(NEWLINE);
                sb.append("        <td>").append(s.getSessionTimeMean()).append("</td>").append(NEWLINE);
                sb.append("        <td>").append(s.getMaximumSessionTime()).append("</td>").append(NEWLINE);
                sb.append("        <td>").append(s.getConcurrency()).append("</td>").append(NEWLINE);
                sb.append("        <td>").append(s.getPeakConcurrencyInterval().getConcurrency()).append("</td>").append(NEWLINE);
                sb.append("    </tr>").append(NEWLINE);
            }
        }
        sb.append("</table>" + NEWLINE);
        return sb.toString();
    }
}
