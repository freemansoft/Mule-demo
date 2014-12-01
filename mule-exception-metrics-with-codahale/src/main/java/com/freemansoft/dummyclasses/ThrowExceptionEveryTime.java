package com.freemansoft.dummyclasses;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

/**
 * Dummy class that throws "Exception" every time it is called
 * 
 * @author joe@freemansoft.com
 */
public class ThrowExceptionEveryTime implements Callable {

    @Override
    public Object onCall(MuleEventContext eventContext) throws Exception {
        throw new IllegalStateException("This Java test throws this Java exception every time", new IllegalAccessException(
                "This test creates this inner exception"));
    }

}
