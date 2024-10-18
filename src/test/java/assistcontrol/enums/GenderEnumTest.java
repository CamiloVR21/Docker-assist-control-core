package assistcontrol.enums;

import cl.bennu.commons.enums.base.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

@Getter
@RegisterForReflection
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GenderEnumTest implements BaseEnum {

    //@formatter:off
    MALE(1, "Masculino")
    , FEME(2, "Femenino");
    //@formatter:on

    GenderEnumTest(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private final Integer id;
    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GenderEnumTest valueOf(Object o) {
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
