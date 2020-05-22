package com.bailbots.tg.Framework.repository;

import java.util.List;

public interface KeyboardObjectRepository {
    List<Object> getObjectListByEntity(Class clazz, Integer page, int pageSize);
    int getPageNumbersOfObjectListByEntity(Class clazz, int pageSize);
}
