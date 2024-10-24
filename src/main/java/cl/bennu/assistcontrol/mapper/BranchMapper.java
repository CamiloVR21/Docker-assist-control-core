package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.Branch;
import cl.bennu.assistcontrol.domain.query.BranchQuery;
import cl.bennu.commons.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@CacheNamespace
public interface BranchMapper extends BaseMapper<Branch> {

    List<Branch> findByQuery(BranchQuery query);

    Branch getByQuery(BranchQuery query);

}
