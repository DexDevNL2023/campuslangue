package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.Diplome;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NiveauRepository extends PagingAndSortingRepository<Niveau, Long> {
    @Query("SELECT DISTINCT e FROM Niveau e WHERE e.code = :code")
    Optional<Niveau> findByCode(String code);

    @Query("SELECT DISTINCT e FROM Niveau e WHERE e.code = :code OR e.libelle = :libelle")
    Optional<Niveau> findByCodeAndLibelle(String code, String libelle);

    @Query("SELECT DISTINCT e FROM Niveau e WHERE e.diplomeFinFormation = :diplome OR e.diplomeRequis = :diplome")
    List<Niveau> findAllByDiplome(Diplome diplome);
}