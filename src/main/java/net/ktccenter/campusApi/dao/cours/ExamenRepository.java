package net.ktccenter.campusApi.dao.cours;

import net.ktccenter.campusApi.entities.cours.Examen;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamenRepository extends PagingAndSortingRepository<Examen, Long> {
    @Query("SELECT DISTINCT e FROM Examen e WHERE e.code = :code")
    Optional<Examen> findByCode(String code);

    @Query("SELECT DISTINCT e FROM Examen e WHERE e.inscription = :inscription")
    Examen findByInscription(Inscription inscription);

    @Query("SELECT DISTINCT e FROM Examen e WHERE e.inscription.session.id = :sessionId")
    List<Examen> findAllBySessionId(Long sessionId);
}
