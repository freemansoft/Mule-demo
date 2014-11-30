package com.freemansoft.metrics;

import org.mule.AbstractAgent;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.InitialisationException;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

/**
 * Initializes the JMX interface for the Coda Hale Library. Called by Mule on
 * startup.
 * <p>
 * 
 * 
 * @author joe@freemansoft.com
 * 
 */
public class MetricAgent extends AbstractAgent {

    /**
     * coda hale metric registry must be a shared instance or singleton so that
     * all metrics end up in the same registry. The registry is an injected
     * spring bean because other components need access to the registry.
     */
    private MetricRegistry metricRegistry;
    /**
     * We retain a reference here so. We only want one instance of the
     * JMXReporter
     */
    private JmxReporter reporter = null;

    /**
     * Zero argument constructor useful for spring
     */
    public MetricAgent() {
        super("name is inconsequential because it is overridden later");
    }

    /**
     * Standard constructor
     * 
     * @param name
     */
    public MetricAgent(String name) {
        super(name);
    }

    /**
     * Called on app startup
     */
    @Override
    public void initialise() throws InitialisationException {
        if (metricRegistry == null) {
            throw new IllegalArgumentException("metricRegistry not set");
        }
        if (reporter == null) {
            reporter = JmxReporter.forRegistry(metricRegistry).build();
            reporter.start();
        }
    }

    /**
     * Called when app starts.
     */
    @Override
    public void start() throws MuleException {
        // Increment this counter as a demonstration.
        // Should be able to see this in mbean viewer.
        metricRegistry.counter("MetricAgent.start").inc();
    }

    /**
     * called when app stops
     */
    @Override
    public void stop() throws MuleException {
        // Increment this counter as a demonstration.
        metricRegistry.counter("MetricAgent.stop").inc();
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
    }

    /**
     * Used as part of spring configuration
     * 
     * @return spring wired MetricRegistry
     */
    public MetricRegistry getMetricRegistry() {
        return metricRegistry;
    }

    /**
     * Used for setter injection for registry
     * 
     * @param metricRegistry
     *            our shared registry that was configrued as spring bean
     */
    public void setMetricRegistry(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

}
