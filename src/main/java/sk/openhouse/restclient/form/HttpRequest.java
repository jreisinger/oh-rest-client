package sk.openhouse.restclient.form;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

/**
 * Http Request Form
 * 
 * @author pete
 */
public class HttpRequest {

    private HttpMethod httpMethod = HttpMethod.GET;

    @URL
    @Length(min = 1, max = 200)
    private String url;

    @Length(min = 0, max = 500)
    private String headers;

    @Length(min = 0, max = 1000)
    private String body;

    /**
     * @return http method (GET, POST ...)
     */
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    /**
     * @param method http method (GET, POST ...)
     */
    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return request headers
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * @param headers request headers
     */
    public void setHeaders(String headers) {
        this.headers = headers;
    }

    /**
     * @return request body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body request body
     */
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public final int hashCode() {

        return new HashCodeBuilder()
                .append(httpMethod)
                .append(url)
                .append(headers)
                .append(body)
                .toHashCode();
    }

    @Override
    public final boolean equals(final Object obj) {

        if(!(obj instanceof HttpRequest)) {
            return false;
        }

        final HttpRequest other = (HttpRequest) obj;
        return new EqualsBuilder()
            .append(httpMethod, other.httpMethod)
            .append(url, other.url)
            .append(headers, other.headers)
            .append(body, other.body)
            .isEquals();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this)
                .append("httpMethod", httpMethod)
                .append("uri", url)
                .append("requestHeaders", headers)
                .append("body", body)
                .toString();
    }
}
