/**
 * SaveCompanyRequest se utiliza para encapsular la información necesaria para guardar o actualizar una compañía.
 * Incluye datos de la compañía, la sucursal asociada y un indicador booleano que especifica si la sucursal es la sede central.
 */
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
