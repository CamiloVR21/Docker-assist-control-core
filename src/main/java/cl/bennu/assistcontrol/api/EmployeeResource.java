package cl.bennu.assistcontrol.api;

import cl.bennu.assistcontrol.api.base.BaseResource;
import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.Employee;
import cl.bennu.assistcontrol.domain.query.BranchQuery;
import cl.bennu.assistcontrol.domain.query.EmployeeQuery;
import cl.bennu.assistcontrol.service.AssistControlService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;

import java.util.List;

@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource extends BaseResource {

    private @Inject AssistControlService assistControlService;

    @SneakyThrows
    @GET
    public Response getAll(@HeaderParam("Authorization") String token) {
        List<Employee> employees = assistControlService.getAllEmployees(token);
        return Response.ok(employees).build();
    }

    @SneakyThrows
    @GET
    @Path("/{id}")
    public Response get(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Employee employee = assistControlService.getEmployeeById(token, id);
        return Response.ok(employee).build();
    }

    @SneakyThrows
    @GET
    @Path("/-/by-params")
    public Response find(@HeaderParam("Authorization") String token,
                         @QueryParam("name") String name,
                         @QueryParam("lastName") String lastName,
                         @QueryParam("address") String address,
                         @QueryParam("active") Boolean active) {
        EmployeeQuery query = new EmployeeQuery();
        query.setName(name);
        query.setLastName(lastName);
        query.setAddress(address);
        query.setActive(active);

        List<Employee> employees = assistControlService.findEmployeesByQuery(token, query);
        return Response.ok(employees).build();
    }

    @SneakyThrows
    @POST
    public Response insert(@HeaderParam("Authorization") String token, Employee employee) {
        assistControlService.saveEmployee(token, employee, HttpMethod.POST);
        return Response.status(Response.Status.CREATED).entity(employee).build();
    }

    @SneakyThrows
    @PUT
    @Path("/updateEmployee")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@HeaderParam("Authorization") String token, Employee employee) {
        if (employee.getId() == null) {
            throw new BadRequestException("El ID del empleado es obligatorio");
        }
        assistControlService.saveEmployee(token, employee, HttpMethod.PUT);
        return Response.ok(employee).build();
    }

    @SneakyThrows
    @DELETE
    @Path("/{id}")
    public Response delete(@HeaderParam("Authorization") String token, @PathParam("id") Long id) {
        Employee employee = assistControlService.deleteEmployeeById(token, id);
        return Response.ok(employee).build();
    }
}
