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

package com.berryworks.jquantify;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * A singleton repository for accessing <code>Metric</code> instances by their
 * label.
 */
public final class MetricRepository implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Map<String, Metric> repository = new HashMap<>();
    private static final MetricRepository instance = new MetricRepository();

    /**
     * Private constructor preventing others from instantiating this class.
     */
    private MetricRepository() {
    }

    /**
     * Returns the singleton instance of <code>MetricRepository</code>.
     *
     * @return The unique instance of this class.
     */
    public static MetricRepository instance() {
        return instance;
    }

    /**
     * Gets the labels for all metrics in the repository.
     *
     * @return set of labels
     */
    public Set<String> getLabels() {
        return repository.keySet();
    }

    /**
     * Gets a <code>Metric</code> with a specified label.
     *
     * @param inLabel String label
     * @return <code>Metric</code> with the specified label, or <code>null</code>
     *         if one does not exist.
     */
    public Metric getMetric(String inLabel) {
        return repository.get(inLabel);
    }

    /**
     * Places an instance of <code>Metric</code> into the repository.
     * <p>
     * If the repository already contains a <code>Metric</code> with the same
     * label, it is replaced with the specified one.
     *
     * @param inMetric Metric to be placed into the repository.
     */
    public void putMetric(Metric inMetric) {
        repository.put(inMetric.getLabel(), inMetric);
    }

    /**
     * Gets a <code>Metric</code> with a specified label.
     * <p>
     * This static method is equivalent to
     * <code>MetricRepository.instance().getMetric(label)</code>
     *
     * @param inLabel String label
     * @return <code>Metric</code> with the specified label, or <code>null</code>
     *         if one does not exist.
     */
    public static Metric get(String inLabel) {
        return instance().getMetric(inLabel);
    }

    /**
     * Places an instance of <code>Metric</code> into the repository.
     * <p>
     * This static method is equivalent to
     * <code>MetricRepository.instance().putMetric(metric)</code>
     *
     * @param inMetric <code>Metric</code> to be placed into the repository
     */
    public static void put(Metric inMetric) {
        instance().putMetric(inMetric);
    }

}

