package com.freemansoft.metrics.systemexception;

import org.mule.api.MuleContext;
import org.mule.api.exception.RollbackSourceCallback;
import org.mule.api.exception.SystemExceptionHandler;
import org.mule.exception.DefaultSystemExceptionStrategy;
import org.mule.transport.ConnectException;

import com.codahale.metrics.MetricRegistry;
import com.freemansoft.metrics.MetricUtilities;

public class SystemExceptionMetricListener implements SystemExceptionHandler {

    private SystemExceptionHandler wrappedStrategy;

    private MetricRegistry metricRegistry;

    public SystemExceptionMetricListener(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    /**
     * plugs this object into the context
     * 
     * @param context
     *            application context
     */
    public void activateWithContext(MuleContext context) {
        if (context.getExceptionListener() != null) {
            wrappedStrategy = context.getExceptionListener();
        } else {
            wrappedStrategy = new DefaultSystemExceptionStrategy(context);
        }
        context.setExceptionListener(this);
    }

    @Override
    public void handleException(Exception exception, RollbackSourceCallback rollbackMethod) {
        generateMetrics(exception);
        wrappedStrategy.handleException(exception, rollbackMethod);
    }

    @Override
    public void handleException(Exception exception) {
        generateMetrics(exception);
        wrappedStrategy.handleException(exception);
    }

    /**
     * Tear apart exception and generate metric for it
     * 
     * @param exception that we are generating counter for
     */
    private void generateMetrics(Exception exception) {
        StringBuffer outBuffer = new StringBuffer();
        if (exception != null) {
            outBuffer.append("SystemException");
            MetricUtilities.buildMetricNameFromException(outBuffer, exception);
            if (exception instanceof ConnectException) {
                ConnectException connectException = (ConnectException)exception;
                outBuffer.append(".");
                outBuffer.append(connectException.getFailed().getClass().getSimpleName());
                // Connectors don't implement a name interface.
                // Some connectors do have a name() method which 
                // we should call here to differentiate between connectors of the same type
            }
            this.metricRegistry.counter(outBuffer.toString()).inc();
        }
    }

}
