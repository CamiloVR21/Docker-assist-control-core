package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.City;
import cl.bennu.assistcontrol.domain.query.CityQuery;
import cl.bennu.commons.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@CacheNamespace
public interface CityMapper extends BaseMapper<City>{
    List<City> findByQuery(CityQuery query);
    City getByQuery(CityQuery query);

}
