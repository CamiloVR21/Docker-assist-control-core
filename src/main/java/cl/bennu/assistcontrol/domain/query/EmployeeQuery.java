/**
 * EmployeeQuery se utiliza para definir los criterios de búsqueda de empleados en la aplicación.
 * Permite filtrar por propiedades como id, sucursal, programador de trabajos, comuna, país, género,
 * estado civil, tipo de contrato, tipo de trabajo, código, nombre, apellidos, fecha de nacimiento,
 * teléfono, email, dirección, fecha de contrato, fecha de fin de contrato y estado activo.
 */
package cl.bennu.assistcontrol.domain.query;

import cl.bennu.assistcontrol.enums.ContractTypeEnum;
import cl.bennu.assistcontrol.enums.GenderEnum;
import cl.bennu.assistcontrol.enums.MaritalStatusEnum;
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
public class EmployeeQuery extends BaseDomain implements Serializable {

    private Long id;
    private BranchQuery branch;
    private JobSchedulerQuery jobScheduler;
    private CommuneQuery commune;
    private CountryQuery country;
    private GenderEnum gender;
    private MaritalStatusEnum maritalStatus;
    private ContractTypeEnum contractType;
    private JobTypeQuery jobType;
    private String code;
    private String name;
    private String lastName;
    private String motherLastName;
    private Time birthDate;
    private Integer phone;
    private String email;
    private String address;
    private Time contractDate;
    private Time contractEndDate;
    private Boolean active;
}
