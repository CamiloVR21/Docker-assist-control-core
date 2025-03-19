package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Register;
import cl.bennu.assistcontrol.domain.query.RegisterQuery;
import cl.bennu.assistcontrol.mapper.base.BaseMapper;
import cl.bennu.assistcontrol.request.SaveRegisterRequest;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@CacheNamespace
public interface RegisterMapper extends BaseMapper<Register> {

    List<Register> findByQuery(RegisterQuery query);
    Register getByQuery(RegisterQuery query);
    List<Register> findByCompanyId(@Param("companyId") Long companyId);
    List<Register> findByBranchId(@Param("branchId") Long branchId);

    void mergeRegister(SaveRegisterRequest request);
}
