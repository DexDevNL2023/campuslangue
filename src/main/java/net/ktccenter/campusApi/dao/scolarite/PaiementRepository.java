package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.Compte;
import net.ktccenter.campusApi.entities.scolarite.ModePaiement;
import net.ktccenter.campusApi.entities.scolarite.Paiement;
import net.ktccenter.campusApi.entities.scolarite.Rubrique;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaiementRepository extends PagingAndSortingRepository<Paiement, Long> {

    @Query("SELECT DISTINCT e FROM Paiement e WHERE e.refPaiement = :ref")
    Optional<Paiement> findByRefPaiement(String ref);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.compte = :compte")
    List<Paiement> findAllByCompte(Compte compte);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.modePaiement = :modePaiement")
    List<Paiement> findAllByModePaiement(ModePaiement modePaiement);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.rubrique = :rubrique")
    List<Paiement> findAllByRubrique(Rubrique rubrique);

    @Query("SELECT DISTINCT p FROM Paiement p WHERE p.campusId = :campusId")
    List<Paiement> findAllByCampusId(Long campusId);
}
