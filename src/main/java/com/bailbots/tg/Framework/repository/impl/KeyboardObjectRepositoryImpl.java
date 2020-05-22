package com.bailbots.tg.Framework.repository.impl;

import com.bailbots.tg.Framework.repository.KeyboardObjectRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Repository
public class KeyboardObjectRepositoryImpl implements KeyboardObjectRepository {
    private static final String GET_OBJECT_LIST_BY_ENTITY_QUERY = "select o from %s o";
    private static final String SELECT_OBJECT_COUNT_BY_ENTITY_QUERY = "select count (o) from %s o";

    private final EntityManager entityManager;

    public KeyboardObjectRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Object> getObjectListByEntity(Class clazz, Integer page, int pageSize) {
        Query query = entityManager.createQuery(String.format(GET_OBJECT_LIST_BY_ENTITY_QUERY, clazz.getSimpleName()));
        query.setFirstResult(page * pageSize)
                .setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public int getPageNumbersOfObjectListByEntity(Class clazz, int pageSize) {
        Query query = entityManager.createQuery(String.format(SELECT_OBJECT_COUNT_BY_ENTITY_QUERY, clazz.getSimpleName()));

        List<Long> result = query.getResultList();

        int pageIncrementer = result.get(0) % pageSize == 0 ? 0 : 1;

        return (int) (result.get(0) / pageSize + pageIncrementer);
    }

}
