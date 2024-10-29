package assistcontrol.api;

import cl.bennu.assistcontrol.api.BranchResource;
import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.query.BranchQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BranchResourceTest {

    @Mock
    private AssistControlService assistControlService;

    @InjectMocks
    private BranchResource branchResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBranchesReturnsListOfBranches() throws Exception {
        Branch branch = new Branch();
        when(assistControlService.getAllBranch(anyString())).thenReturn(List.of(branch));

        Response response = branchResource.getAll("token");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(List.of(branch), response.getEntity());
    }

    @Test
    void getBranchByIdReturnsBranch() throws Exception {
        Branch branch = new Branch();
        branch.setId(1L);

        when(assistControlService.getBranchById(anyString(), any(Branch.class))).thenReturn(branch);
        Response response = branchResource.get("token", 1L);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(branch, response.getEntity());
    }


    @Test
    void findBranchesByQueryReturnsListOfBranches() throws Exception {
        Branch branch = new Branch();
        when(assistControlService.findBranchByQuery(anyString(), any(BranchQuery.class))).thenReturn(List.of(branch));

        Response response = branchResource.find("token", 1L, "name", "address", true);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(List.of(branch), response.getEntity());
    }

    @Test
    void insertBranchCreatesNewBranch() throws Exception {
        Branch branch = new Branch();
        Response response = branchResource.insert("token", branch);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(branch, response.getEntity());
        verify(assistControlService).saveBranch(anyString(), any(Branch.class), eq(HttpMethod.POST));
    }

    @Test
    void updateBranchUpdatesExistingBranch() throws Exception {
        Branch branch = new Branch();
        Response response = branchResource.update("token", 1L, branch);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(branch, response.getEntity());
        verify(assistControlService).saveBranch(anyString(), any(Branch.class), eq(HttpMethod.PUT));
    }

    @Test
    void deleteBranchDeletesBranch() throws Exception {
        Branch branch = new Branch();
        when(assistControlService.deleteBranchById(anyString(), anyLong())).thenReturn(branch);

        Response response = branchResource.delete("token", 1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(branch, response.getEntity());
        verify(assistControlService).deleteBranchById(anyString(), anyLong());
    }

    @Test
    void getBranchByIdThrowsExceptionForInvalidId() throws Exception {
        Branch branch = new Branch();
        branch.setId(99L);

        when(assistControlService.getBranchById(anyString(), any(Branch.class)))
                .thenThrow(new NoDataException("No se encontró la sucursal con el ID especificado"));
        assertThrows(NoDataException.class, () -> branchResource.get("token", 99L));
    }


    @Test
    void insertBranchThrowsExceptionForInvalidToken() throws Exception {
        doThrow(new RuntimeException("Invalid token")).when(assistControlService).saveBranch(anyString(), any(Branch.class), anyString());

        assertThrows(RuntimeException.class, () -> branchResource.insert("invalidToken", new Branch()));
    }
}