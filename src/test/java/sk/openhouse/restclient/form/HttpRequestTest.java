package sk.openhouse.restclient.form;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.testng.annotations.Test;

public class HttpRequestTest {

    @Test
    public void testEquals() {

        EqualsVerifier.forClass(HttpRequest.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
