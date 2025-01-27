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
public class Commune extends BaseDomain implements Serializable {

    private City city;
    private String name;
    private String siiCode;
    private String tgrCode;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Commune valueOf(Long id) {
        Commune obj = new Commune();
        obj.setId(id);
        return obj;
    }

}
