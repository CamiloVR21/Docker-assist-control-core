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
        City cityRequest = new City();
        cityRequest.setId(id);

        City city = assistControlService.getCityById(token, cityRequest);

        return Response.ok(city).build();
    }

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

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, City city) {
        assistControlService.saveCity(token, city, HttpMethod.POST);
        return Response.ok(city).build();
    }
}
