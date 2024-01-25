package net.ktccenter.campusApi.dao.scolarite;

import net.ktccenter.campusApi.entities.administration.Branche;
import net.ktccenter.campusApi.entities.scolarite.Formateur;
import net.ktccenter.campusApi.entities.scolarite.Niveau;
import net.ktccenter.campusApi.entities.scolarite.Session;
import net.ktccenter.campusApi.entities.scolarite.Vague;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends PagingAndSortingRepository<Session, Long> {
  @Query("SELECT DISTINCT e FROM Session e WHERE e.code = :code")
  Optional<Session> findByCode(String code);

  @Query("SELECT DISTINCT e FROM Session e WHERE e.branche = :branche")
  List<Session> findAllByBranche(Branche branche);

  @Query("SELECT DISTINCT e FROM Session e WHERE e.niveau = :niveau")
  List<Session> findAllByNiveau(Niveau niveau);

  @Query("SELECT DISTINCT e FROM Session e WHERE e.formateur = :formateur")
  List<Session> findAllByFormateur(Formateur formateur);

  @Query("SELECT DISTINCT e FROM Session e WHERE e.vague = :vague")
  List<Session> findAllByVague(Vague vague);
}
