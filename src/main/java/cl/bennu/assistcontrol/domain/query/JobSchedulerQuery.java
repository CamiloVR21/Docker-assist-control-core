package cl.bennu.assistcontrol.domain.query;

import cl.bennu.assistcontrol.enums.JobTypeEnum;
import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;

@Data
@NoArgsConstructor
@RegisterForReflection
public class JobSchedulerQuery extends BaseDomain implements Serializable {

    private Long id;
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

}
