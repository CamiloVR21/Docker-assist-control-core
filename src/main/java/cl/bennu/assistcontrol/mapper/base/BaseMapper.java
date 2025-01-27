package cl.bennu.assistcontrol.mapper.base;

import cl.bennu.commons.domain.base.BaseDomain;

import java.util.List;

public interface BaseMapper<T extends BaseDomain> {

    List<T> getAll();

    List<T> find(T type);

    T get(Long id);

    T getByName(String name);

    void insert(T type);

    void update(T type);

    void delete(Long id);

}
