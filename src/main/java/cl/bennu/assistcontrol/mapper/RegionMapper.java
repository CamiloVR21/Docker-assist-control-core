package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Region;
import cl.bennu.assistcontrol.domain.query.RegionQuery;
import cl.bennu.commons.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
@CacheNamespace

public interface RegionMapper extends BaseMapper<Region> {

    List<Region> findByQuery(RegionQuery query);
    Region getByQuery(RegionQuery query);
}
