package assistcontrol.domain;

import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommuneTest {

    @Test
    void communeQueryHandlesEmptyStringValues() {
        CommuneQuery communeQuery = new CommuneQuery();
        communeQuery.setName("");
        communeQuery.setSiiCode("");
        communeQuery.setTgrCode("");

        assertEquals("", communeQuery.getName());
        assertEquals("", communeQuery.getSiiCode());
        assertEquals("", communeQuery.getTgrCode());
    }

    @Test
    void communeQueryHandlesLongStringValues() {
        String longString = "a".repeat(1000);
        CommuneQuery communeQuery = new CommuneQuery();
        communeQuery.setName(longString);
        communeQuery.setSiiCode(longString);
        communeQuery.setTgrCode(longString);

        assertEquals(longString, communeQuery.getName());
        assertEquals(longString, communeQuery.getSiiCode());
        assertEquals(longString, communeQuery.getTgrCode());
    }

    @Test
    void communeQueryHandlesNegativeIds() {
        CommuneQuery communeQuery = new CommuneQuery();
        communeQuery.setId(-1L);
        communeQuery.setCityId(-2L);

        assertEquals(-1L, communeQuery.getId());
        assertEquals(-2L, communeQuery.getCityId());
    }

    @Test
    void communeQueryHandlesZeroIds() {
        CommuneQuery communeQuery = new CommuneQuery();
        communeQuery.setId(0L);
        communeQuery.setCityId(0L);

        assertEquals(0L, communeQuery.getId());
        assertEquals(0L, communeQuery.getCityId());
    }
}