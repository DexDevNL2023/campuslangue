package net.ktccenter.campusApi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericService<E, D, R, L, I> {
    R save(D dto);
    List<L> save(List<I> dtos);
    void deleteById(Long id);
    R getOne(Long id);
    E findById(Long id);
    List<L> findAll();
    Page<L> findAll(Pageable pageable);
    R update(D dto, Long id);
}