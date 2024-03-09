package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.LogImpression;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogImpressionRepository extends PagingAndSortingRepository<LogImpression, Long> {
    @Query("SELECT DISTINCT e FROM LogImpression e WHERE e.inscription.id = :inscriptionId")
    List<LogImpression> findAllByInscriptionId(Long inscriptionId);
}
