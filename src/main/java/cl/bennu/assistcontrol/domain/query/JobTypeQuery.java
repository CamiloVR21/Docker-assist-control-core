/**
 * JobTypeQuery se utiliza para definir los criterios de búsqueda de roles en la aplicación.
 * Permite filtrar por propiedades como id, nombre y compañía asociada.
 */
package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class JobTypeQuery extends BaseDomain implements Serializable {

    private Long id;
    private String name;
    private CompanyQuery company;
}
