package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.scolarite.Etudiant;
import net.ktccenter.campusApi.entities.scolarite.Inscription;
import net.ktccenter.campusApi.entities.scolarite.Session;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InscriptionRepository extends PagingAndSortingRepository<Inscription, Long> {
    @Query("SELECT DISTINCT e FROM Inscription e WHERE e.campusId = :campusId")
    List<Inscription> findAllByCampusId(Long campusId);
    @Query("SELECT DISTINCT e FROM Inscription e WHERE e.session = :session")
    List<Inscription> findAllBySession(Session session);
    @Query("SELECT DISTINCT e FROM Inscription e WHERE e.session = :session AND e.estRedoublant = :estRedoublant")
    List<Inscription> findAllBySessionAndEstRedoublant(Session session, Boolean estRedoublant);
    @Query("SELECT DISTINCT e FROM Inscription e WHERE e.session = :session OR e.campusId = :campusId")
    List<Inscription> findAllBySessionAndCampusId(Session session, Long campusId);
    @Query("SELECT DISTINCT e FROM Inscription e WHERE e.etudiant = :etudiant")
    List<Inscription> findAllByEtudiant(Etudiant etudiant);
    @Query("SELECT DISTINCT e FROM Inscription e WHERE e.etudiant = :etudiant AND e.campusId = :campusId")
    List<Inscription> findAllByEtudiantAndCampusId(Etudiant etudiant, Long campusId);
    @Query(value = "from Inscription t where dateInscription BETWEEN :startDate AND :endDate")
    List<Inscription> findAllBetweenDates(@Param("startDate")Date startDate,@Param("endDate") Date endDate);

    @Query("SELECT DISTINCT e FROM Inscription e WHERE e.etudiant = :etudiant AND e.session = :session")
    List<Inscription> findAllByEtudiantAndSession(Etudiant etudiant, Session session);
}
