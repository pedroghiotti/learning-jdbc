package com.pedroghiotti.learning.data.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Dao<T, Id extends UUID> {
    T create(T entity);
    List<T> getAll();
    Optional<T> getById(Id id);
    T update(T entity);
    void delete(Id id);
}
