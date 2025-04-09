package cl.bennu.assistcontrol.domain.query;

import cl.bennu.assistcontrol.enums.TypeUserEnum;
import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Base64;

/**
 * AppUserQuery se utiliza para definir los criterios de búsqueda de usuarios en la aplicación.
 * Permite filtrar por propiedades como id, nombre, email, código, password, imagen y tipo de usuario.
 * Además, incluye un método para obtener la imagen en formato Base64 y valida que el tamaño de la imagen no
 * exceda 1 MB al asignarla.
 */


@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class AppUserQuery extends BaseDomain implements Serializable {

    private Long id;
    private String name;
    private EmployeeQuery employeeId;
    private String email;
    private String code;
    private String password;
    private byte[] img;
    private TypeUserEnum typeUser;

    public String getImgBase64() {
        return img != null ? Base64.getEncoder().encodeToString(img) : null;
    }

    public void setImg(byte[] img) {
        if (img != null && img.length > 1048576) {
            throw new IllegalArgumentException("El tamaño de la imagen no puede exceder 1 MB.");
        }
        this.img = img;
    }

}
