package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class AppUserQuery extends BaseDomain implements Serializable {

    private Long id;
    private String name;
    private CompanyQuery companyId;
    private String email;
    private String password;
    private byte[] img;

}
