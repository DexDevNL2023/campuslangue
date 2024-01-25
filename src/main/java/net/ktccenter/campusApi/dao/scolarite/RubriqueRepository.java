package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.Rubrique;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RubriqueRepository extends PagingAndSortingRepository<Rubrique, Long> {
    @Query("SELECT DISTINCT e FROM Rubrique e WHERE e.code = :code")
    Optional<Rubrique> findByCode(String code);

    @Query("SELECT DISTINCT e FROM Rubrique e WHERE e.code = :code OR e.libelle = :libelle")
    Optional<Rubrique> findByCodeAndLibelle(String code, String libelle);
}
