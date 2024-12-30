package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.query.BranchQuery;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import java.util.List;

@Path("/branch")
@Produces(MediaType.APPLICATION_JSON)
public class BranchResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Branch> branchies = assistControlService.getAllBranch(token);
        return Response.ok(branchies).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Branch branchRequest = new Branch();
        branchRequest.setId(id);

        Branch branch = assistControlService.getBranchById(token, branchRequest);

        return Response.ok(branch).build();
    }

    @SneakyThrows
    @GET
    @Path("/-")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("company") Long companyId,
                         @QueryParam("name") String name,
                         @QueryParam("address") String address,
                         @QueryParam("active") Boolean active) {
        BranchQuery query = new BranchQuery();

        if (companyId != null) {
            CompanyQuery companyQuery = new CompanyQuery();
            companyQuery.setId(companyId);
            query.setCompanyId(companyQuery);
        }

        query.setName(name);
        query.setAddress(address);
        query.setActive(active);

        List<Branch> branches = assistControlService.findBranchByQuery(token, query);
        return Response.ok(branches).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Branch branch) {
        assistControlService.saveBranch(token, branch, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(branch).build();
    }

    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, Branch branch) {
        if (branch == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información de la sucursal");
        }
        if (branch.getId() == null) {
            throw new NoDataException("El id de la sucursal es requerido para una actualización");
        }
        assistControlService.saveBranch(token, branch, HttpMethod.PUT);
        return Response.ok(branch).build();
    }

    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Branch branch = assistControlService.deleteBranchById(token, id);
        return Response.ok(branch).build();
    }
}
