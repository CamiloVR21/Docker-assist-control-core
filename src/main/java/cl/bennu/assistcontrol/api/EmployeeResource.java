package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Employee;
import cl.bennu.assistcontrol.domain.query.EmployeeQuery;
import cl.bennu.assistcontrol.domain.query.JobTypeQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.utils.TokenUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import java.util.List;

@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource extends BaseResource {

    private @Inject AssistControlService assistControlService;


    private String extractEmployeeUUID(String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring("Bearer ".length());
            }
            token = token.trim();

            DecodedJWT decodedJWT = JWT.decode(token);
            String uuid = decodedJWT.getClaim("uuid").asString();
            if (uuid == null || uuid.isEmpty()) {
                throw new IllegalArgumentException("El token no contiene el claim 'uuid'");
            }
            return uuid;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error extrayendo el UUID del token", e);
        }
    }


    /**
     * Función: getAll
     * Descripción: Retorna la lista completa de empleados registrados en la aplicación.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @return Response con la lista de empleados.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        String employeeUUID = extractEmployeeUUID(token);
        List<Employee> employees = assistControlService.getAllEmployees(employeeUUID);
        return Response.ok(employees).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información detallada de un empleado específico identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del empleado.
     * @return Response con los detalles del empleado.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        String employeeUUID = extractEmployeeUUID(token);
        Employee employee = assistControlService.getEmployeeById(employeeUUID, id);
        return Response.ok(employee).build();
    }

    /**
     * Función: findByCompany
     * Descripción: Retorna la lista de empleados asociados a una compañía específica.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param companyId Identificador de la compañía.
     * @return Response con la lista de empleados vinculados a la compañía.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-company/{companyId}")
    public Response findByCompany(@HeaderParam("Authorization") String token,
                                  @PathParam("companyId") Long companyId) {
        String employeeUUID = extractEmployeeUUID(token);
        List<Employee> employees = assistControlService.findEmployeesByCompany(employeeUUID, companyId);
        return Response.ok(employees).build();
    }

    /**
     * Función: find
     * Descripción: Busca empleados basándose en varios parámetros de consulta:
     * nombre, id del tipo de trabajo, apellido, dirección y estado de actividad.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param name Nombre del empleado.
     * @param jobTypeId Identificador del tipo de trabajo.
     * @param lastName Apellido del empleado.
     * @param address Dirección del empleado.
     * @param active Estado de actividad del empleado.
     * @return Response con la lista de empleados que cumplen con los criterios de búsqueda.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("name") String name,
                         @QueryParam("job-type") Long jobTypeId,
                         @QueryParam("lastName") String lastName,
                         @QueryParam("address") String address,
                         @QueryParam("active") Boolean active) {
        String employeeUUID = extractEmployeeUUID(token);
        EmployeeQuery query = new EmployeeQuery();
        if (jobTypeId != null) {
            JobTypeQuery jobTypeQuery = new JobTypeQuery();
            jobTypeQuery.setId(jobTypeId);
            query.setJobType(jobTypeQuery);
        }
        query.setName(name);
        query.setLastName(lastName);
        query.setAddress(address);
        query.setActive(active);

        List<Employee> employees = assistControlService.findEmployeesByQuery(employeeUUID, query);
        return Response.ok(employees).build();
    }

    /**
     * Función: searchEmployees
     * Descripción: Permite buscar empleados mediante un objeto EmployeeQuery enviado en el cuerpo de la solicitud.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param query Objeto de consulta con los parámetros de búsqueda para empleados.
     * @return Response con la lista de empleados que cumplen los criterios.
     */
    @SneakyThrows
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchEmployees(@HeaderParam("Authorization") String token, EmployeeQuery query) {
        String employeeUUID = extractEmployeeUUID(token);
        List<Employee> employees = assistControlService.findEmployeesByQuery(employeeUUID, query);
        return Response.ok(employees).build();
    }

    /**
     * Función: insert
     * Descripción: Inserta un nuevo empleado en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto Employee en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param request Objeto Employee a insertar.
     * @return Response con el empleado insertado y estado CREATED.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insert(@HeaderParam("Authorization") String token, Employee request) throws Exception {
        String employeeUUID = extractEmployeeUUID(token);
        assistControlService.saveEmployee(employeeUUID, request, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(request).build();
    }

    /**
     * Función: update
     * Descripción: Actualiza la información de un empleado existente.
     * Requiere que el objeto Employee contenga un id válido y un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param request Objeto Employee con la información a actualizar.
     * @return Response con el empleado actualizado.
     * @throws NoDataException si el cuerpo de la solicitud no contiene la información del empleado.
     */
    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, Employee request) {
        if (request == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información del empleado");
        }
        String employeeUUID = extractEmployeeUUID(token);
        assistControlService.saveEmployee(employeeUUID, request, HttpMethod.PUT);
        return Response.ok(request).build();
    }

    /**
     * Función: delete
     * Descripción: Elimina un empleado identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del empleado a eliminar.
     * @return Response con el empleado eliminado.
     */
    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        String employeeUUID = extractEmployeeUUID(token);
        Employee employee = assistControlService.deleteEmployeeById(employeeUUID, id);
        return Response.ok(employee).build();
    }
}
