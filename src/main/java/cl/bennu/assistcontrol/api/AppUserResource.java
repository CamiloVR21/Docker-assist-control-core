package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.AppUser;
import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.query.AppUserQuery;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import io.vertx.ext.web.FileUpload;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import org.jboss.resteasy.reactive.PartType;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class AppUserResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

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
                         @QueryParam("company") Long companyId,
                         @QueryParam("name") String name) {
        AppUserQuery query = new AppUserQuery();

        if (companyId != null) {
            CompanyQuery companyQuery = new CompanyQuery();
            companyQuery.setId(companyId);
            query.setCompanyId(companyQuery);
        }

        query.setName(name);

        List<AppUser> appUsers = assistControlService.findAppUserByQuery(token, query);
        return Response.ok(appUsers).build();
    }

    @SneakyThrows
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @POST
    public Response insert(@HeaderParam("Authorization") String token,
                           @FormParam("name") String name,
                           @FormParam("email") String email,
                           @FormParam("password") String password,
                           @FormParam("companyId") Long companyId,
                           @FormParam("img") @PartType(MediaType.APPLICATION_OCTET_STREAM) InputStream imgStream) {

        if (name == null || password == null || companyId == null) {
            throw new NoDataException("Faltan datos requeridos.");
        }

        AppUser appUser = new AppUser();
        appUser.setName(name);
        appUser.setEmail(email);
        appUser.setPassword(password);

        Company company = new Company();
        company.setId(companyId);
        appUser.setCompany(company);

        if (imgStream != null) {
            byte[] imageBytes = imgStream.readAllBytes();
            appUser.setImg(imageBytes);
        }

        assistControlService.saveAppUser(token, appUser, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(appUser).build();
    }

    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, AppUser appUser) {
        if (appUser == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del usuario");
        }
        if (appUser.getId() == null) {
            throw new NoDataException("El id del usuario es requerido para una actualización");
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
