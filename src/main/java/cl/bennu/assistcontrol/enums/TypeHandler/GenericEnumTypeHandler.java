package cl.bennu.assistcontrol.enums.TypeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenericEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final Class<E> type;

    public GenericEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        try {
            int id = (int) type.getMethod("getId").invoke(parameter);
            ps.setInt(i, id);
        } catch (Exception e) {
            throw new SQLException("Error setting non-null parameter for enum " + type.getSimpleName(), e);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return getEnumById(id);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return getEnumById(id);
    }

    @Override
    public E getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return getEnumById(id);
    }

    private E getEnumById(int id) {
        try {
            return (E) type.getMethod("valueOf", Object.class).invoke(null, id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot convert " + id + " to " + type.getSimpleName() + " by ID.", e);
        }
    }

}
