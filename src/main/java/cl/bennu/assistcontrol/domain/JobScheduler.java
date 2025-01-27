package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.sql.Time;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)

public class JobScheduler extends BaseDomain implements Serializable {

    private String name;
    private Boolean monday;
    private Time mondayFrom;
    private Time mondayTo;
    private Boolean tuesday;
    private Time tuesdayFrom;
    private Time tuesdayTo;
    private Boolean wednesday;
    private Time wednesdayFrom;
    private Time wednesdayTo;
    private Boolean thursday;
    private Time thursdayFrom;
    private Time thursdayTo;
    private Boolean friday;
    private Time fridayFrom;
    private Time fridayTo;
    private Boolean saturday;
    private Time saturdayFrom;
    private Time saturdayTo;
    private Boolean sunday;
    private Time sundayFrom;
    private Time sundayTo;
    private JobType jobType;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static JobScheduler valueOf(Long id) {
        JobScheduler obj = new JobScheduler();
        obj.setId(id);
        return obj;
    }
}
