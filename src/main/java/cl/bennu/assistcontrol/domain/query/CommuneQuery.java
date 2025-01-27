package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class CommuneQuery extends BaseDomain implements Serializable {

    private Long id;
    private CityQuery city;
    private String name;
    private String siiCode;
    private String tgrCode;

}
