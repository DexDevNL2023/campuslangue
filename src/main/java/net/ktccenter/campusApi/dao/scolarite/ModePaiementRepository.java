package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.ModePaiement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModePaiementRepository extends PagingAndSortingRepository<ModePaiement, Long> {
    @Query("SELECT DISTINCT e FROM ModePaiement e WHERE e.code = :code")
    Optional<ModePaiement> findByCode(String code);

    @Query("SELECT DISTINCT e FROM ModePaiement e WHERE e.code = :code OR e.libelle = :libelle")
    Optional<ModePaiement> findByCodeAndLibelle(String code, String libelle);
}
