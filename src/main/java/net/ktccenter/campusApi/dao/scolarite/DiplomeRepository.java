package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.Diplome;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiplomeRepository extends PagingAndSortingRepository<Diplome, Long> {
    @Query("SELECT DISTINCT e FROM Diplome e WHERE e.code = :code")
    Optional<Diplome> findByCode(String code);
    @Query("SELECT DISTINCT e FROM Diplome e WHERE e.code = :code OR e.libelle = :libelle")
    Optional<Diplome> findByCodeAndLibelle(String code, String libelle);

    Diplome findFirstByOrderById();
}