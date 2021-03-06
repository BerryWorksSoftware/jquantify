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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        StringBuilder sb = new StringBuilder("<table bgcolor=\"#fffae6\">" + NEWLINE);
        sb.append("" +
                "    <col>" + NEWLINE +
                "    <colgroup span=\"2\"></colgroup>" + NEWLINE +
                "    <tr>" + NEWLINE +
                "        <td rowspan=\"2\"></td>" + NEWLINE +
                "        <th colspan=\"1\" scope=\"colgroup\"></th>" + NEWLINE +
                "        <th colspan=\"3\" scope=\"colgroup\" bgcolor=\"#dafffe\">Frequency (/min)</th>" + NEWLINE +
                "        <th colspan=\"3\" scope=\"colgroup\" bgcolor=\"#ffedd6\">Duration (ms)</th>" + NEWLINE +
                "        <th colspan=\"2\" scope=\"colgroup\" bgcolor=\"#dcffee\">Concurrency</th>" + NEWLINE +
                "    </tr>" + NEWLINE +
                "    <tr>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#fffee6\">Count</th>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#dafffe\">Recent</th>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#dafffe\">Mean</th>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#dafffe\">Peak</th>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#ffedd6\">Recent</th>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#ffedd6\">Mean</th>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#ffedd6\">Peak</th>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#dcffee\">Recent</th>" + NEWLINE +
                "        <th scope=\"col\" bgcolor=\"#dcffee\">Peak</th>" + NEWLINE +
                "    </tr>" + NEWLINE);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for (Metric m : metrics) {
            if (m instanceof SessionCounter) {
                SessionCounter s = (SessionCounter) m;
                sb.append("    <tr>").append(NEWLINE);
                sb.append("        <th scope=\"row\" align=\"left\">").append(s.getLabel()).append("</th>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#fffee6\">").append(s.getCount()).append("</td>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#dafffe\">").append(decimalFormat.format(60.0 * s.getCurrentFreq())).append("</td>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#dafffe\">").append(decimalFormat.format(60.0 * s.getCumulativeFreq())).append("</td>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#dafffe\">").append(decimalFormat.format(60.0 * s.getPeakEventsInterval().getEventRatePerSecond())).append("</td>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#ffedd6\">").append(s.getRecentSessionTime()).append("</td>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#ffedd6\">").append((long) s.getSessionTimeMean()).append("</td>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#ffedd6\">").append(s.getMaximumSessionTime()).append("</td>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#dcffee\">").append(s.getConcurrency()).append("</td>").append(NEWLINE);
                sb.append("        <td align=\"right\" bgcolor=\"#dcffee\">").append(s.getPeakConcurrencyInterval().getConcurrency()).append("</td>").append(NEWLINE);
                sb.append("    </tr>").append(NEWLINE);
            }
        }
        sb.append("</table>" + NEWLINE);
        return sb.toString();
    }

    public static String asHtmlTable() {
        List<String> names = new ArrayList<>(MetricRepository.instance().getLabels());
        Collections.sort(names);
        Metric[] metrics = new Metric[names.size()];
        for (int n = 0; n < names.size(); n++) {
            metrics[n] = MetricRepository.get(names.get(n));
        }
        return asHtmlTable(metrics);
    }
}
