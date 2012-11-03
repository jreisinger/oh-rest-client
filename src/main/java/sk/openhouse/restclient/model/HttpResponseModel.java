package sk.openhouse.restclient.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.Header;

/**
 * Represents http response
 * 
 * @author pete
 */
public final class HttpResponseModel {

    private final String statusLine;
    private final Header[] headers;
    private final String responseBody;

    /**
     * @param statusLine first line of the http response
     * @param httpHeaders http headers
     * @param responseBody response
     */
    public HttpResponseModel(String statusLine, Header[] headers, String responseBody) {

        this.statusLine = statusLine;
        this.headers = headers;
        this.responseBody = responseBody;
    }

    /**
     * @return first line of the http response
     */
    public String getStatusLine() {
        return statusLine;
    }

    /**
     * @return http headers
     */
    public Header[] getHeaders() {
        return headers;
    }

    /**
     * @return response
     */
    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public final int hashCode() {

        return new HashCodeBuilder()
                .append(statusLine)
                .append(headers)
                .append(responseBody)
                .toHashCode();
    }

    @Override
    public final boolean equals(final Object obj) {

        if(!(obj instanceof HttpResponseModel)) {
            return false;
        }

        final HttpResponseModel other = (HttpResponseModel) obj;
        return new EqualsBuilder()
            .append(statusLine, other.statusLine)
            .append(headers, other.headers)
            .append(responseBody, other.responseBody)
            .isEquals();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this)
                .append("statusLine", statusLine)
                .append("headers", headers)
                .append("responseBody", responseBody)
                .toString();
    }
}
