package com.freemansoft.metrics.flowexception;

import org.mule.api.ExceptionPayload;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

import com.codahale.metrics.MetricRegistry;
import com.freemansoft.metrics.MetricUtilities;

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

    public MetricRecordFlowException(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public Object onCall(MuleEventContext arg0) throws Exception {
        MuleMessage message = arg0.getMessage();
        StringBuffer outBuffer = new StringBuffer();
        ExceptionPayload ePayload = message.getExceptionPayload();
        if (ePayload != null && ePayload.getException() != null) {
            outBuffer.append("Exception");
            MetricUtilities.buildMetricNameFromException(outBuffer, ePayload.getException());
            this.metricRegistry.counter(outBuffer.toString()).inc();
        }
        // don't change the message
        return message;
    }

}
