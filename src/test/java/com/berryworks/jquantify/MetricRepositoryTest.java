package com.berryworks.jquantify;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class MetricRepositoryTest {

    final MetricRepository mRepository = MetricRepository.instance();

    @Test
    public void canPutAndGetMetrics() {
        Metric sessionCounter = new SessionCounter("sc");
        Metric eventCounter = new EventCounter("ec");

        mRepository.putMetric(sessionCounter);
        MetricRepository.put(eventCounter);

        assertSame(sessionCounter, MetricRepository.get("sc"));
        assertSame(eventCounter, mRepository.getMetric("ec"));
    }
}
