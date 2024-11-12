package cl.bennu.assistcontrol.mapper;


import cl.bennu.assistcontrol.domain.JobScheduler;
import cl.bennu.assistcontrol.domain.query.JobSchedulerQuery;
import cl.bennu.commons.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
@CacheNamespace
public interface JobSchedulerMapper extends BaseMapper<JobScheduler> {

    List<JobScheduler> findByQuery(JobSchedulerQuery query);

    JobScheduler getByQuery(JobSchedulerQuery query);
}
