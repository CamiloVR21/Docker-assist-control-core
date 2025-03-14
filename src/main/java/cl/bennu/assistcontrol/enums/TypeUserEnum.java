package cl.bennu.assistcontrol.enums;

import cl.bennu.commons.enums.base.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

@Getter
@RegisterForReflection(registerFullHierarchy = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeUserEnum implements BaseEnum, Serializable {

    //@formatter:off
    ADMIN(1, "Administrador"),
    WORKER(2, "Trabajador");
    //@formatter:on

    TypeUserEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private final Integer id;
    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static TypeUserEnum valueOf(Object o) {
        if (o instanceof Integer id) {
            return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        } else if (o instanceof Map map) {
            Integer id = (Integer) map.get("id");
            return Arrays.stream(values()).filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        } else {
            return null;
        }
    }
}
