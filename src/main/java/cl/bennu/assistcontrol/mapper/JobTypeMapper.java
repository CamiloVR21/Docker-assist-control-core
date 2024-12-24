package cl.bennu.assistcontrol.mapper;

import cl.bennu.assistcontrol.domain.JobType;
import cl.bennu.assistcontrol.domain.query.JobTypeQuery;
import cl.bennu.commons.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@CacheNamespace
public interface JobTypeMapper extends BaseMapper<JobType> {
    List<JobType> findByQuery(JobTypeQuery query);

    JobType getByQuery(JobTypeQuery query);

}
