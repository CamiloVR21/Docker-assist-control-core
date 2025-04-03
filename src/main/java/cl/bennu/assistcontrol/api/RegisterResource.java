package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Register;
import cl.bennu.assistcontrol.domain.query.EmployeeQuery;
import cl.bennu.assistcontrol.domain.query.RegisterQuery;
import cl.bennu.assistcontrol.request.SaveRegisterRequest;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import cl.bennu.commons.exception.UniqueException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.sql.Date;
import java.util.List;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    /**
     * Función: getAll
     * Descripción: Retorna la lista completa de registros de asistencia almacenados en la aplicación.
     * Requiere un token de autorización en la cabecera para validar el acceso.
     *
     * @param token Token de autorización.
     * @return Response con la lista de registros.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Register> registers = assistControlService.getAllRegistry(token);
        return Response.ok(registers).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información detallada de un registro específico identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del registro.
     * @return Response con la información del registro.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Register register = new Register();
        register.setId(id);
        Register result = assistControlService.getRegistryById(token, register);
        return Response.ok(result).build();
    }

    /**
     * Función: find
     * Descripción: Busca registros de asistencia basándose en parámetros de consulta:
     * - day: Fecha específica para filtrar registros.
     * - employeeId: Identificador del empleado al cual pertenece el registro.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param day Fecha para filtrar los registros.
     * @param employeeId Identificador del empleado.
     * @return Response con la lista de registros que cumplen los criterios de búsqueda.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("day") Date day,
                         @QueryParam("employeeId") Long employeeId) {
        RegisterQuery query = new RegisterQuery();
        if (employeeId != null) {
            EmployeeQuery employeeQuery = new EmployeeQuery();
            employeeQuery.setId(employeeId);
            query.setEmployee(employeeQuery);
        }
        query.setDay(day);
        List<Register> registers = assistControlService.findRegistryByQuery(token, query);
        return Response.ok(registers).build();
    }

    /**
     * Función: getByCompanyId
     * Descripción: Retorna la lista de registros de asistencia asociados a una compañía específica.
     *
     * @param companyId Identificador de la compañía.
     * @return Response con la lista de registros encontrados o un error si no se encuentran datos.
     */
    @GET
    @Path("/company/{companyId}")
    public Response getByCompanyId(@PathParam("companyId") Long companyId) {
        try {
            List<Register> registries = assistControlService.findByCompanyId(companyId);
            return Response.ok(registries).build();
        } catch (NoDataException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Función: getByBranchId
     * Descripción: Retorna la lista de registros de asistencia asociados a una sucursal específica.
     *
     * @param branchId Identificador de la sucursal.
     * @return Response con la lista de registros encontrados o un error si no se encuentran datos.
     */
    @GET
    @Path("/branch/{branchId}")
    public Response getByBranchId(@PathParam("branchId") Long branchId) {
        try {
            List<Register> registries = assistControlService.findByBranchId(branchId);
            return Response.ok(registries).build();
        } catch (NoDataException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    /**
     * Función: saveRegister
     * Descripción: Procesa e inserta un nuevo registro de asistencia en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto SaveRegisterRequest en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param request Objeto que contiene la información necesaria para procesar el registro.
     * @return Response con el registro procesado.
     * @throws NoDataException Si faltan datos requeridos.
     * @throws UniqueException Si se viola una restricción de unicidad.
     */
    @POST
    @SneakyThrows
    public Response saveRegister(@HeaderParam("Authorization") String token, SaveRegisterRequest request)
            throws NoDataException, UniqueException {
        Register reg = assistControlService.processRegister(token, request);
        return Response.status(Response.Status.OK).entity(reg).build();
    }

    /**
     * Función: update
     * Descripción: Actualiza la información de un registro de asistencia existente.
     * Verifica que el objeto Register y su id sean proporcionados.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param register Objeto Register con la información actualizada.
     * @return Response indicando que la actualización se realizó exitosamente.
     * @throws NoDataException Si el objeto Register es nulo o no contiene un id.
     */
    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, Register register) {
        if (register == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información de registro");
        }
        if (register.getId() == null) {
            throw new NoDataException("El id del registro es requerido para una actualización");
        }
        assistControlService.saveRegistry(token, register, HttpMethod.PUT);
        return Response.ok().build();
    }

    /**
     * Función: delete
     * Descripción: Elimina un registro de asistencia identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del registro a eliminar.
     * @return Response con el registro eliminado.
     */
    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Register register = assistControlService.deleteRegistryById(token, id);
        return Response.ok(register).build();
    }
}
