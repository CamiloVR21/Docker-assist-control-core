package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class BranchQuery extends BaseDomain implements Serializable {

    private Long id;
    private CompanyQuery companyId;
    private String name;
    private String address;
    private Integer phone;
    private String alias;
    private Boolean active;

}
