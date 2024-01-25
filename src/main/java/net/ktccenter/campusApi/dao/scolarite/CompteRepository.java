package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.Compte;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CompteRepository extends PagingAndSortingRepository<Compte, Long> {
    @Query("SELECT DISTINCT e FROM Compte e WHERE e.code = :code")
    Optional<Compte> findByCode(String code);

    @Query("SELECT DISTINCT e FROM Compte e WHERE e.inscription = :inscription")
    Compte findByInscription(Inscription inscription);
}
