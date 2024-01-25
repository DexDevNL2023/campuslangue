package net.ktccenter.campusApi.dao.cours;

import net.ktccenter.campusApi.entities.cours.Unite;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniteRepository extends PagingAndSortingRepository<Unite, Long> {
    @Query("SELECT DISTINCT e FROM Unite e WHERE e.code = :code")
    Optional<Unite> findByCode(String code);

    @Query("SELECT DISTINCT e FROM Unite e WHERE e.niveau = :niveau")
    List<Unite> findAllByNiveau(Niveau niveau);

    @Query("SELECT DISTINCT e FROM Unite e WHERE e.niveau.id = :niveauId")
    List<Unite> findAllByNiveauId(Long niveauId);
}
