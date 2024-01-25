package net.ktccenter.campusApi.dao.cours;

import net.ktccenter.campusApi.entities.cours.Epreuve;
import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.cours.Unite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EpreuveRepository extends PagingAndSortingRepository<Epreuve, Long> {
    @Query("SELECT DISTINCT e FROM Epreuve e WHERE e.unite = :unite")
    List<Epreuve> findAllByUnite(Unite unite);

    @Query("SELECT DISTINCT e FROM Epreuve e WHERE e.examen = :examen")
    List<Epreuve> findAllByExamen(Examen examen);

    @Query("SELECT DISTINCT e FROM Epreuve e WHERE e.examen.id = :examenId AND e.unite.id = :uniteId")
    List<Epreuve> findAllByExamenIdAndUniteId(Long examenId, Long uniteId);
}
