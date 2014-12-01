package com.freemansoft.metrics.flowexception;

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
     * Used when creating the mbean domain name multiple apps can run codahale
     */
    private String appName;
    
    /**
     * We retain a reference here so. We only want one instance of the
     * JMXReporter
     */
    private JmxReporter reporter = null;

    /**
     * Spring constructor
     */
    public MetricAgent() {
        // abstract only has a constructor that takes a name
        super("name is inconsequential because it is overridden later");
    }

    /**
     * Called on app startup.  Creates mbean registry domain
     */
    @Override
    public void initialise() throws InitialisationException {
        if (metricRegistry == null) {
            throw new IllegalArgumentException("metricRegistry not set");
        }
        // need to differentiate by app name
        if (reporter == null) {
            reporter = JmxReporter.forRegistry(metricRegistry).inDomain("Mule."+appName+".metrics").build();
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
        // redeploy always creates a new reporter with new registry
        // close the reporter so that jmx tools don't stay connected to reporter
        // / beans
        reporter.stop();
        reporter.close();
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

    /**
     * 
     * @return injected app name used in mbean domain
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 
     * @param appName injected app name used in mbean domain
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }


}
