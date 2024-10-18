package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.query.BranchQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
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
        Branch branch = assistControlService.getBranchyById(token, id);
        return Response.ok(branch).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token
            , @QueryParam("company-id") Long companyId
            , @QueryParam("name") String name
            , @QueryParam("address") String address
            , @QueryParam("active") Boolean active) {
        BranchQuery query = new BranchQuery();
        query.setCompanyId(companyId);
        query.setName(name);
        query.setAddress(address);
        query.setActive(active);;

        List<Branch> branchies = assistControlService.findBranchByQuery(token, query);
        return Response.ok(branchies).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Branch branch) {
        assistControlService.saveBranch(token, branch, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(branch).build();
    }

    @SneakyThrows
    @PUT
    @Path("/{id}")
    public Response update(@HeaderParam("Authorization") String token, @PathParam("id") Long id, Branch branch) {
        branch.setId(id);
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
