package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.City;
import cl.bennu.assistcontrol.domain.query.CityQuery;
import cl.bennu.assistcontrol.domain.query.RegionQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import java.util.List;

@Path("/city")
@Produces(MediaType.APPLICATION_JSON)
public class CityResource extends BaseResource {
    private @Inject AssistControlService assistControlService;

    /**
     * Función: getAll
     * Descripción: Retorna la lista de todas las ciudades registradas.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @return Response con la lista de ciudades.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<City> city = assistControlService.getAllCity(token);
        return Response.ok(city).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información detallada de una ciudad específica identificada por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador de la ciudad.
     * @return Response con la información de la ciudad.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        City cityRequest = new City();
        cityRequest.setId(id);

        City city = assistControlService.getCityById(token, cityRequest);
        return Response.ok(city).build();
    }

    /**
     * Función: find
     * Descripción: Busca ciudades basándose en parámetros de consulta:
     * el identificador de la región y el nombre de la ciudad.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param regionId Identificador de la región a la que pertenece la ciudad.
     * @param name Nombre de la ciudad.
     * @return Response con la lista de ciudades que cumplen los criterios de búsqueda.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("region-id") Long regionId,
                         @QueryParam("name") String name) {
        CityQuery query = new CityQuery();

        if (regionId != null) {
            RegionQuery regionQuery = new RegionQuery();
            regionQuery.setId(regionId);
            query.setRegion(regionQuery);
        }
        query.setName(name);

        List<City> cities = assistControlService.findCityByQuery(token, query);
        return Response.ok(cities).build();
    }

    /**
     * Función: insert
     * Descripción: Inserta una nueva ciudad en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto City en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param city Objeto City a insertar.
     * @return Response con la ciudad insertada.
     */
    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, City city) {
        assistControlService.saveCity(token, city, HttpMethod.POST);
        return Response.ok(city).build();
    }
}
