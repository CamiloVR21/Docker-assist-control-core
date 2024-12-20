package cl.bennu.assistcontrol.domain;

import cl.bennu.assistcontrol.enums.ContractTypeEnum;
import cl.bennu.assistcontrol.enums.GenderEnum;
import cl.bennu.assistcontrol.enums.JobTypeEnum;
import cl.bennu.assistcontrol.enums.MaritalStatusEnum;
import cl.bennu.commons.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection

public class Employee extends BaseDomain implements Serializable {

    private Branch branch;
    private JobScheduler jobScheduler;
    private Commune commune;
    private Country country;
    private GenderEnum gender;
    private MaritalStatusEnum maritalStatus;
    private ContractTypeEnum contractType;
    private JobTypeEnum jobType;
    private String code;
    private String name;
    private String lastName;
    private String motherLastName;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private LocalDate contractDate;
    private LocalDate contractEndDate;
    private Boolean active;


    public Employee(JobTypeEnum jobType, String name) {
        this.jobType = jobType;
        this.name = name;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Employee valueOf(Long id) {
        Employee obj = new Employee();
        obj.setId(id);
        return obj;
    }
}
