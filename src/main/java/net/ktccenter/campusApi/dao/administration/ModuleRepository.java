package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Module;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepository extends PagingAndSortingRepository<Module, Long> {
    @Query("SELECT DISTINCT e FROM Module e WHERE e.name = :name")
    Optional<Module> findByName(String name);
}
