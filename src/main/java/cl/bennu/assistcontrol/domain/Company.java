package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection
public class Company extends BaseDomain implements Serializable {


    private Long    communeId;
    private String  code;
    private String  name;
    private String  address;
    private String  phone;
    private String  alias;
    private String  giro;
    private String  email;
    private Boolean geolocation;
    private Boolean selfie;
    private Boolean lag;
}
