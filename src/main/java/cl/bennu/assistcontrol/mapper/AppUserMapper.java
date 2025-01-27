package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.AppUser;
import cl.bennu.assistcontrol.domain.Company;
import cl.bennu.assistcontrol.domain.query.AppUserQuery;
import cl.bennu.assistcontrol.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@CacheNamespace
public interface AppUserMapper extends BaseMapper<AppUser> {

    List<AppUser> findByQuery(AppUserQuery query);
    AppUser getByQuery(AppUserQuery query);
    Company getCompanyInfoByUser(@Param("userId") Long userId);

}
