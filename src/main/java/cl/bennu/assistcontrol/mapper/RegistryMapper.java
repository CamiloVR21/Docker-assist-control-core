package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Region;
import cl.bennu.assistcontrol.domain.Registry;
import cl.bennu.assistcontrol.domain.query.RegistryQuery;
import cl.bennu.commons.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@CacheNamespace
public interface RegistryMapper extends BaseMapper<Registry> {

    List<Registry> findByQuery(RegistryQuery query);
    Registry getByQuery(RegistryQuery query);
    List<Registry> findByCompanyId(@Param("companyId") Long companyId);
    List<Registry> findByBranchId(@Param("branchId") Long branchId);
}
