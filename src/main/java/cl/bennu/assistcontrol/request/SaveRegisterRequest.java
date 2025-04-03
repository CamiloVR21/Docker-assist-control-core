/**
 * SaveRegisterRequest se utiliza para encapsular la información necesaria para guardar o actualizar un registro de asistencia.
 * Contiene un objeto Register y un identificador que indica el tipo de registro.
 */
package cl.bennu.assistcontrol.request;

import cl.bennu.assistcontrol.domain.Register;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaveRegisterRequest {

    private Register register;
    private Long typeRegister;
}
