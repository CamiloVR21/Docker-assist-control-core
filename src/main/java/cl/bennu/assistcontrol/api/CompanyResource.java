package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.util.List;

@Path("/company")
@Produces(MediaType.APPLICATION_JSON)
public class CompanyResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Company> companies = assistControlService.getAllCompany(token);
        return Response.ok(companies).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Company company = assistControlService.getCompanyById(token, id);
        return Response.ok(company).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token
            , @QueryParam("commune-id") Long communeId
            , @QueryParam("code") String code
            , @QueryParam("name") String name
            , @QueryParam("address") String address) {
        CompanyQuery query = new CompanyQuery();
        query.setCommuneId(communeId);
        query.setCode(code);
        query.setName(name);
        query.setAddress(address);

        List<Company> companies = assistControlService.findCompanyByQuery(token, query);
        return Response.ok(companies).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Company company) {
        assistControlService.saveCompany(token, company, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(company).build();
    }

    @SneakyThrows
    @PUT
    @Path("/{id}")
    public Response update(@HeaderParam("Authorization") String token, @PathParam("id") Long id, Company company) {
        company.setId(id);
        assistControlService.saveCompany(token, company, HttpMethod.PUT);
        return Response.ok(company).build();
    }

    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Company company = assistControlService.deleteCompanyById(token, id);
        return Response.ok(company).build();
    }
}
