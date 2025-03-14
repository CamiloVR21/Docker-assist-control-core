package cl.bennu.assistcontrol.domain.query;

import cl.bennu.assistcontrol.enums.TypeUserEnum;
import cl.bennu.commons.domain.base.BaseDomain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Base64;

@Data
@NoArgsConstructor
@RegisterForReflection(registerFullHierarchy = true)
public class AppUserQuery extends BaseDomain implements Serializable {

    private Long id;
    private String name;
    private CompanyQuery companyId;
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
