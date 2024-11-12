package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RegisterForReflection
public class CityQuery extends BaseDomain implements Serializable {

    private Long id;
    private RegionQuery region;
    private String name;
}
