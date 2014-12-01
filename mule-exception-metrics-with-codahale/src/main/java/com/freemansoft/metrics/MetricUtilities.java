package com.freemansoft.metrics;

import java.util.List;

public class MetricUtilities {

    /**
     * Creates a string for the metric name based on the nested exception names
     * 
     * @param buffer
     *            the buffer we record the table in
     * @param seed
     *            the originating Throwable
     */
    public static void buildMetricNameFromException(StringBuffer buffer, Throwable seed) {
        if (seed != null) {
            buffer.append(".");
            buffer.append(seed.getClass().getSimpleName());
            if (seed.getCause() != null) {
                buildMetricNameFromException(buffer, seed.getCause());
            }
        }
    }

    /**
     * Creates a table from nested Throwable objects where each row of the table
     * is the cause of the one previous
     * 
     * @param buffer
     *            the buffer we record the table in
     * @param seed
     *            the originating Throwable
     */
    public static void buildHTMLString(StringBuffer buffer, Throwable seed) {
        if (seed != null) {
            buffer.append("<tr><td>");
            buffer.append(seed.getClass().getName());
            buffer.append("</td><td>");
            buffer.append(seed.getMessage());
            buffer.append("</td></tr>");
            if (seed.getCause() != null) {
                buildHTMLString(buffer, seed.getCause());
            }
        }
    }

    /**
     * Creates a list of Throwable objects where each element is the cause of
     * the one previous.
     * 
     * @param exceptions
     *            the list we populate with the unpacked exceptions
     * @param seed
     *            the origintating Throwable
     */
    public static void buildExceptionList(List<Throwable> exceptions, Throwable seed) {
        if (seed != null) {
            exceptions.add(seed);
            if (seed.getCause() != null) {
                buildExceptionList(exceptions, seed.getCause());
            }
        }
    }

}
