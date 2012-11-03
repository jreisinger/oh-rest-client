package sk.openhouse.restclient.service;

import sk.openhouse.restclient.form.HttpRequest;
import sk.openhouse.restclient.model.HttpResponseModel;

/**
 * Rest service for sending http requests
 * 
 * @author pete
 */
public interface RestService {

    /**
     * @param httpRequest request to be sent
     * @return response
     */
    HttpResponseModel sendRequest(HttpRequest httpRequest);
}
