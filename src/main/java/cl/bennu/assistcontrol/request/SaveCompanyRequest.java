package cl.bennu.assistcontrol.request;

import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.Company;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaveCompanyRequest {

    private Company company;
    private Branch branch;
    private Boolean hq;

}
