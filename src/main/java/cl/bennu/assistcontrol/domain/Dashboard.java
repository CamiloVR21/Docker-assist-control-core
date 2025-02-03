package cl.bennu.assistcontrol.domain;

import cl.bennu.commons.domain.base.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class Dashboard extends BaseDomain implements Serializable {

    private List<Branch> branches;
    private List<Employee> employees;
    private List<JobType> roles;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Dashboard valueOf(Long id) {
        Dashboard obj = new Dashboard();
        obj.setId(id);
        return obj;
    }

    public Dashboard(List<Branch> branches, List<Employee> employees, List<JobType> roles) {
        this.branches = branches;
        this.employees = employees;
        this.roles = roles;
    }
}
