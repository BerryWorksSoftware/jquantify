package com.berryworks.jquantify.util;

import com.berryworks.jquantify.EventCounter;
import com.berryworks.jquantify.MetricRepository;
import com.berryworks.jquantify.SessionCounter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FormatTest {
    SessionCounter mSessionCounter;
    EventCounter mEventCounter;

    @Before
    public void setUp() {
    }

    @Test
    public void testSessionCounterToCSV() {
        mSessionCounter = new SessionCounter("testCounter");

        mSessionCounter.start();
        mSessionCounter.stop();
        mSessionCounter.start();
        String text = Format.asCSV(mSessionCounter);

        String fields[] = text.split(",");
        assertEquals(15, fields.length);
        assertEquals("testCounter", fields[0]);

        String age = fields[1].trim();
//        assertTrue(age.startsWith("0.0"));

        String count = fields[2].trim();
        assertEquals("2", count);

        String overallFreq = fields[3].trim();
        assertTrue(Float.valueOf(overallFreq) >= 0.0);

        String recentFreq = fields[4].trim();
        assertEquals(Float.valueOf(recentFreq), mSessionCounter.getCurrentFreq(), 2000.0);

        String peakFreq = fields[5].trim();
        assertEquals(Float.valueOf(peakFreq),
                mSessionCounter.getPeakEventsInterval().getEventRatePerSecond(), 1.0);

        String peakFreqAt = fields[6].trim();
        assertEquals(Long.valueOf(peakFreqAt).longValue(),
                mSessionCounter.getPeakEventsInterval().getStartTime());

        String concurrency = fields[7].trim();
        assertEquals(Long.valueOf(concurrency).longValue(), mSessionCounter.getConcurrency());

        String peakConcurrency = fields[8].trim();
        assertEquals(Long.valueOf(peakConcurrency).longValue(), mSessionCounter.getPeakConcurrency());

        String peakConcurrencyAt = fields[9].trim();
        assertEquals(Long.valueOf(peakConcurrencyAt).longValue(),
                mSessionCounter.getPeakConcurrencyInterval().getStartTime());

//        String sessionTime = fields[10].trim();
//        assertEquals(Long.valueOf(sessionTime).longValue(), mSessionCounter.getSessionTime());

        String sessionAvg = fields[11].trim();
        assertEquals(Float.valueOf(sessionAvg).floatValue(), mSessionCounter.getSessionTimeMean(), 0.01);

        String sessionMax = fields[12].trim();
        assertEquals(Long.valueOf(sessionMax).longValue(), mSessionCounter.getMaximumSessionTime());

        String sessionMaxAt = fields[13].trim();
        assertEquals(Long.valueOf(sessionMaxAt).longValue(), mSessionCounter.getMaximumSessionStartTime());

        String sessionRecent = fields[14].trim();
        assertEquals(Long.valueOf(sessionRecent).longValue(), mSessionCounter.getRecentSessionTime());

//        assertEquals("?", text);
    }

    @Test
    public void testEventCounterToCSV() throws InterruptedException {
        String text;
        String fields[];

        mEventCounter = new EventCounter("testCounter");

        mEventCounter.add(100);
        mEventCounter.add(200);
        mEventCounter.add(400);

        fields = Format.asCSV(mEventCounter).split(",");

        assertEquals(7, fields.length);
        assertEquals("testCounter", fields[0]);

        String age = fields[1].trim();
        assertTrue(age.startsWith("0.0"));

        String count = fields[2].trim();
        assertEquals("700", count);

        Thread.sleep(1100);
        mEventCounter.add();

        fields = Format.asCSV(mEventCounter).split(",");
        assertEquals(7, fields.length);
        assertEquals("testCounter", fields[0]);

        age = fields[1].trim();
        assertTrue(age.startsWith("1."));

        count = fields[2].trim();
        assertEquals("701", count);

        String overallFreq = fields[3].trim();
        assertEquals(Float.valueOf(overallFreq), mEventCounter.getCumulativeFreq(), 1.0);
        assertTrue(overallFreq.startsWith("6"));

        String recentFreq = fields[4].trim();
        assertEquals(Float.valueOf(recentFreq), mEventCounter.getCurrentFreq(), 1.0);
        assertTrue(recentFreq.startsWith("6"));

        String peakFreq = fields[5].trim();
        assertEquals(Float.valueOf(peakFreq),
                mEventCounter.getPeakEventsInterval().getEventRatePerSecond(), 1.0);

        String peakFreqAt = fields[6].trim();
        assertEquals(Long.valueOf(peakFreqAt).longValue(),
                mEventCounter.getPeakEventsInterval().getStartTime());

//        assertEquals("?", Format.asCSV(mEventCounter));
    }

    @Test
    public void test100Percent() {
        assertEquals("100.0%", Format.percent(1.0f, 1.0f));
        assertEquals("100.0%", Format.percent(2.0f, 2.0f));
    }

    @Test
    public void test0Percent() {
        assertEquals("0.0%", Format.percent(0.0f, 1.0f));
        assertEquals("0.0%", Format.percent(0.0f, 2.0f));
    }

    @Test
    public void test50Percent() {
        assertEquals("50.0%", Format.percent(0.5f, 1.0f));
        assertEquals("50.0%", Format.percent(1.0f, 2.0f));
    }

    @Test
    public void testTwoThirdsAsPercent() {
        assertTrue(Format.percent(2.0f, 3.0f).startsWith("66.6"));
        assertTrue(Format.percent(2.0f, 3.0f).endsWith("%"));
    }

    @Test
    public void testDecimalFormat() {
        assertEquals("0.0", Format.toDecimalFormat(0.0));
        assertEquals("0.0", Format.toDecimalFormat(0.0f));
        assertEquals("0.0", Format.toDecimalFormat(0.04999));
        assertEquals("0.1", Format.toDecimalFormat(0.05001));
        assertEquals("0.3", Format.toDecimalFormat(0.3456));
        assertEquals("2.0", Format.toDecimalFormat(2.0));
        assertEquals("2.0", Format.toDecimalFormat(2.04999));
        assertEquals("2.1", Format.toDecimalFormat(2.05001));
        assertEquals("2.3", Format.toDecimalFormat(2.3456));
        assertEquals("12.0", Format.toDecimalFormat(11.99999));
        assertEquals("12.0", Format.toDecimalFormat(12.0));
        assertEquals("12.0", Format.toDecimalFormat(12.04999));
        assertEquals("12.1", Format.toDecimalFormat(12.05001));
        assertEquals("12.3", Format.toDecimalFormat(12.3456));
        assertEquals("100.0", Format.toDecimalFormat(99.99999));
    }

    @Test
    public void htmlTableBasics() {
        SessionCounter a = new SessionCounter("A");
        SessionCounter b = new SessionCounter("B");
        EventCounter c = new EventCounter("C");
        String actual = Format.asHtmlTable(a, b);
        assertEquals("" +
                "<table>\n" +
                "    <col>\n" +
                "    <colgroup span=\"2\"></colgroup>\n" +
                "    <tr>\n" +
                "        <td rowspan=\"2\"></td>\n" +
                "        <th colspan=\"1\" scope=\"colgroup\"></th>\n" +
                "        <th colspan=\"2\" scope=\"colgroup\" bgcolor=\"#dafffe\">Frequency</th>\n" +
                "        <th colspan=\"3\" scope=\"colgroup\" bgcolor=\"#ffedd6\">Duration</th>\n" +
                "        <th colspan=\"2\" scope=\"colgroup\" bgcolor=\"#dcffee\">Concurrency</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <th scope=\"col\" bgcolor=\"#fffee6\">Count</th>\n" +
                "        <th scope=\"col\" bgcolor=\"#dafffe\">Recent</th>\n" +
                "        <th scope=\"col\" bgcolor=\"#dafffe\">Peak</th>\n" +
                "        <th scope=\"col\" bgcolor=\"#ffedd6\">Recent</th>\n" +
                "        <th scope=\"col\" bgcolor=\"#ffedd6\">Mean</th>\n" +
                "        <th scope=\"col\" bgcolor=\"#ffedd6\">Peak</th>\n" +
                "        <th scope=\"col\" bgcolor=\"#dcffee\">Recent</th>\n" +
                "        <th scope=\"col\" bgcolor=\"#dcffee\">Peak</th>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <th scope=\"row\" align=\"left\">A</th>\n" +
                "        <td align=\"right\" bgcolor=\"#fffee6\">0</td>\n" +
                "        <td align=\"right\" bgcolor=\"#dafffe\">0.00</td>\n" +
                "        <td align=\"right\" bgcolor=\"#dafffe\">0.00</td>\n" +
                "        <td align=\"right\" bgcolor=\"#ffedd6\">0</td>\n" +
                "        <td align=\"right\" bgcolor=\"#ffedd6\">0.00</td>\n" +
                "        <td align=\"right\" bgcolor=\"#ffedd6\">0</td>\n" +
                "        <td align=\"right\" bgcolor=\"#dcffee\">0</td>\n" +
                "        <td align=\"right\" bgcolor=\"#dcffee\">0</td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <th scope=\"row\" align=\"left\">B</th>\n" +
                "        <td align=\"right\" bgcolor=\"#fffee6\">0</td>\n" +
                "        <td align=\"right\" bgcolor=\"#dafffe\">0.00</td>\n" +
                "        <td align=\"right\" bgcolor=\"#dafffe\">0.00</td>\n" +
                "        <td align=\"right\" bgcolor=\"#ffedd6\">0</td>\n" +
                "        <td align=\"right\" bgcolor=\"#ffedd6\">0.00</td>\n" +
                "        <td align=\"right\" bgcolor=\"#ffedd6\">0</td>\n" +
                "        <td align=\"right\" bgcolor=\"#dcffee\">0</td>\n" +
                "        <td align=\"right\" bgcolor=\"#dcffee\">0</td>\n" +
                "    </tr>\n" +
                "</table>\n", actual);
    }

    @Test
    public void canGenerateTableForAllMetrics() {
        SessionCounter z = new SessionCounter("Z");
        MetricRepository.put(z);
        MetricRepository.put(new SessionCounter("Y"));
        MetricRepository.put(new SessionCounter("X"));
        z.start();z.start();z.start();
        z.stop();z.stop();z.stop();
        String table = Format.asHtmlTable();
        assertTrue(table.startsWith("<table>"));
    }
}
