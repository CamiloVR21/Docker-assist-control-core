package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.AppUser;
import cl.bennu.assistcontrol.domain.Employee;
import cl.bennu.assistcontrol.domain.query.AppUserQuery;
import cl.bennu.assistcontrol.domain.query.EmployeeQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.util.Base64;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class AppUserResource extends BaseResource {

    @Inject
    private AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<AppUser> appUsers = assistControlService.getAllAppUser(token);
        return Response.ok(appUsers).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        AppUser appUserRequest = new AppUser();
        appUserRequest.setId(id);
        AppUser appUser = assistControlService.getAppUserById(token, appUserRequest);
        return Response.ok(appUser).build();
    }


    @SneakyThrows
    @GET
    @Path("/-")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("employeeId") Long employeeId,
                         @QueryParam("name") String name) {
        AppUserQuery query = new AppUserQuery();
        if (employeeId != null) {
            EmployeeQuery eq = new EmployeeQuery();
            eq.setId(employeeId);
            query.setEmployeeId(eq);
        }
        query.setName(name);
        List<AppUser> appUsers = assistControlService.findAppUserByQuery(token, query);
        return Response.ok(appUsers).build();
    }


    @SneakyThrows
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insert(@HeaderParam("Authorization") String token, AppUser appUser) {
        if (appUser.getName() == null || appUser.getPassword() == null || appUser.getEmployee() == null) {
            throw new NoDataException("Faltan datos requeridos (nombre, contraseña o empleado).");
        }
        if (appUser.getBase64Img() != null) {
            byte[] imageBytes = Base64.getDecoder().decode(appUser.getBase64Img());
            appUser.setImg(imageBytes);
        }
        assistControlService.saveAppUser(token, appUser, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(appUser).build();
    }

    @SneakyThrows
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@HeaderParam("Authorization") String token, AppUser appUser) {
        if (appUser == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del usuario.");
        }
        if (appUser.getId() == null) {
            throw new NoDataException("El id del usuario es requerido para una actualización.");
        }
        assistControlService.saveAppUser(token, appUser, HttpMethod.PUT);
        return Response.ok(appUser).build();
    }

    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        AppUser appUser = assistControlService.deleteAppUserById(token, id);
        return Response.ok(appUser).build();
    }
}
