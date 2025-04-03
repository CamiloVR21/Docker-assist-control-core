package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
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

    /**
     * Función: getAll
     * Descripción: Retorna la lista completa de roles (JobTypes) registrados.
     * Requiere un token de autorización en la cabecera para validar el acceso.
     *
     * @param token Token de autorización.
     * @return Response con la lista de JobTypes.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<JobType> jobTypes = assistControlService.getAllJobType(token);
        return Response.ok(jobTypes).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información detallada de un rol específico, identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del rol (JobType).
     * @return Response con la información del rol.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        JobType jobType = new JobType();
        jobType.setId(id);
        JobType result = assistControlService.getJobTypeById(token, jobType);
        return Response.ok(result).build();
    }

    /**
     * Función: find
     * Descripción: Busca roles (JobTypes) según parámetros de consulta.
     * Se pueden filtrar por el identificador de la compañía y/o por el nombre del rol.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param companyId Identificador de la compañía a la que pertenece el rol.
     * @param name Nombre del rol.
     * @return Response con la lista de roles que cumplen los criterios de búsqueda.
     */
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

    /**
     * Función: insert
     * Descripción: Inserta un nuevo rol (JobType) en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto JobType en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param jobType Objeto JobType a insertar.
     * @return Response con el rol insertado y el código de estado CREATED.
     */
    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, JobType jobType) {
        assistControlService.saveJobType(token, jobType, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(jobType).build();
    }

    /**
     * Función: update
     * Descripción: Actualiza la información de un rol (JobType) existente.
     * Verifica que se haya proporcionado un objeto JobType y que contenga un id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param jobType Objeto JobType con la información actualizada.
     * @return Response con el rol actualizado.
     * @throws NoDataException si el objeto JobType es nulo o no contiene un id.
     */
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

    /**
     * Función: delete
     * Descripción: Elimina un rol (JobType) identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del rol a eliminar.
     * @return Response con el rol eliminado.
     */
    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        JobType jobType = assistControlService.deleteJobTypeById(token, id);
        return Response.ok(jobType).build();
    }
}
