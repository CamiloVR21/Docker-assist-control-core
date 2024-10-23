package assistcontrol.service;

import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.Commune;
import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.mapper.CommuneMapper;
import cl.bennu.assistcontrol.mapper.CompanyMapper;
import cl.bennu.assistcontrol.request.SaveCompanyRequest;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.ws.rs.HttpMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssistControlServiceTest {

    @Mock
    private CommuneMapper communeMapper;

    @Mock
    private CompanyMapper companyMapper;

    @InjectMocks
    private AssistControlService assistControlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCommuneByIdReturnsCommune() {
        Commune commune = new Commune();
        when(communeMapper.get(1L)).thenReturn(commune);

        Commune result = assistControlService.getCommuneById("token", 1L);
        assertEquals(commune, result);
    }

    @Test
    void getCommuneByIdReturnsNullForInvalidId() {
        when(communeMapper.get(99L)).thenReturn(null);

        Commune result = assistControlService.getCommuneById("token", 99L);
        assertNull(result);
    }

    @Test
    void getAllCommuneReturnsListOfCommunes() {
        Commune commune = new Commune();
        when(communeMapper.getAll()).thenReturn(List.of(commune));

        List<Commune> result = assistControlService.getAllCommune("token");
        assertEquals(1, result.size());
        assertEquals(commune, result.get(0));
    }

    @Test
    void findCommuneByQueryReturnsListOfCommunes() {
        CommuneQuery query = new CommuneQuery();
        Commune commune = new Commune();
        when(communeMapper.findByQuery(query)).thenReturn(List.of(commune));

        List<Commune> result = assistControlService.findCommuneByQuery("token", query);
        assertEquals(1, result.size());
        assertEquals(commune, result.get(0));
    }

    @Test
    void saveCommuneInsertsNewCommune() throws NoDataException, UniqueException {
        Commune commune = new Commune();
        commune.setName("Test");
        commune.setCityId(1L);

        assistControlService.saveCommune("token", commune, HttpMethod.POST);
        verify(communeMapper).insert(commune);
    }

    @Test
    void saveCommuneUpdatesExistingCommune() throws NoDataException, UniqueException {
        Commune commune = new Commune();
        commune.setId(1L);
        commune.setName("Test");
        commune.setCityId(1L);

        assistControlService.saveCommune("token", commune, HttpMethod.PUT);
        verify(communeMapper).update(commune);
    }

    @Test
    void deleteCommuneDeletesCommune() {
        assistControlService.deleteCommune("token", 1L);
        verify(communeMapper).delete(1L);
    }

    @Test
    void getCompanyByIdReturnsCompany() {
        Company company = new Company();
        when(companyMapper.get(1L)).thenReturn(company);

        Company result = assistControlService.getCompanyById("token", 1L);
        assertEquals(company, result);
    }

    @Test
    void getAllCompanyReturnsListOfCompanies() {
        Company company = new Company();
        when(companyMapper.getAll()).thenReturn(List.of(company));

        List<Company> result = assistControlService.getAllCompany("token");
        assertEquals(1, result.size());
        assertEquals(company, result.get(0));
    }

    @Test
    void findCompanyByQueryReturnsListOfCompanies() {
        CompanyQuery query = new CompanyQuery();
        Company company = new Company();
        when(companyMapper.findByQuery(query)).thenReturn(List.of(company));

        List<Company> result = assistControlService.findCompanyByQuery("token", query);
        assertEquals(1, result.size());
        assertEquals(company, result.get(0));
    }

    @Test
    void saveCompanyInsertsNewCompany() throws NoDataException, UniqueException {
        SaveCompanyRequest saveCompanyRequest = new SaveCompanyRequest();

        Company company = new Company();
        company.setCode("COMP123");
        company.setName("Test Company");
        company.setAddress("123 Test St");
        company.setCommuneId(1L);
        saveCompanyRequest.setCompany(company);

        Branch branch = new Branch();
        saveCompanyRequest.setBranch(branch);

        assistControlService.saveCompany("token", saveCompanyRequest, HttpMethod.POST);

        verify(companyMapper).insert(company);
    }

    @Test
    void saveCompanyUpdatesExistingCompany() throws NoDataException, UniqueException {
        SaveCompanyRequest saveCompanyRequest = new SaveCompanyRequest();

        Company company = new Company();
        company.setId(1L);
        company.setCode("COMP123");
        company.setName("Test Company");
        company.setAddress("123 Test St");
        company.setCommuneId(1L);
        saveCompanyRequest.setCompany(company);

        Branch branch = new Branch();
        saveCompanyRequest.setBranch(branch);

        assistControlService.saveCompany("token", saveCompanyRequest, HttpMethod.PUT);

        verify(companyMapper).update(company);
    }


    @Test
    void deleteCompanyByIdDeletesCompany() throws NoDataException {
        Company company = new Company();
        when(companyMapper.get(1L)).thenReturn(company);

        Company result = assistControlService.deleteCompanyById("token", 1L);
        verify(companyMapper).delete(1L);
        assertEquals(company, result);
    }
}