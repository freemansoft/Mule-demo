package com.freemansoft.metrics;

import java.util.List;

import org.mule.api.ExceptionPayload;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

import com.codahale.metrics.MetricRegistry;

/**
 * This increments a codahale metric for every caught exception routed through
 * this component. The metric name is
 * <p>
 * <code>Exception.exception_class_name.nested_exception_class_name...</code>
 * 
 * @author joe@freemansoft.com
 * 
 */
public class MetricRecordFlowException implements Callable {

    /**
     * Singleton injected that is the codahale registry exposed via JMX
     */
    private MetricRegistry metricRegistry;

    @Override
    public Object onCall(MuleEventContext arg0) throws Exception {
        MuleMessage message = arg0.getMessage();
        StringBuffer outBuffer = new StringBuffer();
        ExceptionPayload ePayload = message.getExceptionPayload();
        if (ePayload != null && ePayload.getException() != null) {
            outBuffer.append("Excpetion.");
            MetricRecordFlowException.buildMetricNameFromException(outBuffer,
                    ePayload.getException());
            this.metricRegistry.counter(outBuffer.toString()).inc();
        }
        // don't change the message
        return message;
    }

    /**
     * Creates a string for the metric name based on the nested exception names
     * 
     * @param buffer
     *            the buffer we record the table in
     * @param seed
     *            the originating Throwable
     */
    public static void buildMetricNameFromException(StringBuffer buffer,
            Throwable seed) {
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
    public static void buildExceptionList(List<Throwable> exceptions,
            Throwable seed) {
        if (seed != null) {
            exceptions.add(seed);
            if (seed.getCause() != null) {
                buildExceptionList(exceptions, seed.getCause());
            }
        }
    }

    /**
     * getter
     * 
     * @return codahale metric registry
     */
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    /**
     * setter
     * 
     * @param metricRegistry
     *            the codahale metric registry used for recording metrics
     */
    public void setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

}
