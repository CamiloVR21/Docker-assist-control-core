package cl.bennu.assistcontrol.enums.TypeHandler;

import cl.bennu.assistcontrol.enums.JobTypeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(JobTypeEnum.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class JobTypeEnumTypeHandler extends BaseTypeHandler<JobTypeEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JobTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public JobTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        if (rs.wasNull()) {
            return null;
        }
        return fromId(id);
    }

    @Override
    public JobTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        if (rs.wasNull()) {
            return null;
        }
        return fromId(id);
    }

    @Override
    public JobTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        if (cs.wasNull()) {
            return null;
        }
        return fromId(id);
    }

    private JobTypeEnum fromId(int id) {
        for (JobTypeEnum e : JobTypeEnum.values()) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No enum constant JobTypeEnum con id " + id);
    }
}
