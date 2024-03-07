package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.JourOuvrable;
import net.ktccenter.campusApi.enums.Jour;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourOuvrableRepository extends PagingAndSortingRepository<JourOuvrable, Long> {
    @Query("SELECT DISTINCT e FROM JourOuvrable e WHERE e.jour = :jour")
    JourOuvrable findByJour(Jour jour);
}