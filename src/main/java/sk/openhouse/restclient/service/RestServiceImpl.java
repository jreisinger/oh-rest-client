package sk.openhouse.restclient.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

import sk.openhouse.restclient.form.HttpRequest;
import sk.openhouse.restclient.model.HttpResponseModel;

/**
 * Service for making (mainly REST) http requests
 * 
 * @author pete
 */
public class RestServiceImpl implements RestService {

    private static final Logger logger = Logger.getLogger(RestServiceImpl.class);

    private static final boolean HANDLE_REDIRECTS = false;

    private final HttpClient httpClient;

    /**
     * @param httpClient
     */
    public RestServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public HttpResponseModel sendRequest(HttpRequest httpRequest) {

        HttpUriRequest request = getRequest(httpRequest);
        HttpResponse response = null;
        String logErrorMessage = String.format("Error executing/sending request %s", request);
        try {
            response = httpClient.execute(request);
        } catch (ClientProtocolException e) {
            logger.error(logErrorMessage, e);
        } catch (IOException e) {
            logger.error(logErrorMessage, e);
        }

        if (null == response) {
            return null;
        }

        String statusLine = getStatusLine(response);
        Header[] headers = response.getAllHeaders();
        String responseBody = getResponseBody(response);
        return new HttpResponseModel(statusLine, headers, responseBody);
    }

    /**
     * @param response
     * @return status line from response as a string
     */
    public String getStatusLine(HttpResponse response) {

        StatusLine statusLine = response.getStatusLine();
        return String.format("%s %d %s", 
                statusLine.getProtocolVersion(), 
                statusLine.getStatusCode(), 
                statusLine.getReasonPhrase());
    }

    /**
     * @param response
     * @return response body or null if any response body found
     */
    public String getResponseBody(HttpResponse response) {

        String responseBody = null;
        HttpEntity httpEntity = response.getEntity();
        if (null != httpEntity) {
            try {
                responseBody = new Scanner(httpEntity.getContent()).useDelimiter("\\A").next();
            } catch (NoSuchElementException e) {
                /* empty body */
                responseBody = "";
            } catch (IllegalStateException e) {
                logger.error("Scanner is closed cannot read response body.", e);
            } catch (IOException e) {
                logger.error(e);
            }
        }

        return responseBody;
    }

    /**
     * @param httpRequest
     * @return http uri request based on http method set on the supplied request model
     */
    public HttpUriRequest getRequest(HttpRequest httpRequest) {

        if (null == httpRequest || null == httpRequest.getHttpMethod()) {
            return null;
        }

        HttpUriRequest request = null;
        switch (httpRequest.getHttpMethod()) {
            case GET:
                request = new HttpGet(httpRequest.getUrl());
                break;
            case POST:
                request = new HttpPost(httpRequest.getUrl());
                break;
            case PUT:
                request = new HttpPut(httpRequest.getUrl());
                break;
            case DELETE:
                request = new HttpDelete(httpRequest.getUrl());
                break;
        }

        /* redirects disabled, return real response */
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.protocol.handle-redirects", HANDLE_REDIRECTS);

        request.setHeaders(getRequestHeaders(httpRequest));
        request.setParams(params);
        return request;
    }

    /**
     * @param httpRequest
     * @return converts headers string from request form to array of headers
     */
    public Header[] getRequestHeaders(HttpRequest httpRequest) {

        if (null == httpRequest || 
                (null == httpRequest.getHeaders() || httpRequest.getHeaders().isEmpty())) {

            return new Header[0];
        }

        List<Header> requestHeaders = new ArrayList<Header>();
        Scanner scanner = new Scanner(httpRequest.getHeaders());

        while (scanner.hasNextLine()) {

            String line = scanner.nextLine();
            String[] keyValue = line.split(" *: *", 2);
            if (keyValue.length > 1) {
                requestHeaders.add(new BasicHeader(keyValue[0], keyValue[1]));
            }
        }

        return requestHeaders.toArray(new Header[0]);
    }
}
