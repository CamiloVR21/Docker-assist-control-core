package assistcontrol.domain.query;

import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompanyQueryTest {

    @Test
    void companyQueryInitialization() {
        CompanyQuery companyQuery = new CompanyQuery();
        assertNotNull(companyQuery);
    }

    @Test
    void companyQueryFieldsAreSetCorrectly() {
        CompanyQuery companyQuery = new CompanyQuery();

        CommuneQuery communeQuery = new CommuneQuery();
        communeQuery.setId(2L);

        companyQuery.setId(1L);
        companyQuery.setCommuneQuery(communeQuery);
        companyQuery.setCode("COMP123");
        companyQuery.setName("Test Company");
        companyQuery.setAddress("123 Test St");
        companyQuery.setPhone("1234567890");
        companyQuery.setAlias("TestAlias");
        companyQuery.setGiro("TestGiro");
        companyQuery.setEmail("test@example.com");
        companyQuery.setGeolocation(true);
        companyQuery.setSelfie(false);
        companyQuery.setLag(true);

        assertEquals(1L, companyQuery.getId());
        assertEquals(2L, companyQuery.getCommuneQuery().getId());
        assertEquals("COMP123", companyQuery.getCode());
        assertEquals("Test Company", companyQuery.getName());
        assertEquals("123 Test St", companyQuery.getAddress());
        assertEquals("1234567890", companyQuery.getPhone());
        assertEquals("TestAlias", companyQuery.getAlias());
        assertEquals("TestGiro", companyQuery.getGiro());
        assertEquals("test@example.com", companyQuery.getEmail());
        assertTrue(companyQuery.getGeolocation());
        assertFalse(companyQuery.getSelfie());
        assertTrue(companyQuery.getLag());
    }

    @Test
    void companyQueryHandlesNullValues() {
        CompanyQuery companyQuery = new CompanyQuery();
        companyQuery.setCommuneQuery(null);
        companyQuery.setCode(null);
        companyQuery.setName(null);
        companyQuery.setAddress(null);
        companyQuery.setPhone(null);
        companyQuery.setAlias(null);
        companyQuery.setGiro(null);
        companyQuery.setEmail(null);

        assertNull(companyQuery.getCommuneQuery());
        assertNull(companyQuery.getCode());
        assertNull(companyQuery.getName());
        assertNull(companyQuery.getAddress());
        assertNull(companyQuery.getPhone());
        assertNull(companyQuery.getAlias());
        assertNull(companyQuery.getGiro());
        assertNull(companyQuery.getEmail());
    }

    @Test
    void companyQueryEquality() {
        CommuneQuery communeQuery1 = new CommuneQuery();
        communeQuery1.setId(2L);

        CompanyQuery companyQuery1 = new CompanyQuery();
        companyQuery1.setId(1L);
        companyQuery1.setCommuneQuery(communeQuery1);
        companyQuery1.setCode("COMP123");
        companyQuery1.setName("Test Company");
        companyQuery1.setAddress("123 Test St");
        companyQuery1.setPhone("1234567890");
        companyQuery1.setAlias("TestAlias");
        companyQuery1.setGiro("TestGiro");
        companyQuery1.setEmail("test@example.com");
        companyQuery1.setGeolocation(true);
        companyQuery1.setSelfie(false);
        companyQuery1.setLag(true);

        CommuneQuery communeQuery2 = new CommuneQuery();
        communeQuery2.setId(2L);

        CompanyQuery companyQuery2 = new CompanyQuery();
        companyQuery2.setId(1L);
        companyQuery2.setCommuneQuery(communeQuery2);
        companyQuery2.setCode("COMP123");
        companyQuery2.setName("Test Company");
        companyQuery2.setAddress("123 Test St");
        companyQuery2.setPhone("1234567890");
        companyQuery2.setAlias("TestAlias");
        companyQuery2.setGiro("TestGiro");
        companyQuery2.setEmail("test@example.com");
        companyQuery2.setGeolocation(true);
        companyQuery2.setSelfie(false);
        companyQuery2.setLag(true);

        assertEquals(companyQuery1, companyQuery2);
    }

    @Test
    void companyQueryInequality() {
        CommuneQuery communeQuery1 = new CommuneQuery();
        communeQuery1.setId(2L);

        CompanyQuery companyQuery1 = new CompanyQuery();
        companyQuery1.setId(1L);
        companyQuery1.setCommuneQuery(communeQuery1);
        companyQuery1.setCode("COMP123");
        companyQuery1.setName("Test Company");
        companyQuery1.setAddress("123 Test St");
        companyQuery1.setPhone("1234567890");
        companyQuery1.setAlias("TestAlias");
        companyQuery1.setGiro("TestGiro");
        companyQuery1.setEmail("test@example.com");
        companyQuery1.setGeolocation(true);
        companyQuery1.setSelfie(false);
        companyQuery1.setLag(true);

        CommuneQuery communeQuery2 = new CommuneQuery();
        communeQuery2.setId(3L);

        CompanyQuery companyQuery2 = new CompanyQuery();
        companyQuery2.setId(2L);
        companyQuery2.setCommuneQuery(communeQuery2);
        companyQuery2.setCode("DIFF456");
        companyQuery2.setName("Different Company");
        companyQuery2.setAddress("456 Different St");
        companyQuery2.setPhone("0987654321");
        companyQuery2.setAlias("DiffAlias");
        companyQuery2.setGiro("DiffGiro");
        companyQuery2.setEmail("diff@example.com");
        companyQuery2.setGeolocation(false);
        companyQuery2.setSelfie(true);
        companyQuery2.setLag(false);

        assertNotEquals(companyQuery1, companyQuery2);
    }
}
