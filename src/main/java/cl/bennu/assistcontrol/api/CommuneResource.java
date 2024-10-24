package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Commune;
import cl.bennu.assistcontrol.domain.query.CityQuery;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.assistcontrol.domain.query.CountryQuery;
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

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Commune> communes = assistControlService.getAllCommune(token);
        return Response.ok(communes).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Commune commune = assistControlService.getCommuneById(token, id);
        return Response.ok(commune).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token
            , @PathParam("city-id") Long cityId
            , @PathParam("sii-code") String siiCode
            , @PathParam("tgr-code") String tgrCode) {
        CommuneQuery query = new CommuneQuery();
        CityQuery city = new CityQuery();
        city.setId(cityId);
        query.setTgrCode(tgrCode);
        query.setSiiCode(siiCode);

        List<Commune> communes = assistControlService.findCommuneByQuery(token, query);
        return Response.ok(communes).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Commune commune) {
        assistControlService.saveCommune(token, commune, HttpMethod.POST);
        return Response.ok(commune).build();
    }
}
