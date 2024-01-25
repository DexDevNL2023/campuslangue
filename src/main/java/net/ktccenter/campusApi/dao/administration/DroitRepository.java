package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.Droit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroitRepository extends PagingAndSortingRepository<Droit, Long> {
    @Query("SELECT DISTINCT e FROM Droit e WHERE e.verbe = :verbe OR e.key = :key OR e.libelle = :libelle")
    Optional<Droit> findByVerbeAndKeyAndLibelle(String verbe, String key, String libelle);
    @Query("SELECT DISTINCT e FROM Droit e WHERE e.key = :key")

    Optional<Droit> findByKey(String key);
    @Query("SELECT DISTINCT e FROM Droit e WHERE e.libelle = :libelle")

    Optional<Droit> findByLibelle(String libelle);
}
