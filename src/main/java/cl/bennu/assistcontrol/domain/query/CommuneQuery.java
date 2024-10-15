package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RegisterForReflection
public class CommuneQuery extends BaseDomain implements Serializable {

    private Long id;
    private Long cityId;
    private String name;
    private String siiCode;
    private String tgrCode;

}
