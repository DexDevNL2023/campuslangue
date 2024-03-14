package net.ktccenter.campusApi.dao.administration;

import net.ktccenter.campusApi.entities.administration.OccupationSalle;
import net.ktccenter.campusApi.entities.administration.Salle;
import net.ktccenter.campusApi.entities.cours.PlageHoraire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OccupationSalleRepository extends PagingAndSortingRepository<OccupationSalle, Long> {
    @Query("SELECT DISTINCT e FROM OccupationSalle e WHERE e.salle = :salle AND e.estOccupee = :estOccupee")
    List<OccupationSalle> findAllBySalleAndEstOccupee(Salle salle, Boolean estOccupee);

    @Query("SELECT DISTINCT e FROM OccupationSalle e WHERE e.code = :code")
    Optional<OccupationSalle> findByCode(String code);

    @Query("SELECT DISTINCT e FROM OccupationSalle e WHERE e.salle = :salle")
    List<OccupationSalle> findAllBySalle(Salle salle);

    @Query("SELECT DISTINCT e FROM OccupationSalle e WHERE e.plageHoraire = :plage")
    List<OccupationSalle> findAllByPlageHoraire(PlageHoraire plage);

    @Query("SELECT DISTINCT e FROM OccupationSalle e WHERE e.plageHoraire = :plage AND e.estOccupee = :estOccupee")
    List<OccupationSalle> findAllByPlageHoraireAndEstOccupee(PlageHoraire plage, Boolean estOccupee);

    @Query("SELECT DISTINCT e FROM OccupationSalle e WHERE e.plageHoraire = :plage AND e.salle = :salle")
    Optional<OccupationSalle> findByPlageHoraireAndSalle(PlageHoraire plage, Salle salle);

}
