package assistcontrol.api;

import cl.bennu.assistcontrol.api.CommuneResource;
import cl.bennu.assistcontrol.domain.Commune;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommuneResourceTest {

    @Mock
    private AssistControlService assistControlService;

    @InjectMocks
    private CommuneResource communeResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCommunesReturnsEmptyList() throws Exception {
        when(assistControlService.getAllCommune(anyString())).thenReturn(Collections.emptyList());

        Response response = communeResource.getAll("token");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(Collections.emptyList(), response.getEntity());
    }

    @Test
    void getCommuneByIdWithInvalidTokenThrowsException() throws Exception {
        when(assistControlService.getCommuneById(anyString(), anyLong())).thenThrow(new RuntimeException("Invalid token"));

        assertThrows(RuntimeException.class, () -> communeResource.get("invalidToken", 1L));
    }

    @Test
    void findCommunesByQueryWithNoResultsReturnsEmptyList() throws Exception {
        when(assistControlService.findCommuneByQuery(anyString(), any(CommuneQuery.class))).thenReturn(Collections.emptyList());

        Response response = communeResource.find("token", 1L, "siiCode", "tgrCode");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(Collections.emptyList(), response.getEntity());
    }

    @Test
    void insertCommuneWithInvalidTokenThrowsException() throws Exception {
        doThrow(new RuntimeException("Invalid token")).when(assistControlService).saveCommune(anyString(), any(Commune.class), anyString());

        assertThrows(RuntimeException.class, () -> communeResource.insert("invalidToken", new Commune()));
    }
}