package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Register;
import cl.bennu.assistcontrol.domain.query.EmployeeQuery;
import cl.bennu.assistcontrol.domain.query.RegisterQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import cl.bennu.commons.exception.NoDataException;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.sql.Date;
import java.util.List;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class RegisterResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Register> register = assistControlService.getAllRegistry(token);
        return Response.ok(register).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Register register = new Register();
        register.setId(id);
        Register result = assistControlService.getRegistryById(token, register);
        return Response.ok(result).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("day") Date day,
                         @QueryParam("employeeId") Long employeeId) {
        RegisterQuery query = new RegisterQuery();
        if (employeeId != null) {
            EmployeeQuery employeeQuery = new EmployeeQuery();
            employeeQuery.setId(employeeId);
            query.setEmployee(employeeQuery);
        }
        query.setDay(day);


        List<Register> register = assistControlService.findRegistryByQuery(token, query);
        return Response.ok(register).build();
    }

    @GET
    @Path("/company/{companyId}")
    public Response getByCompanyId(@PathParam("companyId") Long companyId) {
        try {
            List<Register> registries = assistControlService.findByCompanyId(companyId);
            return Response.ok(registries).build();
        } catch (NoDataException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/branch/{branchId}")
    public Response getByBranchId(@PathParam("branchId") Long branchId) {
        try {
            List<Register> registries = assistControlService.findByBranchId(branchId);
            return Response.ok(registries).build();
        } catch (NoDataException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }


    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Register request) {
        assistControlService.saveRegistry(token, request, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(request).build();
    }



    @SneakyThrows
    @PUT
    public Response update(@HeaderParam("Authorization") String token, Register register) {
        if (register == null) {
            throw new NoDataException("El cuerpo de la solicitud no contiene la información de registro");
        }
        if (register.getId() == null) {
            throw new NoDataException("El id del registro es requerido para una actualización");
        }
        assistControlService.saveRegistry(token, register, HttpMethod.PUT);
        return Response.ok().build();
    }

    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Register register = assistControlService.deleteRegistryById(token, id);
        return Response.ok(register).build();
    }







}

