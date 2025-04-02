package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.request.SaveCompanyRequest;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
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
        Company company = new Company();
        company.setId(id);
        Company result = assistControlService.getCompanyById(token, company);
        return Response.ok(result).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("commune-id") Long communeId,
                         @QueryParam("code") String code,
                         @QueryParam("name") String name,
                         @QueryParam("address") String address) {
        CompanyQuery query = new CompanyQuery();

        if (communeId != null) {
            CommuneQuery communeQuery = new CommuneQuery();
            communeQuery.setId(communeId);
            query.setCommune(communeQuery);
        }

        query.setCode(code);
        query.setName(name);
        query.setAddress(address);

        List<Company> companies = assistControlService.findCompanyByQuery(token, query);
        return Response.ok(companies).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-employee/{employeeId}")
    public Response getByEmployeeId(@HeaderParam("Authorization") String token, @PathParam("employeeId") Long employeeId) {
        List<Company> companies = assistControlService.getCompanyByEmployeeId(token, employeeId);
        return Response.ok(companies).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-app-user/{appUserId}")
    public Response getByAppUserId(@HeaderParam("Authorization") String token, @PathParam("appUserId") Long appUserId) {
        List<Company> companies = assistControlService.getCompanyByAppUser(token, appUserId);
        return Response.ok(companies).build();
    }



    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, SaveCompanyRequest request) {
        assistControlService.saveCompany(token, request, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(request.getCompany()).build();
    }

    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, SaveCompanyRequest saveCompanyRequest) {
        Company company = saveCompanyRequest.getCompany();
        if (company == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información de la compañía");
        }
        assistControlService.saveCompany(token, saveCompanyRequest, HttpMethod.PUT);
        return Response.ok(saveCompanyRequest).build();
    }

    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Company company = assistControlService.deleteCompanyById(token, id);
        return Response.ok(company).build();
    }
}