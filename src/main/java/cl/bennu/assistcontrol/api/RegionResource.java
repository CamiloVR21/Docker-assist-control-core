package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Region;
import cl.bennu.assistcontrol.domain.query.RegionQuery;
import cl.bennu.assistcontrol.domain.query.CountryQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import java.util.List;

@Path("/region")
@Produces(MediaType.APPLICATION_JSON)
public class RegionResource extends BaseResource {
    private @Inject AssistControlService assistControlService;

    /**
     * Función: getAll
     * Descripción: Retorna la lista completa de regiones registradas en la aplicación.
     * Requiere un token de autorización en la cabecera para validar el acceso.
     *
     * @param token Token de autorización.
     * @return Response con la lista de regiones.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Region> region = assistControlService.getAllRegion(token);
        return Response.ok(region).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información detallada de una región específica identificada por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador de la región.
     * @return Response con la información de la región encontrada.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Region region = new Region();
        region.setId(id);
        Region foundRegion = assistControlService.getRegionById(token, region);
        return Response.ok(foundRegion).build();
    }

    /**
     * Función: find
     * Descripción: Busca regiones basándose en parámetros de consulta.
     * Se puede filtrar por el identificador del país y/o el nombre de la región.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param countryId Identificador del país al que pertenece la región.
     * @param name Nombre de la región.
     * @return Response con la lista de regiones que cumplen con los criterios de búsqueda.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("country-id") Long countryId,
                         @QueryParam("name") String name) {
        RegionQuery query = new RegionQuery();

        if (countryId != null) {
            CountryQuery countryQuery = new CountryQuery();
            countryQuery.setId(countryId);
            query.setCountry(countryQuery);
        }

        query.setName(name);

        List<Region> region = assistControlService.findRegionByQuery(token, query);
        return Response.ok(region).build();
    }

    /**
     * Función: insert
     * Descripción: Inserta una nueva región en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto Region en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param region Objeto Region a insertar.
     * @return Response con la región insertada.
     */
    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Region region) {
        assistControlService.saveRegion(token, region, HttpMethod.POST);
        return Response.ok(region).build();
    }
}
