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

    /**
     * Función: getAll
     * Descripción: Retorna la lista de todas las compañías registradas.
     * Requiere el token de autorización en la cabecera para validar el acceso.
     *
     * @param token Token de autorización.
     * @return Response con la lista de compañías.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Company> companies = assistControlService.getAllCompany(token);
        return Response.ok(companies).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información detallada de una compañía específica identificada por su id.
     * Requiere el token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador de la compañía.
     * @return Response con los detalles de la compañía.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Company company = new Company();
        company.setId(id);
        Company result = assistControlService.getCompanyById(token, company);
        return Response.ok(result).build();
    }

    /**
     * Función: find
     * Descripción: Busca compañías basándose en parámetros de consulta:
     * - commune-id: Identificador de la comuna asociada a la compañía.
     * - code: Código de la compañía.
     * - name: Nombre de la compañía.
     * - address: Dirección de la compañía.
     * Requiere el token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param communeId Identificador de la comuna.
     * @param code Código de la compañía.
     * @param name Nombre de la compañía.
     * @param address Dirección de la compañía.
     * @return Response con la lista de compañías que cumplen los criterios de búsqueda.
     */
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

    /**
     * Función: getByEmployeeId
     * Descripción: Retorna la lista de compañías asociadas a un empleado específico.
     * Requiere el token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param employeeId Identificador del empleado.
     * @return Response con la lista de compañías del empleado.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-employee/{employeeId}")
    public Response getByEmployeeId(@HeaderParam("Authorization") String token, @PathParam("employeeId") Long employeeId) {
        List<Company> companies = assistControlService.getCompanyByEmployeeId(token, employeeId);
        return Response.ok(companies).build();
    }

    /**
     * Función: getByAppUserId
     * Descripción: Retorna la lista de compañías asociadas a un usuario de la aplicación específico.
     * Requiere el token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param appUserId Identificador del usuario de la aplicación.
     * @return Response con la lista de compañías del usuario.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-app-user/{appUserId}")
    public Response getByAppUserId(@HeaderParam("Authorization") String token, @PathParam("appUserId") Long appUserId) {
        List<Company> companies = assistControlService.getCompanyByAppUser(token, appUserId);
        return Response.ok(companies).build();
    }

    /**
     * Función: insert
     * Descripción: Inserta una nueva compañía en la aplicación.
     * Requiere el token de autorización en la cabecera y un objeto SaveCompanyRequest en el cuerpo de la solicitud.
     * Retorna la compañía insertada con el código de estado CREATED.
     *
     * @param token Token de autorización.
     * @param request Objeto que contiene la información de la compañía a insertar.
     * @return Response con la compañía insertada.
     */
    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, SaveCompanyRequest request) {
        assistControlService.saveCompany(token, request, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(request.getCompany()).build();
    }

    /**
     * Función: update
     * Descripción: Actualiza la información de una compañía existente.
     * Verifica que el cuerpo de la solicitud contenga la información de la compañía.
     * Requiere el token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param saveCompanyRequest Objeto que contiene la información actualizada de la compañía.
     * @return Response con la información actualizada de la compañía.
     */
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

    /**
     * Función: delete
     * Descripción: Elimina una compañía identificada por su id.
     * Requiere el token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador de la compañía a eliminar.
     * @return Response con la compañía eliminada.
     */
    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Company company = assistControlService.deleteCompanyById(token, id);
        return Response.ok(company).build();
    }
}
