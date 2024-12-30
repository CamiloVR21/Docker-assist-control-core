package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Region valueOf(Long id) {
        Region obj = new Region();
        obj.setId(id);
        return obj;
    }
}
