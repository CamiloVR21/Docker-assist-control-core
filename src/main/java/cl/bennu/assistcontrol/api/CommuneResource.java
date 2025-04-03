package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Commune;
import cl.bennu.assistcontrol.domain.query.CityQuery;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import java.util.List;

@Path("/commune")
@Produces(MediaType.APPLICATION_JSON)
public class CommuneResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    /**
     * Función: getAll
     * Descripción: Retorna la lista completa de comunas registradas en la aplicación.
     * Requiere un token de autorización en la cabecera para validar el acceso.
     *
     * @param token Token de autorización.
     * @return Response con la lista de comunas.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Commune> communes = assistControlService.getAllCommune(token);
        return Response.ok(communes).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información detallada de una comuna específica identificada por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador de la comuna.
     * @return Response con la información de la comuna.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Commune communeRequest = new Commune();
        communeRequest.setId(id);
        Commune commune = assistControlService.getCommuneById(token, communeRequest);
        return Response.ok(commune).build();
    }

    /**
     * Función: find
     * Descripción: Busca comunas basándose en parámetros de consulta:
     * - city-id: Identificador de la ciudad a la que pertenece la comuna.
     * - sii-code: Código SII de la comuna.
     * - tgr-code: Código TGR de la comuna.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param cityId Identificador de la ciudad.
     * @param siiCode Código SII de la comuna.
     * @param tgrCode Código TGR de la comuna.
     * @return Response con la lista de comunas que cumplen los criterios de búsqueda.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(
            @HeaderParam("Authorization") String token,
            @QueryParam("city-id") Long cityId,
            @QueryParam("sii-code") String siiCode,
            @QueryParam("tgr-code") String tgrCode) {

        CommuneQuery query = new CommuneQuery();

        if (cityId != null) {
            CityQuery city = new CityQuery();
            city.setId(cityId);
            query.setCity(city);
        }

        query.setTgrCode(tgrCode);
        query.setSiiCode(siiCode);

        List<Commune> communes = assistControlService.findCommuneByQuery(token, query);
        return Response.ok(communes).build();
    }

    /**
     * Función: insert
     * Descripción: Inserta una nueva comuna en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto Commune en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param commune Objeto Commune a insertar.
     * @return Response con la comuna insertada.
     */
    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Commune commune) {
        assistControlService.saveCommune(token, commune, HttpMethod.POST);
        return Response.ok(commune).build();
    }
}
