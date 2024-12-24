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

public class JobType extends BaseDomain implements Serializable {

    private String name;
    private Company company;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static JobType valueOf(Long id) {
        JobType obj = new JobType();
        obj.setId(id);
        return obj;
    }

}
