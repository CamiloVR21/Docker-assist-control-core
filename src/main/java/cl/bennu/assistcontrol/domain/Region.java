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

public class Region extends BaseDomain implements Serializable {

    private Country country;
    private String name;
}
