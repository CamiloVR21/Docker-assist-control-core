package assistcontrol.api;

import cl.bennu.assistcontrol.api.CompanyResource;
import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.request.SaveCompanyRequest;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CompanyResourceTest {

    @Mock
    private AssistControlService assistControlService;

    @InjectMocks
    private CompanyResource companyResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCompaniesReturnsEmptyList() throws Exception {
        when(assistControlService.getAllCompany(anyString())).thenReturn(Collections.emptyList());

        Response response = companyResource.getAll("token");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(Collections.emptyList(), response.getEntity());
    }

    @Test
    void getCompanyByIdWithInvalidTokenThrowsException() throws Exception {
        Company company = new Company();
        company.setId(1L);

        when(assistControlService.getCompanyById(anyString(), any(Company.class)))
                .thenThrow(new RuntimeException("Invalid token"));

        assertThrows(RuntimeException.class, () -> companyResource.get("invalidToken", company.getId()));
    }



    @Test
    void findCompaniesByQueryWithNoResultsReturnsEmptyList() throws Exception {
        when(assistControlService.findCompanyByQuery(anyString(), any(CompanyQuery.class))).thenReturn(Collections.emptyList());

        Response response = companyResource.find("token", 1L, "code", "name", "address");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(Collections.emptyList(), response.getEntity());
    }

    @Test
    void insertCompanyWithInvalidTokenThrowsException() throws Exception {
        SaveCompanyRequest saveCompanyRequest = new SaveCompanyRequest();
        doThrow(new RuntimeException("Invalid token")).when(assistControlService).saveCompany(anyString(), any(SaveCompanyRequest.class), anyString());

        assertThrows(RuntimeException.class, () -> companyResource.insert("invalidToken", saveCompanyRequest));
    }

}