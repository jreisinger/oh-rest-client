package sk.openhouse.restclient.model;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.testng.annotations.Test;

public class HttpResponseModelTest {

    @Test
    public void testEquals() {
        EqualsVerifier.forClass(HttpResponseModel.class).verify();
    }
}
