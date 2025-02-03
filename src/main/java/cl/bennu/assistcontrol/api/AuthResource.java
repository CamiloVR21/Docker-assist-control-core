package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.domain.AppUser;
import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.Employee;
import cl.bennu.assistcontrol.domain.JobType;
import cl.bennu.assistcontrol.domain.Credential;
import cl.bennu.assistcontrol.domain.Dashboard;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private AssistControlService assistControlService;

    @POST
    @Path("/login")
    public Response login(Credential credentials) {
        AppUser appUser = assistControlService.login(credentials.getEmail(), credentials.getPassword());
        if (appUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Credenciales inválidas")
                    .build();
        }
        return Response.ok(appUser).build();
    }

    @GET
    @Path("/dashboard")
    public Response getDashboard(@QueryParam("userId") Long userId) {
        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El parámetro userId es requerido")
                    .build();
        }

        AppUser userQuery = new AppUser();
        userQuery.setId(userId);
        AppUser appUser;
        try {
            appUser = assistControlService.getAppUserById("", userQuery);
        } catch (NoDataException ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuario no encontrado: " + ex.getMessage())
                    .build();
        }

        Long companyId = appUser.getCompany().getId();

        try {
            List<Branch> branches = assistControlService.getBranchesByCompany(companyId);
            List<Employee> employees = assistControlService.findEmployeesByCompany("", companyId);
            List<JobType> roles = assistControlService.getJobTypesByCompany(companyId);

            Dashboard dashboard = new Dashboard(branches, employees, roles);
            return Response.ok(dashboard).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el dashboard: " + e.getMessage())
                    .build();
        }
    }
}
