package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Institution;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends PagingAndSortingRepository<Institution, Long> {
    Optional<Institution> findFirstByOrderByName();
    Optional<Institution> findByName(String name);
}