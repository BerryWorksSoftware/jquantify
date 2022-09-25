package com.berryworks.jquantify;

import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class JQuantifyTest {

    private SessionCounter sc;

    @Test
    public void canDirectlyConstructSessionCounter() {
        sc = new SessionCounter("sc0", 1);
        Assert.assertEquals(0, (int) sc.getCumulativeEvents());
        sc.start();
        sc.stop();
        Assert.assertEquals(1, (int) sc.getCumulativeEvents());
    }

    @Test
    public void canGetSessionCounterFromTheRepository() {
        MetricRepository.put(new SessionCounter("sc1"));
        Metric m = MetricRepository.get("sc1");

        Assert.assertTrue(m instanceof SessionCounter);
        sc = (SessionCounter) m;

        long n = sc.getCumulativeEvents();
        sc.start();
        sc.stop();
        Assert.assertEquals(n + 1, sc.getCumulativeEvents());
    }

    @Test
    public void canGetSessionCounterFromTheRepositoryIndirectly() {
        MetricRepository.put(new SessionCounter("sc2"));

        sc = SessionCounter.getSessionCounter("sc2");

        long n = sc.getCumulativeEvents();
        sc.start();
        sc.stop();
        Assert.assertEquals(n + 1, sc.getCumulativeEvents());
    }

    @Test
    public void canLazilyAndImplicitlyCreateSessionCounterInRepository() {
        sc = SessionCounter.getSessionCounter("sc3");

        long n = sc.getCumulativeEvents();
        sc.start();
        sc.stop();
        Assert.assertEquals(n + 1, sc.getCumulativeEvents());
    }

    @Test
    public void canIterateOverAllMetricsInRepository() {
        SessionCounter.getSessionCounter("sc4");
        SessionCounter.getSessionCounter("sc5");

        @SuppressWarnings("unchecked")
        Set<String> labels = MetricRepository.instance().getLabels();

        Assert.assertTrue(labels.contains("sc4"));
        Assert.assertTrue(labels.contains("sc5"));
    }

    @Test
    public void canImpliclitlyCreateSessionCounterInRepositoryWithNonDefaultInterval() {
        sc = SessionCounter.getSessionCounter("sc6", 10);
        Assert.assertEquals(10, sc.getIntervalSeconds());
    }

//  @Test
//  public void canUseVMUsage()
//  {
//    VMUsage vmUsage = new VMUsage("vm");
//    Assert.assertTrue(vmUsage.getActiveThreads() > 0);
//
//    // int n = vmUsage.getSampleCount();
//    vmUsage.add();
//    Assert.assertEquals(2, vmUsage.getSampleCount());
//
//    long freeMemory = vmUsage.getFreeMemory();
//    long totalMemory = vmUsage.getTotalMemory();
//    Assert.assertTrue(totalMemory > freeMemory);
//
////    Assert.assertTrue(vmUsage.toXML().contains("<memory>"));
//  }

    @Test
    public void thatMultiThreadedCountingWorks() throws InterruptedException {
        sc = SessionCounter.getSessionCounter("sc7");

        List<Thread> threads = new ArrayList<>();
        int n = 15;

        // Start n threads, each one counting on the same session counter.
        for (int i = 0; i < n; i++) {
            Thread countingThread = new Thread(new CountingThread(i, sc));
            threads.add(countingThread);
            countingThread.start();
        }

        // Wait until all of the counting threads have completed.
        for (Thread t : threads) {
            t.join();
        }

        Assert.assertEquals(n * 10, sc.getCumulativeEvents());
    }

    private static class CountingThread implements Runnable {
        private final SessionCounter mSessionCounter;

        public CountingThread(int inLabel, SessionCounter inSessionCounter) {
            this.mSessionCounter = inSessionCounter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignore) {
                }

                mSessionCounter.add();
            }
        }
    }
}
