package sk.openhouse.restclient.form;

import java.beans.PropertyEditorSupport;

import org.apache.log4j.Logger;

/**
 * Custom property editor for setting enum value. If the value is incorrect, then it
 * is defaulted to default method instead of throwing exception.
 * 
 * @author pete
 */
public class HttpMethodEditor extends PropertyEditorSupport {

    private static final Logger logger = Logger.getLogger(HttpMethodEditor.class);

    private static final HttpMethod DEFAULT_HTTP_METHOD = HttpMethod.GET;

    @Override
    public void setAsText(String text) {

        if (null == text) {
            setValue(DEFAULT_HTTP_METHOD);
            return;
        }

        try {
            setValue(Enum.valueOf(HttpMethod.class, text));
        } catch(IllegalArgumentException e) {
            logger.error(String.format("Http method %s is not allowed. Setting to default method - %s", 
                    text, DEFAULT_HTTP_METHOD));
            setValue(DEFAULT_HTTP_METHOD);
        }
    }
}
