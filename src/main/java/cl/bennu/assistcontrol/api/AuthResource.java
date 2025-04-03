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

    /**
     * Función: login
     * Descripción: Autentica al usuario a partir de sus credenciales (código y contraseña).
     * Si la autenticación es exitosa, retorna el objeto AppUser correspondiente.
     * En caso de fallar (por ejemplo, credenciales inválidas), retorna una respuesta con estado UNAUTHORIZED.
     *
     * @param loginUser Objeto AppUser que contiene el código y la contraseña para iniciar sesión.
     * @return Response con el usuario autenticado o un mensaje de error.
     */
    @POST
    @Path("/login")
    public Response login(AppUser loginUser) {
        try {
            AppUser appUser = assistControlService.login(loginUser.getCode(), loginUser.getPassword());
            return Response.ok(appUser).build();
        } catch (NoDataException ex) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ex.getMessage())
                    .build();
        }
    }

    /**
     * Función: getDashboard
     * Descripción: Obtiene la información del dashboard para un usuario específico.
     * Requiere el parámetro de consulta "userId" para identificar al usuario.
     * Se busca el usuario y, a partir de este, se obtienen las sucursales, empleados y tipos de trabajo asociados a la empresa.
     * Retorna un objeto Dashboard que consolida estos datos.
     * Si falta el parámetro o no se encuentra el usuario, se retorna un error con el código correspondiente.
     *
     * @param userId Identificador del usuario cuyo dashboard se desea obtener.
     * @return Response con el dashboard o un mensaje de error según el caso.
     */
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

        Long companyId = appUser.getEmployee().getId();

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
