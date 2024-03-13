package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.ParametreInstitution;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametreInstitutionRepository extends PagingAndSortingRepository<ParametreInstitution, Long> {
    ParametreInstitution findFirstByOrderById();
}