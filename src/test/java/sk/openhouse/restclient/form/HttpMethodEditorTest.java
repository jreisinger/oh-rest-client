package sk.openhouse.restclient.form;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HttpMethodEditorTest {

    private HttpMethodEditor httpMethodEditor;

    @BeforeMethod
    public void beforeMethod() {
        httpMethodEditor = new HttpMethodEditor();
    }

    @Test
    public void testSetAsTextNull() {

        httpMethodEditor.setAsText(null);
        HttpMethod httpMethod = (HttpMethod) httpMethodEditor.getValue();
        Assert.assertEquals(httpMethod, HttpMethod.GET);
    }

    @Test
    public void testSetAsTextEmpty() {

        httpMethodEditor.setAsText("");
        HttpMethod httpMethod = (HttpMethod) httpMethodEditor.getValue();
        Assert.assertEquals(httpMethod, HttpMethod.GET);
    }

    @Test
    public void testSetAsTextIncorrect() {

        httpMethodEditor.setAsText("incorrect method");
        HttpMethod httpMethod = (HttpMethod) httpMethodEditor.getValue();
        Assert.assertEquals(httpMethod, HttpMethod.GET);
    }

    @Test
    public void testSetAsText() {

        httpMethodEditor.setAsText("PUT");
        HttpMethod httpMethod = (HttpMethod) httpMethodEditor.getValue();
        Assert.assertEquals(httpMethod, HttpMethod.PUT);
    }
}
