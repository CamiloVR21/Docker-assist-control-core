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
public class Company extends BaseDomain implements Serializable {

    private Commune commune;
    private String code;
    private String name;
    private String address;
    private String phone;
    private String alias;
    private String giro;
    private String email;
    private Boolean geolocation;
    private Boolean selfie;
    private Boolean lag;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Company valueOf(Long id) {
        Company obj = new Company();
        obj.setId(id);
        return obj;
    }
}
