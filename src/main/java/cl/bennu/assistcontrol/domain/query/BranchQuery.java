package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RegisterForReflection
public class BranchQuery extends BaseDomain implements Serializable {

    private Long id;
    private Long companyId;
    private String name;
    private String address;
    private String phone;
    private String alias;
    private Boolean active;

}
