package cl.bennu.assistcontrol.domain.query;

import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)

public class RegisterQuery extends BaseDomain implements Serializable {

    private Long id;
    private EmployeeQuery employee;
    private Time startOfTheDay;
    private Time entry;
    private Time exit;
    private Time breakTime;
    private Date day;
}
