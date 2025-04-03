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

    /**
     * Función: getAll
     * Descripción: Retorna la lista de todas las sucursales registradas en la aplicación.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @return Response con la lista de sucursales.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Branch> branchies = assistControlService.getAllBranch(token);
        return Response.ok(branchies).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información de una sucursal específica identificada por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador de la sucursal.
     * @return Response con los detalles de la sucursal.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Branch branchRequest = new Branch();
        branchRequest.setId(id);

        Branch branch = assistControlService.getBranchById(token, branchRequest);
        return Response.ok(branch).build();
    }

    /**
     * Función: find
     * Descripción: Busca sucursales basándose en diferentes criterios de consulta:
     * empresa (company), nombre, dirección y estado de actividad.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param companyId Identificador de la empresa asociada.
     * @param name Nombre de la sucursal.
     * @param address Dirección de la sucursal.
     * @param active Estado de actividad de la sucursal.
     * @return Response con la lista de sucursales que cumplen los criterios.
     */
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

    /**
     * Función: insert
     * Descripción: Inserta una nueva sucursal en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto Branch en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param branch Objeto Branch a insertar.
     * @return Response con el objeto Branch insertado y el código de estado CREATED.
     */
    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Branch branch) {
        assistControlService.saveBranch(token, branch, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(branch).build();
    }

    /**
     * Función: update
     * Descripción: Actualiza la información de una sucursal existente.
     * Verifica que el cuerpo de la solicitud contenga la información y que se provea el id de la sucursal.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param branch Objeto Branch con la información a actualizar.
     * @return Response con el objeto Branch actualizado.
     */
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

    /**
     * Función: delete
     * Descripción: Elimina una sucursal identificada por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador de la sucursal a eliminar.
     * @return Response con el objeto Branch eliminado.
     */
    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Branch branch = assistControlService.deleteBranchById(token, id);
        return Response.ok(branch).build();
    }
}
