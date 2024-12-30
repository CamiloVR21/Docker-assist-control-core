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
public class Branch extends BaseDomain implements Serializable {

    private Company company;
    private String name;
    private String address;
    private String phone;
    private String alias;
    private Boolean active;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Branch valueOf(Long id) {
        Branch obj = new Branch();
        obj.setId(id);
        return obj;
    }
}
