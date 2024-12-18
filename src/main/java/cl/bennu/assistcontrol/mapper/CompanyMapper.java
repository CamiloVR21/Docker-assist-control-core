package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.query.CompanyQuery;
import cl.bennu.assistcontrol.enums.JobTypeEnum;
import cl.bennu.commons.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@CacheNamespace
public interface CompanyMapper extends BaseMapper<Company> {

    List<Company> findByQuery(CompanyQuery query);

    Company getByQuery(CompanyQuery query);

    List<JobTypeEnum> findJobTypesByCompany(@Param("companyId") Long companyId);
}