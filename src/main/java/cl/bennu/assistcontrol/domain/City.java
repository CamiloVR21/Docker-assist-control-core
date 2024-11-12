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

public class City extends BaseDomain implements Serializable {

    private Region region;
    private String name;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static City valueOf(Long id) {
        City obj = new City();
        obj.setId(id);
        return obj;
    }

}
