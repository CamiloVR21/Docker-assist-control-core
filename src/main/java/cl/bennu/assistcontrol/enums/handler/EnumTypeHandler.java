/**
 * EnumTypeHandler se utiliza para mapear enumeraciones que implementan BaseEnum a columnas de tipo entero en la base de datos.
 * Facilita la conversión entre el valor numérico almacenado y el valor enumerado en Java.
 */
package cl.bennu.assistcontrol.enums.handler;

import cl.bennu.commons.enums.base.BaseEnum;
import cl.bennu.commons.exception.NoDataException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

@MappedTypes(BaseEnum.class)
public class EnumTypeHandler<T> implements TypeHandler<BaseEnum> {

    private final Class<T> clazz;

    public EnumTypeHandler(Class<T> clazz) throws NoDataException {
        if (clazz == null) throw new NoDataException("No class defined");
        this.clazz = clazz;
    }

    public BaseEnum findEnum(Integer id) {
        BaseEnum[] enums = (BaseEnum[]) clazz.getEnumConstants();
        for (BaseEnum baseEnum : enums) {
            if (baseEnum.getId().equals(id)) {
                return baseEnum;
            }
        }
        return null;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, BaseEnum parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, Types.INTEGER);
        } else {
            ps.setInt(i, parameter.getId());
        }
    }

    @Override
    public BaseEnum getResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return value == 0 ? null : findEnum(value);
    }

    @Override
    public BaseEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return value == 0 ? null : findEnum(value);
    }

    @Override
    public BaseEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return value == 0 ? null : findEnum(value);
    }
}
