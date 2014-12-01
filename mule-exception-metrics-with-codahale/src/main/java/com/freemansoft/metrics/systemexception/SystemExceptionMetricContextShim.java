package com.freemansoft.metrics.systemexception;

import org.mule.api.MuleContext;
import org.mule.api.context.MuleContextAware;

public class SystemExceptionMetricContextShim implements MuleContextAware {

    private SystemExceptionMetricListener metricAwareExceptionListener;

    public SystemExceptionMetricContextShim(SystemExceptionMetricListener metricAwareExceptionListener) {
        this.metricAwareExceptionListener = metricAwareExceptionListener;
    }

    @Override
    public void setMuleContext(MuleContext context) {
        // activate with current context so it can be chained to previous
        // listener or to a default
        metricAwareExceptionListener.activateWithContext(context);
    }

}
