package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Country;
import cl.bennu.assistcontrol.domain.query.CountryQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import java.util.List;

@Path("/country")
@Produces(MediaType.APPLICATION_JSON)
public class CountryResource extends BaseResource {
    private @Inject AssistControlService assistControlService;

    /**
     * Función: getAll
     * Descripción: Retorna la lista de todos los países registrados en la aplicación.
     * Requiere un token de autorización en la cabecera para validar el acceso.
     *
     * @param token Token de autorización.
     * @return Response con la lista de países.
     */
    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Country> country = assistControlService.getAllCountry(token);
        return Response.ok(country).build();
    }

    /**
     * Función: get
     * Descripción: Retorna la información detallada de un país específico identificado por su id.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param id Identificador del país.
     * @return Response con la información del país.
     */
    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Country countryRequest = new Country();
        countryRequest.setId(id);
        Country country = assistControlService.getCountryById(token, countryRequest);
        return Response.ok(country).build();
    }

    /**
     * Función: find
     * Descripción: Busca países basándose en parámetros de consulta:
     * - name: Nombre del país.
     * - nationality: Nacionalidad asociada.
     * Requiere un token de autorización en la cabecera.
     *
     * @param token Token de autorización.
     * @param name Nombre del país.
     * @param nationality Nacionalidad del país.
     * @return Response con la lista de países que cumplen los criterios de búsqueda.
     */
    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("name") String name,
                         @QueryParam("nationality") String nationality) {
        CountryQuery query = new CountryQuery();
        query.setName(name);
        query.setNationality(nationality);

        List<Country> country = assistControlService.findCountryByQuery(token, query);
        return Response.ok(country).build();
    }

    /**
     * Función: insert
     * Descripción: Inserta un nuevo país en la aplicación.
     * Requiere un token de autorización en la cabecera y un objeto Country en el cuerpo de la solicitud.
     *
     * @param token Token de autorización.
     * @param country Objeto Country a insertar.
     * @return Response con el país insertado.
     */
    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Country country) {
        assistControlService.saveCountry(token, country, HttpMethod.POST);
        return Response.ok(country).build();
    }
}
