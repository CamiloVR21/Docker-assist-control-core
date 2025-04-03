package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.JobScheduler;
import cl.bennu.assistcontrol.domain.query.JobSchedulerQuery;
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

    /**
     * Función: getAll
     * Descripción: Retorna una lista de todos los programadores de trabajos (JobSchedulers) registrados en la aplicación.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @return Response con la lista de JobSchedulers.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<JobScheduler> jobSchedulers = assistControlService.getAllJobScheduler(token);
        return Response.ok(jobSchedulers).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información de un JobScheduler específico identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del JobScheduler.
     * @return Response con el JobScheduler encontrado.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        JobScheduler jobScheduler = new JobScheduler();
        jobScheduler.setId(id);
        JobScheduler result = assistControlService.getJobSchedulerById(token, jobScheduler);
        return Response.ok(result).build();
    }

    /**
     * Función: find
     * Descripción: Busca JobSchedulers basándose en parámetros de consulta: nombre, lunes y martes.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param name Nombre del JobScheduler.
     * @param monday Valor booleano para filtrar por el día lunes.
     * @param tuesday Valor booleano para filtrar por el día martes.
     * @return Response con la lista de JobSchedulers que cumplen con los criterios de búsqueda.
     */
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

    /**
     * Función: getByCompanyId
     * Descripción: Retorna una lista de JobSchedulers asociados a una compañía específica.
     * Requiere el id de la compañía como parámetro en la URL.
     *
     * @param companyId Identificador de la compañía.
     * @return Response con la lista de JobSchedulers asociados a la compañía.
     * @throws NoDataException si el companyId es nulo.
     */
    @SneakyThrows
    @GET
    @Path("/by-company/{companyId}")
    public Response getByCompanyId(@PathParam("companyId") Long companyId) {
        if (companyId == null) {
            throw new NoDataException("El ID de la compañía es requerido.");
        }
        List<JobScheduler> jobSchedulers = assistControlService.findJobSchedulersByCompanyId(companyId);
        return Response.ok(jobSchedulers).build();
    }

    /**
     * Función: getByBranchId
     * Descripción: Retorna una lista de JobSchedulers asociados a una sucursal específica.
     * Requiere el id de la sucursal como parámetro en la URL.
     *
     * @param branchId Identificador de la sucursal.
     * @return Response con la lista de JobSchedulers asociados a la sucursal.
     * @throws NoDataException si el branchId es nulo.
     */
    @SneakyThrows
    @GET
    @Path("/by-branch/{branchId}")
    public Response getByBranchId(@PathParam("branchId") Long branchId) {
        if (branchId == null) {
            throw new NoDataException("El ID de la sucursal es requerido.");
        }
        List<JobScheduler> jobSchedulers = assistControlService.findJobSchedulersByBranchId(branchId);
        return Response.ok(jobSchedulers).build();
    }

    /**
     * Función: insert
     * Descripción: Inserta un nuevo JobScheduler en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto JobScheduler en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param request Objeto JobScheduler a insertar.
     * @return Response con el JobScheduler insertado y el código de estado CREATED.
     */
    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, JobScheduler request) {
        assistControlService.saveJobScheduler(token, request, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(request).build();
    }

    /**
     * Función: update
     * Descripción: Actualiza la información de un JobScheduler existente.
     * Verifica que el objeto JobScheduler no sea nulo y que contenga un id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param jobScheduler Objeto JobScheduler con la información a actualizar.
     * @return Response indicando la actualización exitosa.
     * @throws NoDataException si el objeto JobScheduler es nulo o no contiene un id.
     */
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

    /**
     * Función: delete
     * Descripción: Elimina un JobScheduler identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del JobScheduler a eliminar.
     * @return Response con el JobScheduler eliminado.
     */
    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        JobScheduler jobScheduler = assistControlService.deleteJobSchedulerById(token, id);
        return Response.ok(jobScheduler).build();
    }
}
