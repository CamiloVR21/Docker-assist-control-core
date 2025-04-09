/**
 * SaveEmployeeRequest se utiliza para encapsular la información necesaria para insertar o actualizar
 * un empleado junto con los datos del usuario de la aplicación (AppUser), como contraseña, imagen y tipo de usuario.
 * Los datos de nombre, correo y código pueden extraerse del empleado.
 */
package cl.bennu.assistcontrol.request;

import cl.bennu.assistcontrol.domain.AppUser;
import cl.bennu.assistcontrol.domain.Employee;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaveEmployeeRequest {

    private Employee employee;
    private AppUser appUser;
}
