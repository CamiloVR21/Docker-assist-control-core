package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection

public class Register extends BaseDomain implements Serializable {

    private Employee employee;
    private Time startOfTheDay;
    private Time entry;
    private Time exit;
    private Time breakTime;
    private Date day;
    private JobType jobType;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Register valueOf(Long id) {
        Register obj = new Register();
        obj.setId(id);
        return obj;
    }
}
