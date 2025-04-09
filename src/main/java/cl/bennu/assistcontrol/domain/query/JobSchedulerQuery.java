/**
 * JobSchedulerQuery se utiliza para definir los criterios de búsqueda de programaciones de trabajo en la aplicación.
 * Permite filtrar por propiedades como id, compañía, sucursal, nombre, y la configuración de cada día (lunes a domingo)
 * con sus respectivos horarios de inicio y fin.
 */
package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.sql.Time;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class JobSchedulerQuery extends BaseDomain implements Serializable {

    private Long id;
    private CompanyQuery company;
    private BranchQuery branch;
    private String name;
    private Boolean monday;
    private Time mondayFrom;
    private Time mondayTo;
    private Boolean tuesday;
    private Time tuesdayFrom;
    private Time tuesdayTo;
    private Boolean wednesday;
    private Time wednesdayFrom;
    private Time wednesdayTo;
    private Boolean thursday;
    private Time thursdayFrom;
    private Time thursdayTo;
    private Boolean friday;
    private Time fridayFrom;
    private Time fridayTo;
    private Boolean saturday;
    private Time saturdayFrom;
    private Time saturdayTo;
    private Boolean sunday;
    private Time sundayFrom;
    private Time sundayTo;
}
