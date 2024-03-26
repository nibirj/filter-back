package data;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import org.wisercat.test.assignment.protocol.FilterDTO;

public class FilterTest {

    @Test
    public void testEquals() {
        EqualsVerifier.simple().forClass(FilterDTO.class).verify();
    }
}
