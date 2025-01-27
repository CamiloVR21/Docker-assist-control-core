package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Country;
import cl.bennu.assistcontrol.domain.query.CountryQuery;
import cl.bennu.assistcontrol.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
@CacheNamespace
public interface CountryMapper extends BaseMapper<Country> {

    List<Country> findByQuery(CountryQuery query);
    Country getByQuery(CountryQuery query);
}
