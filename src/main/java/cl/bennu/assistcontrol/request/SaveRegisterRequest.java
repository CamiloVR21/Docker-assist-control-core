package cl.bennu.assistcontrol.request;

import cl.bennu.assistcontrol.domain.Register;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaveRegisterRequest {

    private Register register;
    private Long typeRegister;
}
