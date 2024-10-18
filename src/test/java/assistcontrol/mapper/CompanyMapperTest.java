package assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.mapper.CompanyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CompanyMapperTest {

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private CompanyMapperTest companyMapperTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByQueryReturnsEmptyList() {
        CompanyQuery query = new CompanyQuery();
        when(companyMapper.findByQuery(query)).thenReturn(Collections.emptyList());

        List<Company> result = companyMapper.findByQuery(query);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void getByQueryReturnsCompany() {
        CompanyQuery query = new CompanyQuery();
        Company company = new Company();
        when(companyMapper.getByQuery(query)).thenReturn(company);

        Company result = companyMapper.getByQuery(query);

        assertEquals(company, result);
    }

    @Test
    void findByQueryReturnsCompanyList() {
        CompanyQuery query = new CompanyQuery();
        Company company = new Company();
        List<Company> companyList = List.of(company);
        when(companyMapper.findByQuery(query)).thenReturn(companyList);

        List<Company> result = companyMapper.findByQuery(query);

        assertEquals(companyList, result);
    }
}