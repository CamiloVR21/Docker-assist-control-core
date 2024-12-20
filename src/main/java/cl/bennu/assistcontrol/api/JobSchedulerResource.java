package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.JobScheduler;
import cl.bennu.assistcontrol.domain.query.JobSchedulerQuery;
import cl.bennu.assistcontrol.enums.JobTypeEnum;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.util.List;

@Path("/job-scheduler")
@Produces(MediaType.APPLICATION_JSON)
public class JobSchedulerResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<JobScheduler> jobSchedulers = assistControlService.getAllJobScheduler(token);
        return Response.ok(jobSchedulers).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        JobScheduler jobScheduler = new JobScheduler();
        jobScheduler.setId(id);
        JobScheduler result = assistControlService.getJobSchedulerById(token, jobScheduler);
        return Response.ok(result).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("name") String name,
                         @QueryParam("monday") Boolean monday,
                         @QueryParam("tuesday") Boolean tuesday) {
        JobSchedulerQuery query = new JobSchedulerQuery();
        query.setName(name);
        query.setMonday(monday);
        query.setTuesday(tuesday);

        List<JobScheduler> jobSchedulers = assistControlService.findJobSchedulerByQuery(token, query);
        return Response.ok(jobSchedulers).build();
    }


    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, JobScheduler request) {
        assistControlService.saveJobScheduler(token, request, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(request).build();
    }

    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, JobScheduler jobScheduler) {
        if (jobScheduler == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del programador de trabajos");
        }
        if (jobScheduler.getId() == null) {
            throw new NoDataException("El id del JobScheduler es requerido para una actualización");
        }
        assistControlService.saveJobScheduler(token, jobScheduler, HttpMethod.PUT);
        return Response.ok().build();
    }


    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        JobScheduler jobScheduler = assistControlService.deleteJobSchedulerById(token, id);
        return Response.ok(jobScheduler).build();
    }
}
