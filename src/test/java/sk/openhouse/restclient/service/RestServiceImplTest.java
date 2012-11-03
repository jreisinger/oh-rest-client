package sk.openhouse.restclient.service;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sk.openhouse.restclient.form.HttpMethod;
import sk.openhouse.restclient.form.HttpRequest;
import sk.openhouse.restclient.model.HttpResponseModel;

public class RestServiceImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private StatusLine statusLine;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private HttpEntity httpEntity;

    private RestServiceImpl restServiceImpl;

    @BeforeMethod
    public void beforeMethod() {

        MockitoAnnotations.initMocks(this);
        restServiceImpl = new RestServiceImpl(httpClient);
    }

    @Test
    public void testGetStatusLine() {

        Mockito.when(httpResponse.getStatusLine()).thenReturn(statusLine);

        restServiceImpl.getStatusLine(httpResponse);
        Mockito.verify(statusLine, Mockito.times(1)).getProtocolVersion();
        Mockito.verify(statusLine, Mockito.times(1)).getStatusCode();
        Mockito.verify(statusLine, Mockito.times(1)).getReasonPhrase();
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void testGetStatusLineNull() {
        restServiceImpl.getStatusLine(null);
    }

    @Test
    public void testGetRequestHeader() {

        String requestHeaders = "h1:xx\nh2  : : x:v\n\ntest:\ntest\n";
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHeaders(requestHeaders);

        Header[] headers = restServiceImpl.getRequestHeaders(httpRequest);
        Assert.assertEquals(headers.length, 3);

        Assert.assertEquals(headers[0].getName(), "h1");
        Assert.assertEquals(headers[0].getValue(), "xx");

        Assert.assertEquals(headers[1].getName(), "h2");
        Assert.assertEquals(headers[1].getValue(), ": x:v");

        Assert.assertEquals(headers[2].getName(), "test");
        Assert.assertEquals(headers[2].getValue(), "");
    }

    @Test
    public void testGetRequestHeaderNull() {

        Header[] headers = restServiceImpl.getRequestHeaders(null);
        Assert.assertEquals(headers.length, 0);

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHeaders(null);

        headers = restServiceImpl.getRequestHeaders(httpRequest);
        Assert.assertEquals(headers.length, 0);
    }

    @Test
    public void testGetRequestHeaderEmpty() {

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHeaders("");

        Header[] headers = restServiceImpl.getRequestHeaders(httpRequest);
        Assert.assertEquals(headers.length, 0);
    }

    @Test
    public void testGetRequest() {

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl("http://test.com");

        /* default method (none set) */
        HttpUriRequest request = restServiceImpl.getRequest(httpRequest);
        Assert.assertEquals(request.getRequestLine().getMethod(), "GET");

        httpRequest.setHttpMethod(HttpMethod.GET);
        request = restServiceImpl.getRequest(httpRequest);
        Assert.assertEquals(request.getRequestLine().getMethod(), "GET");

        httpRequest.setHttpMethod(HttpMethod.PUT);
        request = restServiceImpl.getRequest(httpRequest);
        Assert.assertEquals(request.getRequestLine().getMethod(), "PUT");

        httpRequest.setHttpMethod(HttpMethod.POST);
        request = restServiceImpl.getRequest(httpRequest);
        Assert.assertEquals(request.getRequestLine().getMethod(), "POST");

        httpRequest.setHttpMethod(HttpMethod.DELETE);
        request = restServiceImpl.getRequest(httpRequest);
        Assert.assertEquals(request.getRequestLine().getMethod(), "DELETE");
    }

    @Test
    public void testGetRequestNull() {

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHttpMethod(null);
        HttpUriRequest request = restServiceImpl.getRequest(httpRequest);
        Assert.assertNull(request);

        request = restServiceImpl.getRequest(null);
        Assert.assertNull(request);
    }

    @Test(expectedExceptions = {NullPointerException.class})
    public void testGetResponseBodyNull() {
        /* rest client should not return null, if it does we want to know */
        restServiceImpl.getResponseBody(null);
    }

    @Test
    public void testGetResponseBodyNullEntity() {

        Mockito.when(httpResponse.getEntity()).thenReturn(null);
        String response = restServiceImpl.getResponseBody(httpResponse);

        Assert.assertNull(response);
    }

    @Test
    public void testGetResponseBodyEmpty() throws IllegalStateException, IOException {

        /* empty response */
        Mockito.when(httpEntity.getContent()).thenThrow(new NoSuchElementException());
        Mockito.when(httpResponse.getEntity()).thenReturn(httpEntity);

        String response = restServiceImpl.getResponseBody(httpResponse);
        Assert.assertTrue(response.isEmpty());
    }

    @Test
    public void testGetResponseBodyIllegalStateException() throws IllegalStateException, IOException {

        /* empty response */
        Mockito.when(httpEntity.getContent()).thenThrow(new IllegalStateException());
        Mockito.when(httpResponse.getEntity()).thenReturn(httpEntity);

        String response = restServiceImpl.getResponseBody(httpResponse);
        Assert.assertNull(response);
    }

    @Test
    public void testGetResponseBodyIOException() throws IllegalStateException, IOException {

        /* empty response */
        Mockito.when(httpEntity.getContent()).thenThrow(new IOException());
        Mockito.when(httpResponse.getEntity()).thenReturn(httpEntity);

        String response = restServiceImpl.getResponseBody(httpResponse);
        Assert.assertNull(response);
    }

    @Test
    public void testSendRequestClientProtocolException() throws ClientProtocolException, IOException {

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl("http://test.com");

        Mockito.when(httpClient.execute(Mockito.any(HttpUriRequest.class))).thenThrow(new ClientProtocolException());
        HttpResponseModel response = restServiceImpl.sendRequest(httpRequest);

        Assert.assertNull(response);
    }

    @Test
    public void testSendRequestIOException() throws ClientProtocolException, IOException {

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl("http://test.com");

        Mockito.when(httpClient.execute(Mockito.any(HttpUriRequest.class))).thenThrow(new IOException());
        HttpResponseModel response = restServiceImpl.sendRequest(httpRequest);

        Assert.assertNull(response);
    }

    @Test
    public void testSendRequest() throws ClientProtocolException, IOException {

        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setUrl("http://test.com");

        Header[] headers = {new BasicHeader("test", "x")};

        Mockito.when(httpResponse.getStatusLine()).thenReturn(statusLine);
        Mockito.when(httpResponse.getAllHeaders()).thenReturn(headers);
        Mockito.when(httpResponse.getEntity()).thenReturn(null);
        Mockito.when(httpClient.execute(Mockito.any(HttpUriRequest.class))).thenReturn(httpResponse);
        HttpResponseModel response = restServiceImpl.sendRequest(httpRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getHeaders()[0].getName(), "test");
        Assert.assertEquals(response.getHeaders()[0].getValue(), "x");
    }
}
