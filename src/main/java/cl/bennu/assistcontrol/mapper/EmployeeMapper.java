package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Employee;
import cl.bennu.assistcontrol.domain.query.EmployeeQuery;
import cl.bennu.assistcontrol.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
@CacheNamespace
public interface EmployeeMapper extends BaseMapper<Employee> {

    List<Employee> findByQuery(EmployeeQuery query);
    Employee getByQuery(EmployeeQuery query);
    List<Employee> findByCompany(@Param("companyId") Long companyId);



}
