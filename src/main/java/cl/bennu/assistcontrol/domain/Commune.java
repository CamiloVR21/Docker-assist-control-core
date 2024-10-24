package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection
public class Commune extends BaseDomain implements Serializable {

    private City city;
    private String name;
    private String siiCode;
    private String tgrCode;

}
