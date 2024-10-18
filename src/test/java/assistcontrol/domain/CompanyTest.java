package assistcontrol.domain;

import cl.bennu.assistcontrol.domain.Company;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    @Test
    void companyInitialization() {
        Company company = new Company();
        assertNotNull(company);
    }

    @Test
    void companyFieldsAreSetCorrectly() {
        Company company = new Company();
        company.setCommuneId(1L);
        company.setCode("COMP123");
        company.setName("Test Company");
        company.setAddress("123 Test St");
        company.setPhone("1234567890");
        company.setAlias("TestAlias");
        company.setGiro("TestGiro");
        company.setEmail("test@example.com");
        company.setGeolocation(true);
        company.setSelfie(false);
        company.setLag(true);

        assertEquals(1L, company.getCommuneId());
        assertEquals("COMP123", company.getCode());
        assertEquals("Test Company", company.getName());
        assertEquals("123 Test St", company.getAddress());
        assertEquals("1234567890", company.getPhone());
        assertEquals("TestAlias", company.getAlias());
        assertEquals("TestGiro", company.getGiro());
        assertEquals("test@example.com", company.getEmail());
        assertTrue(company.getGeolocation());
        assertFalse(company.getSelfie());
        assertTrue(company.getLag());
    }

    @Test
    void companyHandlesNullValues() {
        Company company = new Company();
        company.setCode(null);
        company.setName(null);
        company.setAddress(null);
        company.setPhone(null);
        company.setAlias(null);
        company.setGiro(null);
        company.setEmail(null);

        assertNull(company.getCode());
        assertNull(company.getName());
        assertNull(company.getAddress());
        assertNull(company.getPhone());
        assertNull(company.getAlias());
        assertNull(company.getGiro());
        assertNull(company.getEmail());
    }

    @Test
    void companyEquality() {
        Company company1 = new Company();
        company1.setCommuneId(1L);
        company1.setCode("COMP123");
        company1.setName("Test Company");
        company1.setAddress("123 Test St");
        company1.setPhone("1234567890");
        company1.setAlias("TestAlias");
        company1.setGiro("TestGiro");
        company1.setEmail("test@example.com");
        company1.setGeolocation(true);
        company1.setSelfie(false);
        company1.setLag(true);

        Company company2 = new Company();
        company2.setCommuneId(1L);
        company2.setCode("COMP123");
        company2.setName("Test Company");
        company2.setAddress("123 Test St");
        company2.setPhone("1234567890");
        company2.setAlias("TestAlias");
        company2.setGiro("TestGiro");
        company2.setEmail("test@example.com");
        company2.setGeolocation(true);
        company2.setSelfie(false);
        company2.setLag(true);

        assertEquals(company1, company2);
    }

    @Test
    void companyInequality() {
        Company company1 = new Company();
        company1.setCommuneId(1L);
        company1.setCode("COMP123");
        company1.setName("Test Company");
        company1.setAddress("123 Test St");
        company1.setPhone("1234567890");
        company1.setAlias("TestAlias");
        company1.setGiro("TestGiro");
        company1.setEmail("test@example.com");
        company1.setGeolocation(true);
        company1.setSelfie(false);
        company1.setLag(true);

        Company company2 = new Company();
        company2.setCommuneId(2L);
        company2.setCode("DIFF456");
        company2.setName("Different Company");
        company2.setAddress("456 Different St");
        company2.setPhone("0987654321");
        company2.setAlias("DiffAlias");
        company2.setGiro("DiffGiro");
        company2.setEmail("diff@example.com");
        company2.setGeolocation(false);
        company2.setSelfie(true);
        company2.setLag(false);

        assertNotEquals(company1, company2);
    }
}