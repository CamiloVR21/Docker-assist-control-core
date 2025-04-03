/**
 * CompanyQuery se utiliza para definir los criterios de búsqueda de compañías en la aplicación.
 * Permite filtrar por propiedades como id, comuna, código, nombre, dirección, teléfono, alias, giro, email,
 * geolocalización, selfie y lag.
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
public class CompanyQuery extends BaseDomain implements Serializable {

    private Long id;
    private CommuneQuery commune;
    private String code;
    private String name;
    private String address;
    private Integer phone;
    private String alias;
    private String giro;
    private String email;
    private Boolean geolocation;
    private Boolean selfie;
    private Boolean lag;
}
