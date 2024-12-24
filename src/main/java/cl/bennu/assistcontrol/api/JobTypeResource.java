package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.JobType;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.domain.query.JobTypeQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.util.List;

@Path("/job-type")
@Produces(MediaType.APPLICATION_JSON)
public class JobTypeResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<JobType> jobTypes = assistControlService.getAllJobType(token);
        return Response.ok(jobTypes).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        JobType jobType = new JobType();
        jobType.setId(id);
        JobType result = assistControlService.getJobTypeById(token, jobType);
        return Response.ok(result).build();
    }


    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("company") Long companyId,
                         @QueryParam("name") String name) {
        JobTypeQuery query = new JobTypeQuery();

        if (companyId != null) {
            CompanyQuery companyQuery = new CompanyQuery();
            companyQuery.setId(companyId);
            query.setCompany(companyQuery);
        }
        query.setName(name);
        List<JobType> jobTypes = assistControlService.findJobTypeByQuery(token, query);
        return Response.ok(jobTypes).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, JobType jobType) {
        assistControlService.saveJobType(token, jobType, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(jobType).build();
    }

    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, JobType jobType) {
        if (jobType == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del rol");
        }
        if (jobType.getId() == null) {
            throw new NoDataException("El id del rol es requerido para una actualización");
        }
        assistControlService.saveJobType(token, jobType, HttpMethod.PUT);
        return Response.ok(jobType).build();
    }



    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        JobType jobType = assistControlService.deleteJobTypeById(token, id);
        return Response.ok(jobType).build();
    }
}