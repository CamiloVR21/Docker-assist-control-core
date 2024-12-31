package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Registry;
import cl.bennu.assistcontrol.domain.query.EmployeeQuery;
import cl.bennu.assistcontrol.domain.query.RegistryQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.sql.Date;
import java.util.List;

@Path("/registry")
@Produces(MediaType.APPLICATION_JSON)
public class RegistryResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

        @SneakyThrows
        @GET
        public Response getAll(@HeaderParam("Authorization") String token) {
            List<Registry> registry = assistControlService.getAllRegistry(token);
            return Response.ok(registry).build();
        }

        @SneakyThrows
        @GET
        @Path("/{id}")
        public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
            Registry registry = new Registry();
            registry.setId(id);
            Registry result = assistControlService.getRegistryById(token, registry);
            return Response.ok(result).build();
        }

        @SneakyThrows
        @GET
        @Path("/-/by-params")
        public Response find(@HeaderParam("Authorization") String token,
                             @QueryParam("day")Date day,
                             @QueryParam("employeeId") Long employeeId) {
            RegistryQuery query = new RegistryQuery();
            if (employeeId != null) {
                EmployeeQuery employeeQuery = new EmployeeQuery();
                employeeQuery.setId(employeeId);
                query.setEmployee(employeeQuery);
            }
            query.setDay(day);


            List<Registry> registry = assistControlService.findRegistryByQuery(token, query);
            return Response.ok(registry).build();
        }

        @SneakyThrows
        @POST
        public Response insert(@HeaderParam("Authorization") String token, Registry request) {
            assistControlService.saveRegistry(token, request, HttpMethod.POST);
            return Response.status(Response.Status.CREATED).entity(request).build();
        }

        @SneakyThrows
        @PUT
        public Response update(@HeaderParam("Authorization") String token, Registry registry) {
            if (registry == null) {
                throw new NoDataException("El cuerpo de la solicitud no contiene la información de registro");
            }
            if (registry.getId() == null) {
                throw new NoDataException("El id del registro es requerido para una actualización");
            }
            assistControlService.saveRegistry(token, registry, HttpMethod.PUT);
            return Response.ok().build();
        }

        @SneakyThrows
        @DELETE
        @Path("/{id}")
        public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
            Registry registry = assistControlService.deleteRegistryById(token, id);
            return Response.ok(registry).build();
        }
}

