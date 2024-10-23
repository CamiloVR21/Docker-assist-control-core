package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.City;
import cl.bennu.assistcontrol.domain.query.CityQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.util.List;

@Path("/city")
@Produces(MediaType.APPLICATION_JSON)
public class CityResource extends BaseResource{
    private @Inject AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<City> city = assistControlService.getAllCity(token);
        return Response.ok(city).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        City city = assistControlService.getCityById(token, id);
        return Response.ok(city).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token
            , @PathParam("city-id") Long countryId
            , @PathParam("name") String name) {
        CityQuery query = new CityQuery();
        query.setCountryId(countryId);
        query.setName(name);


        List<City> city = assistControlService.findCityByQuery(token, query);
        return Response.ok(city).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, City city) {
        assistControlService.saveCity(token, city, HttpMethod.POST);
        return Response.ok(city).build();
    }
}