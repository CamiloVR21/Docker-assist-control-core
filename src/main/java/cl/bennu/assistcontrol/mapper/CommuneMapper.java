package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Commune;
import cl.bennu.assistcontrol.domain.query.CommuneQuery;
import cl.bennu.commons.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
@CacheNamespace
public interface CommuneMapper extends BaseMapper<Commune> {

    List<Commune> findByQuery(CommuneQuery query);
    Commune getByQuery(CommuneQuery query);

}
