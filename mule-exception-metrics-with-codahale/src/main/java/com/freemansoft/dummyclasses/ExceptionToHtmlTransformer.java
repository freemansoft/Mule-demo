package com.freemansoft.dummyclasses;

import org.mule.api.ExceptionPayload;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

import com.freemansoft.metrics.MetricUtilities;

/**
 * Code was all in a payload setter before as a groovy script that got
 * complicated
 * <p>
 * <code> 
 * Exception Class: #[groovy:message.getExceptionPayload().getException().getClass().getName()]<br>Exception Message: #[groovy:message.getExceptionPayload().getException().getMessage()]<br><br><br>Root Exception Class: #[groovy:message.getExceptionPayload().getRootException().getClass().getName()]<br>Root Exception Message: #[groovy:message.getExceptionPayload().getRootException().getMessage()]<br>Root Exception Cause: #[groovy:message.getExceptionPayload().getRootException().getCause()]<br>
 * </code>
 * 
 * @author joe@freemansoft.com
 */
public class ExceptionToHtmlTransformer implements Callable {

    @Override
    public Object onCall(MuleEventContext arg0) throws Exception {
        MuleMessage message = arg0.getMessage();
        ExceptionPayload ePayload = message.getExceptionPayload();

        StringBuffer outBuffer = new StringBuffer();
        outBuffer.append("<html><head><title>demo exceptions</title></head>" + "<body>");
        if (ePayload != null) {
            // ExceptionPayload.message is really the message of the top level
            // exception
            // outBuffer.append(ePayload.getMessage());
            if (ePayload.getException() != null) {
                outBuffer.append("<table border=1 cellspacing=0>");
                outBuffer.append("<tr><td>class name</td><td>message</td></tr>");
                MetricUtilities.buildHTMLString(outBuffer, ePayload.getException());
                outBuffer.append("</table>");
            }
        }
        outBuffer.append("</body></html>");

        message.setPayload(outBuffer.toString());
        return message;
    }

}
