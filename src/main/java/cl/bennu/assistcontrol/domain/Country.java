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
@RegisterForReflection(registerFullHierarchy = true)
public class Country extends BaseDomain implements Serializable {

    private String name;
    private String nationality;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Country valueOf(Long id) {
        Country obj = new Country();
        obj.setId(id);
        return obj;
    }
}
