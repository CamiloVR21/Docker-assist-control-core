/**
 * RegisterQuery se utiliza para definir los criterios de búsqueda de registros de asistencia en la aplicación.
 * Permite filtrar por propiedades como id, empleado, horarios (inicio del día, entrada, salida, descanso, regreso)
 * y el día.
 */
package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class RegisterQuery extends BaseDomain implements Serializable {

    private Long id;
    private EmployeeQuery employee;
    private Time startOfTheDay;
    private Time entry;
    private Time exit;
    private Time breakTime;
    private Time backToWork;
    private Date day;
}
