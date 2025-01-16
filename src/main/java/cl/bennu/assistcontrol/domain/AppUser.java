package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.ext.web.FileUpload;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection
public class AppUser extends BaseDomain implements Serializable {

    private String name;
    private Company company;
    private String email;
    private String password;
    private byte[] img;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AppUser valueOf(Long id) {
        AppUser obj = new AppUser();
        obj.setId(id);
        return obj;
    }
}
