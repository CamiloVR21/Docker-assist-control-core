package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.ext.web.FileUpload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)

public class Credential extends BaseDomain implements Serializable{

    private String email;
    private String password;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Credential valueOf(Long id) {
        Credential obj = new Credential();
        obj.setId(id);
        return obj;
    }
}
