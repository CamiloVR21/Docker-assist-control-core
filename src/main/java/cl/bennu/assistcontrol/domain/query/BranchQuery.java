package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * BranchQuery se utiliza para definir los criterios de búsqueda de sucursales en la aplicación.
 * Permite filtrar por propiedades como id, compañía, nombre, dirección, teléfono, alias y estado activo.
 */

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class BranchQuery extends BaseDomain implements Serializable {

    private Long id;
    private CompanyQuery companyId;
    private String name;
    private String address;
    private Integer phone;
    private String alias;
    private Boolean active;

}
