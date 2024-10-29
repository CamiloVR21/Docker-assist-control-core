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

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Region> region = assistControlService.getAllRegion(token);
        return Response.ok(region).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Region region = new Region();
        region.setId(id);
        Region foundRegion = assistControlService.getRegionById(token, region);

        return Response.ok(foundRegion).build();
    }


    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token
            , @PathParam("country-id") Long countryId
            , @PathParam("name") String name) {
        RegionQuery query = new RegionQuery();

        CountryQuery countryQuery = new CountryQuery();
        countryQuery.setId(countryId);
        query.setName(name);

        List<Region> region = assistControlService.findRegionByQuery(token, query);
        return Response.ok(region).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Region region) {
        assistControlService.saveRegion(token, region, HttpMethod.POST);
        return Response.ok(region).build();
    }
}