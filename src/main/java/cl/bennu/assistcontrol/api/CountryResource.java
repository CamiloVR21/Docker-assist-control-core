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

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Country> country = assistControlService.getAllCountry(token);
        return Response.ok(country).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Country countryRequest = new Country();
        countryRequest.setId(id);
        Country country = assistControlService.getCountryById(token, countryRequest);
        return Response.ok(country).build();
    }


    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token
            , @PathParam("name") String name
            , @PathParam("nationality") String nationality) {
        CountryQuery query = new CountryQuery();
        query.setName(name);
        query.setNationality(nationality);


        List<Country> country = assistControlService.findCountryByQuery(token, query);
        return Response.ok(country).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Country country) {
        assistControlService.saveCountry(token, country, HttpMethod.POST);
        return Response.ok(country).build();
    }
}